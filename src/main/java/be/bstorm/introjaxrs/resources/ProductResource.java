package be.bstorm.introjaxrs.resources;

import be.bstorm.introjaxrs.daos.ProductDao;
import be.bstorm.introjaxrs.pojos.Product;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.UUID;

@Path("/product")
@Tag(name = "Product", description = "Operations sur les produits")
public class ProductResource {

    @Inject
    private ProductDao productDao;

    @GET
    @Produces("application/json")
    @Operation(summary = "Recuperer tous les produits")
    @ApiResponse(responseCode = "200", description = "Liste des produits")
    public Response getAll(){

        List<Product> products = productDao.findAll();

        return Response.ok(products).build();
    }

    @GET()
    @Path("/{id}")
    @Produces("application/json")
    @Operation(summary = "Recuperer un produit par son id")
    @ApiResponse(responseCode = "200", description = "Produit trouve")
    @ApiResponse(responseCode = "404", description = "Produit introuvable")
    public Response getById(@PathParam("id") UUID id){

        return productDao.findById(id)
                .map(Response::ok)
                .orElse(Response.status(Response.Status.NOT_FOUND))
                .build();
    }

    @POST
    @Consumes("application/json")
    @Produces("application/json")
    @Operation(summary = "Creer un produit")
    @ApiResponse(responseCode = "201", description = "Produit cree")
    public Response create(Product product){
        Product created = productDao.save(product);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @PUT
    @Path("/{id}")
    @Consumes("application/json")
    @Produces("application/json")
    @Operation(summary = "Mettre a jour un produit")
    @ApiResponse(responseCode = "200", description = "Produit mis a jour")
    @ApiResponse(responseCode = "404", description = "Produit introuvable")
    public Response update(@PathParam("id") UUID id, Product product){
        Product existing = productDao.findById(id).orElseThrow();
        existing.setName(product.getName());
        existing.setPrice(product.getPrice());
        existing.setCategory(product.getCategory());
        Product updated = productDao.update(existing);
        return Response.ok(updated).build();
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Supprimer un produit")
    @ApiResponse(responseCode = "204", description = "Produit supprime")
    @ApiResponse(responseCode = "404", description = "Produit introuvable")
    public Response delete(@PathParam("id") UUID id){
        productDao.delete(id);
        return Response.noContent().build();
    }
}
