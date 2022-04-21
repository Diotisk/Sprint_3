package ya.praktikum.data;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import ya.praktikum.models.ScooterCourierCredentials;

import java.util.ArrayList;

import static io.restassured.RestAssured.given;

public class ScooterRegisterCourier {

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
    }

    @Step("register new courier and return login and password")
    public ArrayList<String> registerNewCourierAndReturnLoginPassword() {

        ScooterCourierCredentials courierCredentials = new ScooterCourierCredentials(
                RandomStringUtils.randomAlphabetic(10),
                RandomStringUtils.randomAlphabetic(10),
                RandomStringUtils.randomAlphabetic(10)
                );

        ArrayList<String> loginPass = new ArrayList<>();

        Response response =
                given()
                        .header("Content-type", "application/json")
                        .body(courierCredentials)
                        .when()
                        .post("/api/v1/courier");

        if (response.statusCode() == 201 && response.jsonPath().getBoolean("ok")) {
            loginPass.add(courierCredentials.getLogin());
            loginPass.add(courierCredentials.getPassword());
        }

        return loginPass;
    }
}