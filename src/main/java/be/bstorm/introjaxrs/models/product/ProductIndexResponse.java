package be.bstorm.introjaxrs.models.product;

import be.bstorm.introjaxrs.pojos.Product;

import java.util.UUID;

public record ProductIndexResponse(
        UUID id,
        String name,
        String brand,
        int price,
        String image,
        String category
) {

    public static ProductIndexResponse fromProduct(Product p) {
        return new ProductIndexResponse(
                p.getId(),
                p.getName(),
                p.getBrand(),
                p.getPrice(),
                p.getImage(),
                p.getCategory().getName()
        );
    }
}
