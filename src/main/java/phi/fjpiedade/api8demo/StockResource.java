package phi.fjpiedade.api8demo;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import phi.fjpiedade.api8demo.domain.item.ItemModel;
import phi.fjpiedade.api8demo.domain.stock.StockModel;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;

@Path("/stock")
public class StockResource {

    @PersistenceContext(unitName = "default")
    private EntityManager em;

    // Get all
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<StockModel> getStocks() {
        return em.createQuery("SELECT o FROM StockModel o", StockModel.class).getResultList();
    }

    // Create new
    @POST
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createStock(StockModel stock) {

//        StockModel newStock = em.find(StockModel.class, stock.getItem());
//        if (newStock == null) {
//            return Response.status(Response.Status.BAD_REQUEST)
//                    .entity(Collections.singletonMap("message", "Item already Exist in the stock"))
//                    .build();
//        }


        ItemModel item = em.find(ItemModel.class, stock.getItem().getId());

        if (item == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Collections.singletonMap("message", "Item does not yet exist"))
                    .build();
        }

        StockModel existingStock = em.find(StockModel.class, stock.getItem().getId());
        if (existingStock != null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Collections.singletonMap("message", "Item already exists in the stock"))
                    .build();
        }

        stock.setCreatedAt(LocalDateTime.now(ZoneId.of("UTC")));
        stock.setUpdatedAt(LocalDateTime.now(ZoneId.of("UTC")));
        em.persist(stock);
        em.flush();
        return Response.status(Response.Status.CREATED).entity(stock).build();
    }

    // Get by ID
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStockById(@PathParam("id") Long id) {
        StockModel stock = em.find(StockModel.class, id);
        if (stock == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Collections.singletonMap("message", "Stock with id " + id + " not found"))
                    .build();
        }
        return Response.ok(stock).build();
    }

    // Update by ID
    @PUT
    @Path("/{id}")
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateStock(@PathParam("id") Long id, StockModel updatedStock) {
        StockModel stock = em.find(StockModel.class, id);
        if (stock == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Collections.singletonMap("message", "Stock with id " + id + " not found"))
                    .build();
        }

        stock.setQuantity(updatedStock.getQuantity());
        stock.setUpdatedAt(LocalDateTime.now(ZoneId.of("UTC")));

        em.merge(stock);
        em.flush();
        return Response.ok().entity(stock).build();
    }

    // Delete by ID
    @DELETE
    @Path("/{id}")
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteStock(@PathParam("id") Long id) {
        StockModel stock = em.find(StockModel.class, id);
        if (stock == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Collections.singletonMap("message", "Stock with id " + id + " not found"))
                    .build();
        }

        em.remove(stock);
        return Response.ok(Collections.singletonMap("message", "Stock deleted successfully")).build();
    }
}
