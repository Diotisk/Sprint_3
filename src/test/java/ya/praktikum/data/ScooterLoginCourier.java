package ya.praktikum.data;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import ya.praktikum.models.ScooterCourierCredentials;

import static io.restassured.RestAssured.given;

public class ScooterLoginCourier {

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
    }

    @Step("login courier")
    public String loginCourier(String login, String password) {

        ScooterCourierCredentials courierCredentials = new ScooterCourierCredentials(
                login,
                password,
                RandomStringUtils.randomAlphabetic(10));

        Response response =
                given()
                        .header("Content-type", "application/json")
                        .body(courierCredentials)
                        .when()
                        .post("/api/v1/courier/login");

        if (response.statusCode() == 200) {
            return response.jsonPath().get("id").toString();
        } else if (response.statusCode() == 404 && response.jsonPath().get("message").equals("Учетная запись не найдена")) {
            return String.format("No courier account with login %s and password %s found", login, password);
        } else return String.format("Error while getting courier id for login %s and password %s", login, password);
    }

}
