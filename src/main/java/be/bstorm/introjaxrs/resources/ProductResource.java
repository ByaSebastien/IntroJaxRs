package be.bstorm.introjaxrs.resources;

import be.bstorm.introjaxrs.daos.ProductDao;
import be.bstorm.introjaxrs.models.product.ProductDetailsResponse;
import be.bstorm.introjaxrs.models.product.ProductIndexResponse;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.UUID;

@Path("/product")
public class ProductResource {

    @Inject
    private ProductDao productDao;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
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
    @Produces(MediaType.APPLICATION_JSON)
    public Response getById(@PathParam("id") UUID id) {
        return productDao.findByIdWithCategoryAndStock(id)
                .map(p -> Response.ok()
                        .entity(ProductDetailsResponse.fromProduct(p))
                        .build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }
}
