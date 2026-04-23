package be.bstorm.introjaxrs.models.order;

import java.util.UUID;

public record OrderLineRequest(
        int quantity,
        UUID productId
) {
}
