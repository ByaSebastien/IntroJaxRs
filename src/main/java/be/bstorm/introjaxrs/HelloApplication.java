package be.bstorm.introjaxrs;

import io.swagger.v3.jaxrs2.integration.resources.AcceptHeaderOpenApiResource;
import io.swagger.v3.jaxrs2.integration.resources.OpenApiResource;
import jakarta.ws.rs.ApplicationPath;
import org.glassfish.jersey.server.ResourceConfig;

/**
 * Configuration principale de l'application JAX-RS.
 *
 * Cette classe configure le chemin racinale de l'API ("/api") et enregistre
 * les ressources JAX-RS ainsi que les ressources Swagger pour la documentation OpenAPI.
 *
 * @author IntroJaxRs
 * @version 1.0
 */
@ApplicationPath("/api")
public class HelloApplication extends ResourceConfig {

	/**
	 * Constructeur qui initialise la configuration de l'application.
	 * Scanne le package {@code be.bstorm.introjaxrs} pour les ressources JAX-RS
	 * et enregistre les ressources OpenAPI.
	 */
	public HelloApplication() {
		packages("be.bstorm.introjaxrs");
		register(OpenApiResource.class);
		register(AcceptHeaderOpenApiResource.class);
	}
}
