package be.bstorm.introjaxrs;

import io.swagger.v3.jaxrs2.integration.resources.AcceptHeaderOpenApiResource;
import io.swagger.v3.jaxrs2.integration.resources.OpenApiResource;
import jakarta.ws.rs.ApplicationPath;
import org.glassfish.jersey.server.ResourceConfig;

@ApplicationPath("/api")
public class HelloApplication extends ResourceConfig {

	public HelloApplication() {
		packages("be.bstorm.introjaxrs");
		register(OpenApiResource.class);
		register(AcceptHeaderOpenApiResource.class);
	}
}



