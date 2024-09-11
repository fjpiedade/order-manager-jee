package phi.fjpiedade.api8demo.resource;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import phi.fjpiedade.api8demo.domain.item.ItemModel;
import phi.fjpiedade.api8demo.service.ItemService;

import java.util.Collections;
import java.util.List;

@Path("/item")
public class ItemResource {
    private ItemService itemService;

    public ItemResource() {
    }

    @Inject
    public ItemResource(ItemService itemService) {
        this.itemService = itemService;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<ItemModel> getItems() {
        return itemService.getAllItems();
    }

    @POST
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createItem(ItemModel item) {
        ItemModel createdItem = itemService.createItem(item);
        return Response.status(Response.Status.CREATED).entity(createdItem).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getItemById(@PathParam("id") Long id) {
        ItemModel item = itemService.getItemById(id);
        if (item == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Collections.singletonMap("message", "Item not found"))
                    .build();
        }
        return Response.ok(item).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateItem(@PathParam("id") Long id, ItemModel updatedItem) {
        ItemModel item = itemService.updateItem(id, updatedItem);
        if (item == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Collections.singletonMap("message", "Item not found"))
                    .build();
        }
        return Response.ok(item).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteItem(@PathParam("id") Long id) {
        boolean deleted = itemService.deleteItem(id);
        if (!deleted) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Collections.singletonMap("message", "Item not found"))
                    .build();
        }
        return Response.ok(Collections.singletonMap("message", "Item deleted successfully")).build();
    }
}
