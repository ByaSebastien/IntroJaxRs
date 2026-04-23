package be.bstorm.introjaxrs.models.product;

import be.bstorm.introjaxrs.pojos.Product;

import java.util.UUID;

public record ProductDetailsResponse(
        UUID id,
        String name,
        String brand,
        int price,
        String description,
        String image,
        String category
) {

    public static ProductDetailsResponse fromProduct(Product p) {
        return new ProductDetailsResponse(
                p.getId(),
                p.getName(),
                p.getBrand(),
                p.getPrice(),
                p.getDescription(),
                p.getImage(),
                p.getCategory().getName()
        );
    }
}
