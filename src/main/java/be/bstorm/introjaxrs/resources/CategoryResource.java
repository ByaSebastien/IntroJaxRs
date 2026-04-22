package be.bstorm.introjaxrs.resources;

import be.bstorm.introjaxrs.daos.CategoryDao;
import be.bstorm.introjaxrs.pojos.Category;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/category")
public class CategoryResource {

    @Inject
    private CategoryDao categoryDao;

    @GET
    @Produces("application/json")
    public Response findAll() {
        List<Category> categories = categoryDao.findAll();

        return Response.ok()
                .entity(categories)
                .build();
    }

    @GET
    @Path("/{id}")
    @Produces("application/json")
    public Response findById(@PathParam("id") Integer id){

        Category category = categoryDao.findById(id).orElseThrow();

        return Response.ok()
                .entity(category)
                .build();
    }

    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public Response save(Category category) {
        categoryDao.save(category);
        return Response
                .noContent()
                .build();
    }

    @PUT
    @Path("/{id}")
    @Consumes("application/json")
    @Produces("application/json")
    public Response update(
            @PathParam("id") Integer id,
            Category category
    ) {

        categoryDao.update(category);
        return Response.noContent()
                .build();
    }

    @DELETE
    @Path("/{id}")
    @Produces("application/json")
    public Response delete(@PathParam("id") Integer id) {

        categoryDao.remove(id);
        return Response
                .noContent()
                .build();
    }
}
