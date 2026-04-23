package be.bstorm.introjaxrs.resources;

import be.bstorm.introjaxrs.daos.ProductDao;
import be.bstorm.introjaxrs.models.product.ProductDetailsResponse;
import be.bstorm.introjaxrs.models.product.ProductIndexResponse;
import be.bstorm.introjaxrs.pojos.Product;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.UUID;

@Path("/product")
public class ProductResources {

    @Inject
    private ProductDao productDao;

    @GET
    @Produces("application/json")
    public Response get() {
        List<ProductIndexResponse> products = productDao
                .findAllWithCategory().stream()
                .map(ProductIndexResponse::fromProduct)
                .toList();
        return Response.ok()
                .entity(products)
                .build();
    }

    @GET
    @Path("/{id}")
    @Produces("application/json")
    public Response getById(@PathParam("id") UUID id) {
        return productDao.findByIdWithCategory(id)
                .map(p -> Response.ok()
                        .entity(ProductDetailsResponse.fromProduct(p))
                        .build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }
}
