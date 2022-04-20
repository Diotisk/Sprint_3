package ya.praktikum.data;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class ScooterLoginCourier {

    @Step("login courier")
    public String loginCourier(String login, String password) {

        String loginRequestBody = "{\"login\":\"" + login + "\"," + "\"password\":\"" + password + "\"}";

        Response response = given().header("Content-type", "application/json").and().body(loginRequestBody).when().post("http://qa-scooter.praktikum-services.ru/api/v1/courier/login");

        if (response.statusCode() == 200) {
            return response.jsonPath().get("id").toString();
        } else if (response.statusCode() == 404 && response.jsonPath().get("message").equals("Учетная запись не найдена")) {
            return String.format("No courier account with login %s and password %s found", login, password);
        } else return String.format("Error while getting courier id for login %s and password %s", login, password);
    }

}
