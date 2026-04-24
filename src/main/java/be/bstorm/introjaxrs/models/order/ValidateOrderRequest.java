package be.bstorm.introjaxrs.models.order;

import jakarta.validation.Valid;

public record ValidateOrderRequest(
        @Valid
        OrderRequest complete,
        @Valid
        OrderRequest incomplete
) {
}
