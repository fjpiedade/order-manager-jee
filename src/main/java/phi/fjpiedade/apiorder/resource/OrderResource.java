package phi.fjpiedade.apiorder.resource;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import phi.fjpiedade.apiorder.domain.GeneralResponse;
import phi.fjpiedade.apiorder.domain.order.OrderModel;
import phi.fjpiedade.apiorder.service.OrderService;
import phi.fjpiedade.apiorder.service.StockOrderService;

import java.util.Collections;
import java.util.List;

@Path("/order")
@RequestScoped
public class OrderResource {
    @Inject
    private OrderService orderService;
    @Inject
    private StockOrderService stockOrderService;

    public OrderResource() {
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOrders() {
//        return orderService.getAllOrders();
        List<OrderModel> listOfOrders = orderService.getAllOrders();
        if (listOfOrders.isEmpty()) {
            GeneralResponse successResponse = new GeneralResponse(
                    Response.Status.OK.getStatusCode(),
                    Response.Status.OK.toString(),
                    "List of Order is Empty!",
                    null);
            return Response.status(Response.Status.OK).entity(successResponse).build();
        }
        GeneralResponse successResponse = new GeneralResponse(
                Response.Status.OK.getStatusCode(),
                Response.Status.OK.toString(),
                "Order(s) retrieves successfully",
                listOfOrders);
        return Response.status(Response.Status.OK).entity(successResponse).build();
    }

    @POST
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createOrder(OrderModel order) {
        OrderModel createdOrder = orderService.createOrder(order);
//        try {
//            OrderModel createdOrder = orderService.createOrder(order);
//            stockOrderService.trySatisfyOrder(createdOrder);
//            return Response.status(Response.Status.CREATED).entity(createdOrder).build();
//        } catch (IllegalArgumentException e) {
//            return Response.status(Response.Status.BAD_REQUEST)
//                    .entity(Collections.singletonMap("message", e.getMessage()))
//                    .build();
//        }

        if (createdOrder == null) {
            GeneralResponse errorResponse = new GeneralResponse(
                    Response.Status.BAD_REQUEST.getStatusCode(),
                    Response.Status.BAD_REQUEST.toString(),
                    "Error creating Order!",
                    null
            );
            return Response.status(Response.Status.BAD_REQUEST).entity(errorResponse).build();
        }

        stockOrderService.trySatisfyOrder(createdOrder);
        GeneralResponse successResponse = new GeneralResponse(
                Response.Status.CREATED.getStatusCode(),
                Response.Status.CREATED.toString(),
                "Order created successfully and Try Satisfy",
                createdOrder
        );
        return Response.status(Response.Status.CREATED).entity(successResponse).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOrderById(@PathParam("id") Long id) {
        OrderModel order = orderService.getOrderById(id);
//        if (order == null) {
//            return Response.status(Response.Status.NOT_FOUND)
//                    .entity(Collections.singletonMap("message", "Order not found"))
//                    .build();
//        }
//        return Response.ok(order).build();
        if (order == null) {
            GeneralResponse errorResponse = new GeneralResponse(
                    Response.Status.BAD_REQUEST.getStatusCode(),
                    Response.Status.BAD_REQUEST.toString(),
                    "Order not found",
                    null);
            return Response.status(Response.Status.BAD_REQUEST).entity(errorResponse).build();
        }

        GeneralResponse successResponse = new GeneralResponse(
                Response.Status.OK.getStatusCode(),
                Response.Status.OK.toString(),
                "Order retrieve successfully",
                order
        );
        return Response.status(Response.Status.OK).entity(successResponse).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateOrder(@PathParam("id") Long id, OrderModel updatedOrder) {
        OrderModel orderUpdated = orderService.updateOrder(id, updatedOrder);
//        try {
//            OrderModel order = orderService.updateOrder(id, updatedOrder);
//            return Response.ok(order).build();
//        } catch (IllegalArgumentException e) {
//            return Response.status(Response.Status.BAD_REQUEST)
//                    .entity(Collections.singletonMap("message", e.getMessage()))
//                    .build();
//        }
        if (orderUpdated == null) {
            GeneralResponse errorResponse = new GeneralResponse(
                    Response.Status.BAD_REQUEST.getStatusCode(),
                    Response.Status.BAD_REQUEST.toString(),
                    "Order not found!",
                    null
            );
            return Response.status(Response.Status.BAD_REQUEST).entity(errorResponse).build();
        }

        GeneralResponse successResponse = new GeneralResponse(
                Response.Status.OK.getStatusCode(),
                Response.Status.OK.toString(),
                "Order updated successfully",
                orderUpdated
        );
        return Response.status(Response.Status.OK).entity(successResponse).build();
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteOrder(@PathParam("id") Long id) {
//        try {
//            orderService.deleteOrder(id);
//            return Response.ok(Collections.singletonMap("message", "Order deleted successfully")).build();
//        } catch (IllegalArgumentException e) {
//            return Response.status(Response.Status.BAD_REQUEST)
//                    .entity(Collections.singletonMap("message", e.getMessage()))
//                    .build();
//        }
        boolean deleted = orderService.deleteOrder(id);
        if (!deleted) {
            GeneralResponse errorResponse = new GeneralResponse(
                    Response.Status.BAD_REQUEST.getStatusCode(),
                    Response.Status.BAD_REQUEST.toString(),
                    "Order not found!",
                    null
            );
            return Response.status(Response.Status.BAD_REQUEST).entity(errorResponse).build();
        }

        GeneralResponse successResponse = new GeneralResponse(
                Response.Status.OK.getStatusCode(),
                Response.Status.OK.toString(),
                "Order deleted successfully",
                null
        );
        return Response.status(Response.Status.OK).entity(successResponse).build();
    }

    @GET()
    @Path("/completed")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOrdersCompleted() {
        //return orderService.getAllCompletedOrders();
        List<OrderModel> listOfOrdersCompleted = orderService.getAllCompletedOrders();
        if (listOfOrdersCompleted.isEmpty()) {
            GeneralResponse successResponse = new GeneralResponse(
                    Response.Status.OK.getStatusCode(),
                    Response.Status.OK.toString(),
                    "List of Order Completed is Empty!",
                    null);
            return Response.status(Response.Status.OK).entity(successResponse).build();
        }
        GeneralResponse successResponse = new GeneralResponse(
                Response.Status.OK.getStatusCode(),
                Response.Status.OK.toString(),
                "Order(s) Completed retrieves successfully",
                listOfOrdersCompleted);
        return Response.status(Response.Status.OK).entity(successResponse).build();
    }

}
