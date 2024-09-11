package phi.fjpiedade.api8demo.resource;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import phi.fjpiedade.api8demo.domain.order.OrderModel;
import phi.fjpiedade.api8demo.service.OrderService;

import java.util.Collections;
import java.util.List;

@Path("/order")
public class OrderResource {
    private OrderService orderService;

    public OrderResource() {
    }

    @Inject
    public OrderResource(OrderService orderService) {
        this.orderService = orderService;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<OrderModel> getOrders() {
        return orderService.getAllOrders();
    }

    @POST
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createOrder(OrderModel order) {
        try {
            OrderModel createdOrder = orderService.createOrder(order);
            return Response.status(Response.Status.CREATED).entity(createdOrder).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Collections.singletonMap("message", e.getMessage()))
                    .build();
        }
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOrderById(@PathParam("id") Long id) {
        OrderModel order = orderService.getOrderById(id);
        if (order == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Collections.singletonMap("message", "Order not found"))
                    .build();
        }
        return Response.ok(order).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateOrder(@PathParam("id") Long id, OrderModel updatedOrder) {
        try {
            OrderModel order = orderService.updateOrder(id, updatedOrder);
            return Response.ok(order).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Collections.singletonMap("message", e.getMessage()))
                    .build();
        }
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteOrder(@PathParam("id") Long id) {
        try {
            orderService.deleteOrder(id);
            return Response.ok(Collections.singletonMap("message", "Order deleted successfully")).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Collections.singletonMap("message", e.getMessage()))
                    .build();
        }
    }
}
