import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;

import static org.hamcrest.Matchers.equalTo;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserResourceIntegrationTest {

    private static Long createdUserId;

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "http://localhost:8881/apiorder/api/v1";
    }

    @BeforeEach
    public void createTestUser() {
        // Create a user before each test
        String userJson = "{ \"email\": \"test@example.com\", \"name\": \"John Doe\" }";

        Response response = RestAssured.given()
                .contentType("application/json")
                .body(userJson)
                .post("/user");

        createdUserId = response.jsonPath().getLong("data.id"); // Capture the created user ID
    }

    @AfterEach
    public void cleanup() {
        // Cleanup: Delete the user created for the test
        if (createdUserId != null) {
            RestAssured.given()
                    .delete("/user/" + createdUserId)
                    .then()
                    .statusCode(200);
        }
    }

    @Test
    @Order(1)
    public void testCreateUser() {
        String userJson = "{ \"email\": \"new1@example.com\", \"name\": \"Jane Doe\" }";

        Response response = RestAssured.given()
                .contentType("application/json")
                .body(userJson)
                .post("/user");

        response.then()
                .statusCode(201)
                .body("data.email", equalTo("new1@example.com"))
                .body("data.name", equalTo("Jane Doe"));
    }

    @Test
    @Order(2)
    public void testGetUserById() {
        RestAssured.given()
                .when()
                .get("/user/" + createdUserId)
                .then()
                .statusCode(200);
    }

    @Test
    @Order(3)
    public void testGetAllUsers() {
        RestAssured.given()
                .when()
                .get("/user")
                .then()
                .statusCode(200);
    }

    @Test
    @Order(4)
    public void testUpdateUser() {
        String userJson = "{ \"email\": \"updated@example.com\", \"name\": \"John Updated\" }";

        RestAssured.given()
                .contentType("application/json")
                .body(userJson)
                .put("/user/" + createdUserId)
                .then()
                .statusCode(200);
    }

    @Test
    @Order(5)
    public void testDeleteUser() {
        RestAssured.given()
                .delete("/user/" + createdUserId)
//                .delete("/user/19")
                .then()
                .statusCode(200);
    }
}
