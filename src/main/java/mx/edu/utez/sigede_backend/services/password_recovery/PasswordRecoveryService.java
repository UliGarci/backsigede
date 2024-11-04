package mx.edu.utez.sigede_backend.services.password_recovery;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.UUID;

import mx.edu.utez.sigede_backend.controllers.password_recovery.dto.PasswordChangeResponseDTO;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import mx.edu.utez.sigede_backend.models.user_account.UserAccount;
import mx.edu.utez.sigede_backend.models.user_account.UserAccountRepository;
import mx.edu.utez.sigede_backend.models.verification_code.VerificationCode;
import mx.edu.utez.sigede_backend.models.verification_code.VerificationCodeRepository;
import mx.edu.utez.sigede_backend.utils.exception.CustomException;

@Service
public class PasswordRecoveryService {
    private final UserAccountRepository userAccountRepository;
    private final VerificationCodeRepository verificationCodeRepository;
    private final PasswordEncoder passwordEncoder;
    private static final SecureRandom random = new SecureRandom();
    private static final int CODE_LENGTH = 6;
    private static final String USER_NOT_FOUND = "user.not.found";

    public PasswordRecoveryService(UserAccountRepository userAccountRepository,
            VerificationCodeRepository verificationCodeRepository, PasswordEncoder passwordEncoder) {
        this.userAccountRepository = userAccountRepository;
        this.verificationCodeRepository = verificationCodeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UUID sendVerificationCode(String email) {
        if (!userAccountRepository.existsByEmail(email)) {
            throw new CustomException(USER_NOT_FOUND);
        }
        UserAccount user = userAccountRepository.findByEmail(email);
        return saveVerificationCode(user, verificationCodeRepository);
    }

    private static UUID saveVerificationCode(UserAccount user, VerificationCodeRepository verificationCodeRepository) {
        String code = generateVerificationCode();
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime expiration = createdAt.plusHours(1);
        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setVerificationCode(code);
        verificationCode.setCreatedAt(createdAt);
        verificationCode.setExpiration(expiration);
        verificationCode.setFkUserAccount(user);
        verificationCodeRepository.saveAndFlush(verificationCode);
        return user.getUserAccountId();
    }

    @Transactional
    public boolean validateVerificationCode(String code, UUID userId) {
        if (!userAccountRepository.existsByUserAccountId(userId)) {
            throw new CustomException(USER_NOT_FOUND);
        }
        UserAccount user = userAccountRepository.findByUserAccountId(userId);
        VerificationCode databaseCode = verificationCodeRepository.findByFkUserAccount(user);
        if (LocalDateTime.now().isAfter(databaseCode.getExpiration())) {
            throw new CustomException("verification.code.expired");
        }
        return databaseCode.getVerificationCode().equals(code);
    }

    @Transactional
    public PasswordChangeResponseDTO changePassword(String newPassword, UUID userId) {

        if (!userAccountRepository.existsByUserAccountId(userId)) {
            throw new CustomException(USER_NOT_FOUND);
        }
        UserAccount user = userAccountRepository.findByUserAccountId(userId);
        if (passwordEncoder.matches(newPassword, user.getPassword())) {
            throw new CustomException("user.password.same_as_old");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userAccountRepository.save(user);
        // servicio para enviar email
        return new PasswordChangeResponseDTO(user.getPassword(), user.getUserAccountId());
    }

    @Transactional
    public UUID resendVerificationCode(String email) {
        UserAccount user = userAccountRepository.findByEmail(email);
        return saveVerificationCode(user, verificationCodeRepository);
    }

    @Transactional
    public void deleteVerificationCode(UUID userId) {
        UserAccount user = userAccountRepository.findByUserAccountId(userId);
        verificationCodeRepository.deleteByFkUserAccount(user);
    }

    private static String generateVerificationCode() {
        int min = (int) Math.pow(10, CODE_LENGTH - 1);
        int max = (int) Math.pow(10, CODE_LENGTH) - 1;
        int code = random.nextInt((max - min) + 1) + min;
        return String.valueOf(code);
    }
}
