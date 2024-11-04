package mx.edu.utez.sigede_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

import java.awt.Desktop;
import java.net.URI;

import lombok.extern.slf4j.Slf4j;



@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
@Slf4j
public class SigedeBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(SigedeBackendApplication.class, args);
		showSwaggerInfo();
	}

	@Bean
	public OpenAPI customApi() {
		return new OpenAPI().info(
			new Info()
				.title("SIGEDE API's")
				.version("0.1")
				.termsOfService("http://swagger.io/terms/")
				.license(new License().name("Apache 2.0").url("http://springdoc.org"))
		);
	}

	private static void showSwaggerInfo() {
		String swaggerURL = "http://localhost:8080/doc/swagger-ui/index.html";

		try{
			URI swaggerUri = new URI(swaggerURL);

			if(Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
				Desktop.getDesktop().browse(swaggerUri);
				log.info("Swagger UI se ha abierto en el navegador: {}", swaggerURL);
			} else {
				log.warn("No se pudo abrir el navegador automáticamente. Accede manualmente a {}", swaggerURL);
			}
		} catch (Exception e) {
			log.error("Error al intentar abrir Swagger UI: {}. Accede manualmente a {}", e.getMessage(), swaggerURL);
		}
	}

}
