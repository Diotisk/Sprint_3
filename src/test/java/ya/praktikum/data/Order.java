package ya.praktikum.data;

import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import ya.praktikum.models.ScooterOrder;

import java.util.List;

import static io.restassured.RestAssured.given;

public class Order {

    String firstName = RandomStringUtils.randomAlphabetic(10);
    String lastName = RandomStringUtils.randomAlphabetic(10);
    String address = RandomStringUtils.randomAlphabetic(10);
    String metroStation = RandomStringUtils.randomAlphabetic(10);
    String phone = RandomStringUtils.randomNumeric(10);
    String rentTime = RandomStringUtils.randomNumeric(2);
    String deliveryDate = "01-01-2023";
    String comment = RandomStringUtils.randomAlphabetic(10);
    List<String> color;

    ScooterOrder testOrder = new ScooterOrder(firstName, lastName, address, metroStation,
            phone, rentTime, deliveryDate, comment, color);

    public Response create(List<String> colorOption) {

        testOrder.setColor(colorOption);

        Response response = given()
                .header("Content-type", "application/json")
                .body(testOrder)
                .when()
                .post("http://qa-scooter.praktikum-services.ru/api/v1/orders");

        return response;
    }

    public Response getOrders() {
        Response response = given()
                .header("Content-type", "application/json")
                .get("http://qa-scooter.praktikum-services.ru/api/v1/orders");

        return response;
    }
}
