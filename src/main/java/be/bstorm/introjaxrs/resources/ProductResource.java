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

/**
 * Ressource JAX-RS pour la gestion des produits.
 *
 * Fournit des endpoints pour consulter le catalogue de produits.
 * Endpoints publics (accessibles sans authentification).
 *
 * @author IntroJaxRs
 * @version 1.0
 */
@Path("/product")
public class ProductResource {

    /**
     * DAO pour les opérations sur les produits
     */
    @Inject
    private ProductDao productDao;

    /**
     * Récupère la liste complète des produits avec leurs catégories.
     *
     * @return une réponse JSON contenant la liste des produits
     *
     * @response 200 Liste des produits (ProductIndexResponse)
     */
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

    /**
     * Récupère les détails d'un produit spécifique avec son stock.
     *
     * @param id l'UUID du produit recherché
     * @return une réponse JSON avec les détails du produit
     *
     * @response 200 Détails du produit (ProductDetailsResponse)
     * @response 404 Le produit n'existe pas
     */
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
