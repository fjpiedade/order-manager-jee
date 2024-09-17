import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.equalTo;

public class ItemResourceIntegrationTest {

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "http://localhost:8881/apiorder/api/v1";
    }

    @Test
    public void testCreateItem() {
        String itemJson = "{ \"name\": \"digital\" }";

        Response response = RestAssured.given()
                .contentType("application/json")
                .body(itemJson)
                .post("/item");

        response.then()
                .statusCode(201)
                .body("statusCode", equalTo(201))
                .body("status", equalTo("Created"))
                .body("message", equalTo("Item created successfully"))
                .body("data.name", equalTo("digital"));
    }

    @Test
    public void testGetItemById() {
        RestAssured.given()
                .when()
                .get("/item/1")
                .then()
                .statusCode(200)
                .body("statusCode", equalTo(200))
                .body("status", equalTo("OK"));
    }

    @Test
    public void testUpdateItem() {
        String itemJson = "{ \"name\": \"updated item\" }";

        RestAssured.given()
                .contentType("application/json")
                .body(itemJson)
                .put("/item/1")
                .then()
                .statusCode(200)
                .body("statusCode", equalTo(200))
                .body("status", equalTo("OK"));
    }


    @Test
    public void testGetAllItems() {
        RestAssured.given()
                .when()
                .get("/item")
                .then()
                .statusCode(200)
                .body("statusCode", equalTo(200))
                .body("status", equalTo("OK"));
    }

    @Test
    public void testDeleteItem() {
        RestAssured.given()
                .delete("/item/1")
                .then()
                .statusCode(200)
                .body("statusCode", equalTo(200))
                .body("status", equalTo("OK"));
    }
}