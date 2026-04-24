package be.bstorm.introjaxrs.models.order;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.UUID;

public record OrderLineRequest(

        @NotNull
        UUID productId,

        @Positive
        int quantity

) {
}
