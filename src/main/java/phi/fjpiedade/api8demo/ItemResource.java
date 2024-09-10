package phi.fjpiedade.api8demo;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import phi.fjpiedade.api8demo.domain.item.ItemModel;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;

@Path("/item")
public class ItemResource {

    @PersistenceContext(unitName = "default")
    private EntityManager em;

    // Get all
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<ItemModel> getItems() {
        return em.createQuery("SELECT o FROM ItemModel o", ItemModel.class).getResultList();
    }

    // Create new
    @POST
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createItem(ItemModel item) {
        item.setCreatedAt(LocalDateTime.now(ZoneId.of("UTC")));
        item.setUpdatedAt(LocalDateTime.now(ZoneId.of("UTC")));
        em.persist(item);
        em.flush();
        return Response.status(Response.Status.CREATED).entity(item).build();
    }

    // Get by ID
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getItemById(@PathParam("id") Long id) {
        ItemModel item = em.find(ItemModel.class, id);
        if (item == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Collections.singletonMap("message", "Item not found"))
                    .build();
        }
        return Response.ok(item).build();
    }

    // Update by ID
    @PUT
    @Path("/{id}")
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateItem(@PathParam("id") Long id, ItemModel updatedItem) {
        ItemModel item = em.find(ItemModel.class, id);
        if (item == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Collections.singletonMap("message", "Item not found"))
                    .build();
        }

        item.setName(updatedItem.getName());
        item.setUpdatedAt(LocalDateTime.now(ZoneId.of("UTC")));

        em.merge(item);
        em.flush();
        return Response.ok().entity(item).build();
    }

    // Delete by ID
    @DELETE
    @Path("/{id}")
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteItem(@PathParam("id") Long id) {
        ItemModel item = em.find(ItemModel.class, id);
        if (item == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Collections.singletonMap("message", "Item not found"))
                    .build();
        }

        em.remove(item);
        return Response.ok(Collections.singletonMap("message", "Item deleted successfully")).build();
    }
}
