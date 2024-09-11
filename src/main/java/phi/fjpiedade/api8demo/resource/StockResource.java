package phi.fjpiedade.api8demo.resource;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import phi.fjpiedade.api8demo.domain.order.OrderModel;
import phi.fjpiedade.api8demo.domain.stock.StockModel;
import phi.fjpiedade.api8demo.service.StockOrderService;
import phi.fjpiedade.api8demo.service.StockService;

import java.util.Collections;
import java.util.List;

@Path("/stock")
public class StockResource {
    @Inject
    private StockService stockService;

    @Inject
    private StockOrderService stockOrderService;

    public StockResource() {
    }

//    @Inject
//    public StockResource(StockService stockService) {
//        this.stockService = stockService;
//    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<StockModel> getStocks() {
        return stockService.getAllStocks();
    }

    @POST
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createStock(StockModel stock) {
        StockModel createdStock = stockService.createStock(stock);
        return Response.status(Response.Status.CREATED).entity(createdStock).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStockById(@PathParam("id") Long id) {
        StockModel stock = stockService.getStockById(id);
        if (stock == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Collections.singletonMap("message", "Stock not found"))
                    .build();
        }
        return Response.ok(stock).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateStock(@PathParam("id") Long id, StockModel updatedStock) {
        StockModel stock = stockService.updateStock(id, updatedStock);
        if (stock == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Collections.singletonMap("message", "Stock not found"))
                    .build();
        }

        // Check if any orders can be satisfied after adding stock
        List<OrderModel> pendingOrders = stockOrderService.getPendingOrdersForItem(stock.getItem().getId());
        for (OrderModel order : pendingOrders) {
            stockOrderService.trySatisfyOrder(order);
        }

        return Response.ok(stock).build();
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteStock(@PathParam("id") Long id) {
        try {
            stockService.deleteStock(id);
            return Response.ok(Collections.singletonMap("message", "Stock deleted successfully")).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Collections.singletonMap("message", e.getMessage()))
                    .build();
        }
    }
}
