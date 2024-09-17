package phi.fjpiedade.apiorder.resource;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import phi.fjpiedade.apiorder.domain.GeneralResponse;
import phi.fjpiedade.apiorder.domain.order.OrderModel;
import phi.fjpiedade.apiorder.domain.stock.StockModel;
import phi.fjpiedade.apiorder.service.StockOrderService;
import phi.fjpiedade.apiorder.service.StockService;

import java.util.List;

@Path("/stock")
@RequestScoped
public class StockResource {
    @Inject
    private StockService stockService;

    @Inject
    private StockOrderService stockOrderService;

    public StockResource() {
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStocks() {
        //return stockService.getAllStocks();
        List<StockModel> listOfStock = stockService.getAllStocks();
        if (listOfStock.isEmpty()) {
            GeneralResponse successResponse = new GeneralResponse(
                    Response.Status.OK.getStatusCode(),
                    Response.Status.OK.toString(),
                    "List of Stock is Empty!",
                    null);
            return Response.status(Response.Status.OK).entity(successResponse).build();
        }
        GeneralResponse successResponse = new GeneralResponse(
                Response.Status.OK.getStatusCode(),
                Response.Status.OK.toString(),
                "Stock(s) retrieves successfully",
                listOfStock);
        return Response.status(Response.Status.OK).entity(successResponse).build();
    }

    @POST
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createStock(StockModel stock) {
        StockModel createdStock = stockService.createStock(stock);
//        return Response.status(Response.Status.CREATED).entity(createdStock).build();

        if (createdStock == null) {
            GeneralResponse errorResponse = new GeneralResponse(
                    Response.Status.BAD_REQUEST.getStatusCode(),
                    Response.Status.BAD_REQUEST.toString(),
                    "Error creating Stock!",
                    null
            );
            return Response.status(Response.Status.BAD_REQUEST).entity(errorResponse).build();
        }

        GeneralResponse successResponse = new GeneralResponse(
                Response.Status.CREATED.getStatusCode(),
                Response.Status.CREATED.toString(),
                "Stock created successfully",
                createdStock
        );
        return Response.status(Response.Status.CREATED).entity(successResponse).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStockById(@PathParam("id") Long id) {
        StockModel stock = stockService.getStockById(id);
//        if (stock == null) {
//            return Response.status(Response.Status.BAD_REQUEST)
//                    .entity(Collections.singletonMap("message", "Stock not found"))
//                    .build();
//        }
//        return Response.ok(stock).build();

        if (stock == null) {
            GeneralResponse errorResponse = new GeneralResponse(
                    Response.Status.BAD_REQUEST.getStatusCode(),
                    Response.Status.BAD_REQUEST.toString(),
                    "Stock not found",
                    null);
            return Response.status(Response.Status.BAD_REQUEST).entity(errorResponse).build();
        }

        GeneralResponse successResponse = new GeneralResponse(
                Response.Status.OK.getStatusCode(),
                Response.Status.OK.toString(),
                "Stock retrieve successfully",
                stock
        );
        return Response.status(Response.Status.OK).entity(successResponse).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateStock(@PathParam("id") Long id, StockModel updatedStock) {
        StockModel stockUpdated = stockService.updateStock(id, updatedStock);
//        if (stock == null) {
//            return Response.status(Response.Status.BAD_REQUEST)
//                    .entity(Collections.singletonMap("message", "Stock not found"))
//                    .build();
//        }
//
//        // Check if any orders can be satisfied after adding stock
//        List<OrderModel> pendingOrders = stockOrderService.getPendingOrdersForItem(stock.getItem().getId());
//        for (OrderModel order : pendingOrders) {
//            stockOrderService.trySatisfyOrder(order);
//        }
//
//        return Response.ok(stock).build();

        if (stockUpdated == null) {
            GeneralResponse errorResponse = new GeneralResponse(
                    Response.Status.BAD_REQUEST.getStatusCode(),
                    Response.Status.BAD_REQUEST.toString(),
                    "Stock not found!",
                    null
            );
            return Response.status(Response.Status.BAD_REQUEST).entity(errorResponse).build();
        }

        List<OrderModel> pendingOrders = stockOrderService.getPendingOrdersForItem(stockUpdated.getItem().getId());
        for (OrderModel order : pendingOrders) {
            stockOrderService.trySatisfyOrder(order);
        }
        GeneralResponse successResponse = new GeneralResponse(
                Response.Status.OK.getStatusCode(),
                Response.Status.OK.toString(),
                "Stock updated successfully and Try Satisfy Padding Order",
                stockUpdated
        );
        return Response.status(Response.Status.OK).entity(successResponse).build();
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteStock(@PathParam("id") Long id) {
//        try {
//            stockService.deleteStock(id);
//            return Response.ok(Collections.singletonMap("message", "Stock deleted successfully")).build();
//        } catch (IllegalArgumentException e) {
//            return Response.status(Response.Status.BAD_REQUEST)
//                    .entity(Collections.singletonMap("message", e.getMessage()))
//                    .build();
//        }

        boolean deleted = stockService.deleteStock(id);
        if (!deleted) {
            GeneralResponse errorResponse = new GeneralResponse(
                    Response.Status.BAD_REQUEST.getStatusCode(),
                    Response.Status.BAD_REQUEST.toString(),
                    "Stock not found!",
                    null
            );
            return Response.status(Response.Status.BAD_REQUEST).entity(errorResponse).build();
        }

        GeneralResponse successResponse = new GeneralResponse(
                Response.Status.OK.getStatusCode(),
                Response.Status.OK.toString(),
                "Stock deleted successfully",
                null
        );
        return Response.status(Response.Status.OK).entity(successResponse).build();
    }
}

