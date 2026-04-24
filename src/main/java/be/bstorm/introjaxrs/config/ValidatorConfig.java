package be.bstorm.introjaxrs.config;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.ws.rs.Produces;

@ApplicationScoped
public class ValidatorConfig {

    @Produces
    @ApplicationScoped
    public Validator validator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        return factory.getValidator();
    }
}
