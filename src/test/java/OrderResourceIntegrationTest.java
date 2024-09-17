import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.hamcrest.Matchers.equalTo;

public class OrderResourceIntegrationTest {

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "http://localhost:8881/apiorder/api/v1";
    }

    @Test
    public void testCreateOrder() {
        // First, create an item and user to associate with the order
        String itemJson = "{ \"name\": \"digital\" }";
        Response itemResponse = RestAssured.given()
                .contentType("application/json")
                .body(itemJson)
                .post("/item");
        int itemId = itemResponse.jsonPath().getInt("data.id");

        String userJson = "{ \"email\": \"user@example.com\", \"name\": \"John Doe\" }";
        Response userResponse = RestAssured.given()
                .contentType("application/json")
                .body(userJson)
                .post("/user");
        int userId = userResponse.jsonPath().getInt("data.id");

        // Create order with the created item and user
        String orderJson = "{ \"quantity\": 10, \"fulfilledQuantity\": 0, \"item\": { \"id\": " + itemId + " }, \"user\": { \"id\": " + userId + " } }";

        Response response = RestAssured.given()
                .contentType("application/json")
                .body(orderJson)
                .post("/order");

        response.then()
                .statusCode(201)
                .body("statusCode", equalTo(201))
                .body("status", equalTo("Created"))
                .body("message", equalTo("Order created successfully"))
                .body("data.quantity", equalTo(10))
                .body("data.fulfilledQuantity", equalTo(0));
    }

    @Test
    public void testGetOrderById() {
        RestAssured.given()
                .when()
                .get("/order/1")
                .then()
                .statusCode(200)
                .body("statusCode", equalTo(200))
                .body("status", equalTo("OK"));
    }

    @Test
    public void testUpdateOrder() {
        // First, create an item and user to associate with the order
        String itemJson = "{ \"name\": \"digital\" }";
        Response itemResponse = RestAssured.given()
                .contentType("application/json")
                .body(itemJson)
                .post("/item");
        int itemId = itemResponse.jsonPath().getInt("data.id");

        String userJson = "{ \"email\": \"user@example.com\", \"name\": \"John Doe\" }";
        Response userResponse = RestAssured.given()
                .contentType("application/json")
                .body(userJson)
                .post("/user");
        int userId = userResponse.jsonPath().getInt("data.id");

        // Create order to update
        String orderJson = "{ \"quantity\": 10, \"fulfilledQuantity\": 0, \"item\": { \"id\": " + itemId + " }, \"user\": { \"id\": " + userId + " } }";
        Response orderResponse = RestAssured.given()
                .contentType("application/json")
                .body(orderJson)
                .post("/order");
        int orderId = orderResponse.jsonPath().getInt("data.id");

        // Update the created order
        String updatedOrderJson = "{ \"quantity\": 20, \"fulfilledQuantity\": 20 }";

        RestAssured.given()
                .contentType("application/json")
                .body(updatedOrderJson)
                .put("/order/" + orderId)
                .then()
                .statusCode(200)
                .body("statusCode", equalTo(200))
                .body("status", equalTo("OK"));
    }

    @Test
    public void testGetAllOrders() {
        RestAssured.given()
                .when()
                .get("/order")
                .then()
                .statusCode(200)
                .body("statusCode", equalTo(200))
                .body("status", equalTo("OK"));
    }

    @Test
    public void testGetCompletedOrders() {
        RestAssured.given()
                .when()
                .get("/order/completed")
                .then()
                .statusCode(200)
                .body("statusCode", equalTo(200))
                .body("status", equalTo("OK"));
    }

    @Test
    public void testDeleteOrder() {
        // First, create an item and user to associate with the order
        String itemJson = "{ \"name\": \"digital\" }";
        Response itemResponse = RestAssured.given()
                .contentType("application/json")
                .body(itemJson)
                .post("/item");
        int itemId = itemResponse.jsonPath().getInt("data.id");

        String userJson = "{ \"email\": \"user@example.com\", \"name\": \"John Doe\" }";
        Response userResponse = RestAssured.given()
                .contentType("application/json")
                .body(userJson)
                .post("/user");
        int userId = userResponse.jsonPath().getInt("data.id");

        // Create order to delete
        String orderJson = "{ \"quantity\": 10, \"fulfilledQuantity\": 0, \"item\": { \"id\": " + itemId + " }, \"user\": { \"id\": " + userId + " } }";
        Response orderResponse = RestAssured.given()
                .contentType("application/json")
                .body(orderJson)
                .post("/order");
        int orderId = orderResponse.jsonPath().getInt("data.id");

        RestAssured.given()
                .delete("/order/" + orderId)
                .then()
                .statusCode(200)
                .body("statusCode", equalTo(200))
                .body("status", equalTo("OK"));
    }
}
