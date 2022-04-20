package ya.praktikum.tests;

import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import io.qameta.allure.TmsLink;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ya.praktikum.models.ScooterOrder;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;

@RunWith(Parameterized.class)
public class ScooterCreateOrderTest {

    private final List<String> colorOption;
    String firstName = RandomStringUtils.randomAlphabetic(10);
    String lastName = RandomStringUtils.randomAlphabetic(10);
    String address = RandomStringUtils.randomAlphabetic(10);
    String metroStation = RandomStringUtils.randomAlphabetic(10);
    String phone = RandomStringUtils.randomNumeric(10);
    String rentTime = RandomStringUtils.randomNumeric(2);
    String deliveryDate = "01-01-2023";
    String comment = RandomStringUtils.randomAlphabetic(10);
    List<String> color;

    public ScooterCreateOrderTest(List<String> colorOption) {
        this.colorOption = colorOption;
    }

    @Parameterized.Parameters
    public static Object[] getData() {
        return new Object[][]{
                {List.of("GREY")},
                {List.of("GREY")},
                {List.of("GREY", "BLACK")},
                {List.of("")},
        };
    }

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
    }

    @Test
    @DisplayName("check creating order")
    @Description("basic test for creating order with /api/v1/orders endpoint")
    @TmsLink("TestCase-8")
    @Issue("BUG-8")
    public void createOrderPositiveTest() {
        ScooterOrder scooterOrder = new ScooterOrder(firstName, lastName, address, metroStation,
                phone, rentTime, deliveryDate, comment, color);
        scooterOrder.setColor(colorOption);

        Response response = given().header("Content-type", "application/json").and()
                .body(scooterOrder).when().post("/api/v1/orders");
        response.then().assertThat().body("track", notNullValue()).and().statusCode(201);
    }
}