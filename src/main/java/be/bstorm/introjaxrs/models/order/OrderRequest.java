package be.bstorm.introjaxrs.models.order;

import java.util.List;

public record OrderRequest(
        List<OrderLineRequest> orderLines
) {
}
