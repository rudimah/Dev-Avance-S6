package tp_avancee_dev.tp_avancee.api;

import io.swagger.v3.jaxrs2.integration.resources.OpenApiResource;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.validation.ValidationFeature;
import tp_avancee_dev.tp_avancee.api.security.AuthenticationFilter;

import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/api")
public class ApiApplication extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<>();
        classes.add(AnnonceResource.class);
        classes.add(JacksonFeature.class);
        classes.add(ValidationFeature.class);
        classes.add(AuthResource.class);
        classes.add(AuthenticationFilter.class);
        classes.add(OpenApiResource.class);
        return classes;
    }
}