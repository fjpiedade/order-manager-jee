package phi.fjpiedade.api8demo;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import phi.fjpiedade.api8demo.dominio.item.ItemModel;
import phi.fjpiedade.api8demo.dominio.order.OrderModel;
import phi.fjpiedade.api8demo.dominio.user.UserModel;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;

@Path("/order")
public class OrderResource {

    @PersistenceContext(unitName = "default")
    private EntityManager em;

    // Get all
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<OrderModel> getOrders() {
        return em.createQuery("SELECT o FROM OrderModel o", OrderModel.class).getResultList();
    }

    // Create new
    @POST
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createOrder(OrderModel order) {

        ItemModel item = em.find(ItemModel.class, order.getItem().getId());

        if (item == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Collections.singletonMap("message", "Item does not yet exist"))
                    .build();
        }

        UserModel user = em.find(UserModel.class, order.getUser().getId());

        if (user == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Collections.singletonMap("message", "User does not yet exist"))
                    .build();
        }

        order.setCreatedAt(LocalDateTime.now(ZoneId.of("UTC")));
        order.setUpdatedAt(LocalDateTime.now(ZoneId.of("UTC")));
        em.persist(order);
        em.flush();
        return Response.status(Response.Status.CREATED).entity(order).build();
    }

    // Get by ID
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOrderById(@PathParam("id") Long id) {
        OrderModel order = em.find(OrderModel.class, id);
        if (order == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Collections.singletonMap("message", "Order with id " + id + " not found"))
                    .build();
        }
        return Response.ok(order).build();
    }

    // Update by ID
    @PUT
    @Path("/{id}")
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateOrder(@PathParam("id") Long id, OrderModel updatedOrder) {
        OrderModel order = em.find(OrderModel.class, id);
        if (order == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Collections.singletonMap("message", "Order with id " + id + " not found"))
                    .build();
        }

        order.setQuantity(updatedOrder.getQuantity());
        order.setFulfilledQuantity(updatedOrder.getFulfilledQuantity());
        order.setUpdatedAt(LocalDateTime.now(ZoneId.of("UTC")));

        em.merge(order);
        em.flush();
        return Response.ok().entity(order).build();
    }

    // Delete by ID
    @DELETE
    @Path("/{id}")
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteOrder(@PathParam("id") Long id) {
        OrderModel order = em.find(OrderModel.class, id);
        if (order == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Collections.singletonMap("message", "Order with id " + id + " not found"))
                    .build();
        }

        em.remove(order);
        return Response.ok(Collections.singletonMap("message", "Order deleted successfully")).build();
    }
}
