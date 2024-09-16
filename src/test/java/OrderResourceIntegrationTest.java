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
        String orderJson = "{ \"quantity\": 10, \"fulfilledQuantity\": 0, \"item\": { \"id\": 1 }, \"user\": { \"id\": 1 } }";

        Response response = RestAssured.given()
                .contentType("application/json")
                .body(orderJson)
                .post("/order");

        response.then()
                .statusCode(201)
                .body("quantity", equalTo(10))
                .body("fulfilledQuantity", equalTo(0));
    }

    @Test
    public void testGetOrderById() {
        RestAssured.given()
                .when()
                .get("/order/1")
                .then()
                .statusCode(200);
    }

    @Test
    public void testUpdateOrder() {
        String orderJson = "{ \"quantity\": 20, \"fulfilledQuantity\": 20 }";

        RestAssured.given()
                .contentType("application/json")
                .body(orderJson)
                .put("/order/1")
                .then()
                .statusCode(200);
    }

    @Test
    public void testDeleteOrder() {
        RestAssured.given()
                .delete("/order/1")
                .then()
                .statusCode(200);
    }

    @Test
    public void testGetOrdersCompleted() {
        RestAssured.given()
                .when()
                .get("/order/completed")
                .then()
                .statusCode(200);
    }
}
