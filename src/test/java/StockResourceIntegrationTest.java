import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.equalTo;

public class StockResourceIntegrationTest {

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "http://localhost:8881/apiorder/api/v1";
    }

    @Test
    public void testCreateStock() {
        // First, create an item to associate with the stock
        String itemJson = "{ \"name\": \"digital\" }";
        Response itemResponse = RestAssured.given()
                .contentType("application/json")
                .body(itemJson)
                .post("/item");
        int itemId = itemResponse.jsonPath().getInt("data.id");

        // Now, create stock for the created item
        String stockJson = "{ \"item\": { \"id\": " + itemId + " }, \"quantity\": 100 }";

        Response response = RestAssured.given()
                .contentType("application/json")
                .body(stockJson)
                .post("/stock");

        response.then()
                .statusCode(201)
                .body("statusCode", equalTo(201))
                .body("status", equalTo("Created"))
                .body("message", equalTo("Stock created successfully"))
                .body("data.quantity", equalTo(100));
    }

    @Test
    public void testGetStockById() {
        RestAssured.given()
                .when()
                .get("/stock/1")
                .then()
                .statusCode(200)
                .body("statusCode", equalTo(200))
                .body("status", equalTo("OK"));
    }

    @Test
    public void testUpdateStock() {
        // First, create an item to associate with the stock
        String itemJson = "{ \"name\": \"digital\" }";
        Response itemResponse = RestAssured.given()
                .contentType("application/json")
                .body(itemJson)
                .post("/item");
        int itemId = itemResponse.jsonPath().getInt("data.id");

        // Create stock to update
        String stockJson = "{ \"item\": { \"id\": " + itemId + " }, \"quantity\": 100 }";
        Response stockResponse = RestAssured.given()
                .contentType("application/json")
                .body(stockJson)
                .post("/stock");
        int stockId = stockResponse.jsonPath().getInt("data.id");

        // Update the created stock
        String updatedStockJson = "{ \"quantity\": 150 }";

        RestAssured.given()
                .contentType("application/json")
                .body(updatedStockJson)
                .put("/stock/" + stockId)
                .then()
                .statusCode(200)
                .body("statusCode", equalTo(200))
                .body("status", equalTo("OK"));
    }

    @Test
    public void testGetAllStocks() {
        RestAssured.given()
                .when()
                .get("/stock")
                .then()
                .statusCode(200)
                .body("statusCode", equalTo(200))
                .body("status", equalTo("OK"));
    }


    @Test
    public void testDeleteStock() {
        // First, create an item to associate with the stock
        String itemJson = "{ \"name\": \"digital\" }";
        Response itemResponse = RestAssured.given()
                .contentType("application/json")
                .body(itemJson)
                .post("/item");
        int itemId = itemResponse.jsonPath().getInt("data.id");

        // Create stock to delete
        String stockJson = "{ \"item\": { \"id\": " + itemId + " }, \"quantity\": 100 }";
        Response stockResponse = RestAssured.given()
                .contentType("application/json")
                .body(stockJson)
                .post("/stock");
        int stockId = stockResponse.jsonPath().getInt("data.id");

        RestAssured.given()
                .delete("/stock/" + stockId)
                .then()
                .statusCode(200)
                .body("statusCode", equalTo(200))
                .body("status", equalTo("OK"));
    }
}
