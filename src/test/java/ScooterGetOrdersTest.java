import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import io.qameta.allure.TmsLink;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;

public class ScooterGetOrdersTest {

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
    }

    @Test
    @DisplayName("get all orders")
    @Description("basic test for getting all orders with /api/v1/orders endpoint")
    @TmsLink("TestCase-9")
    @Issue("BUG-9")
    public void getOrdersPositiveTest() {

        Response response = given()
                .header("Content-type", "application/json")
                .get("/api/v1/orders");
        response.then().assertThat().body("orders", notNullValue());

    }
}
