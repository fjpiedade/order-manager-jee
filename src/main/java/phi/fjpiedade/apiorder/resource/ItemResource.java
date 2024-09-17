package phi.fjpiedade.apiorder.resource;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Collections;
import java.util.List;

import phi.fjpiedade.apiorder.domain.GeneralResponse;
import phi.fjpiedade.apiorder.domain.item.ItemModel;
import phi.fjpiedade.apiorder.service.ItemService;

@Path("/item")
@RequestScoped
public class ItemResource {
    @Inject
    private ItemService itemService;

    public ItemResource() {
    }

//    @GET
//    @Produces(MediaType.APPLICATION_JSON)
//    public List<ItemModel> getItems() {
//        return itemService.getAllItems();
//    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getItems() {
        List<ItemModel> listOfItems = itemService.getAllItems();
        if (listOfItems.isEmpty()) {
            GeneralResponse successResponse = new GeneralResponse(
                    Response.Status.OK.getStatusCode(),
                    Response.Status.OK.toString(),
                    "List of Items is Empty!",
                    null);
            return Response.status(Response.Status.OK).entity(successResponse).build();
        }
        GeneralResponse successResponse = new GeneralResponse(
                Response.Status.OK.getStatusCode(),
                Response.Status.OK.toString(),
                "Item(s) retrieves successfully",
                listOfItems);
        return Response.status(Response.Status.OK).entity(successResponse).build();
    }

    @POST
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createItem(ItemModel item) {
//        ItemModel createdItem = itemService.createItem(item);
//        return Response.status(Response.Status.CREATED).entity(createdItem).build();
        ItemModel createdItem = itemService.createItem(item);
        if (createdItem == null) {
            GeneralResponse errorResponse = new GeneralResponse(
                    Response.Status.BAD_REQUEST.getStatusCode(),
                    Response.Status.BAD_REQUEST.toString(),
                    "Error creating Item!",
                    null
            );
            return Response.status(Response.Status.BAD_REQUEST).entity(errorResponse).build();
        }

        GeneralResponse successResponse = new GeneralResponse(
                Response.Status.CREATED.getStatusCode(),
                Response.Status.CREATED.toString(),
                "Item created successfully",
                createdItem
        );
        return Response.status(Response.Status.CREATED).entity(successResponse).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getItemById(@PathParam("id") Long id) {
        ItemModel item = itemService.getItemById(id);
//        if (item == null) {
//            return Response.status(Response.Status.BAD_REQUEST)
//                    .entity(Collections.singletonMap("message", "Item not found"))
//                    .build();
//        }
//        return Response.ok(item).build();
        if (item == null) {
            GeneralResponse errorResponse = new GeneralResponse(
                    Response.Status.BAD_REQUEST.getStatusCode(),
                    Response.Status.BAD_REQUEST.toString(),
                    "Item not found",
                    null);
            return Response.status(Response.Status.BAD_REQUEST).entity(errorResponse).build();
        }

        GeneralResponse successResponse = new GeneralResponse(
                Response.Status.OK.getStatusCode(),
                Response.Status.OK.toString(),
                "Item retrieve successfully",
                item
        );
        return Response.status(Response.Status.OK).entity(successResponse).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateItem(@PathParam("id") Long id, ItemModel updatedItem) {
        ItemModel itemUpdated = itemService.updateItem(id, updatedItem);
//        if (item == null) {
//            return Response.status(Response.Status.BAD_REQUEST)
//                    .entity(Collections.singletonMap("message", "Item not found"))
//                    .build();
//        }
//        return Response.ok(item).build();
        if (itemUpdated == null) {
            GeneralResponse errorResponse = new GeneralResponse(
                    Response.Status.BAD_REQUEST.getStatusCode(),
                    Response.Status.BAD_REQUEST.toString(),
                    "Item not found!",
                    null
            );
            return Response.status(Response.Status.BAD_REQUEST).entity(errorResponse).build();
        }

        GeneralResponse successResponse = new GeneralResponse(
                Response.Status.OK.getStatusCode(),
                Response.Status.OK.toString(),
                "Item updated successfully",
                itemUpdated
        );
        return Response.status(Response.Status.OK).entity(successResponse).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteItem(@PathParam("id") Long id) {
        boolean deleted = itemService.deleteItem(id);
//        if (!deleted) {
//            return Response.status(Response.Status.BAD_REQUEST)
//                    .entity(Collections.singletonMap("message", "Item not found"))
//                    .build();
//        }
//        return Response.ok(Collections.singletonMap("message", "Item deleted successfully")).build();

        if (!deleted) {
            GeneralResponse errorResponse = new GeneralResponse(
                    Response.Status.BAD_REQUEST.getStatusCode(),
                    Response.Status.BAD_REQUEST.toString(),
                    "Item not found!",
                    null
            );
            return Response.status(Response.Status.BAD_REQUEST).entity(errorResponse).build();
        }

        GeneralResponse successResponse = new GeneralResponse(
                Response.Status.OK.getStatusCode(),
                Response.Status.OK.toString(),
                "Item deleted successfully",
                null
        );
        return Response.status(Response.Status.CREATED).entity(successResponse).build();
    }
}
