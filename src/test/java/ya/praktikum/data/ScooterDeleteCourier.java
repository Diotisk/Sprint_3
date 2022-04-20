package ya.praktikum.data;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import org.junit.Before;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

public class ScooterDeleteCourier {

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
    }

    @Step("delete created courier")
    public void deleteCreatedCourier(String courierId) {

        String deleteRequestBody = "{\"login\":\"" + courierId + "\"}";

        given()
                .header("Content-type", "application/json")
                .and()
                .body(deleteRequestBody)
                .when()
                .delete("http://qa-scooter.praktikum-services.ru/api/v1/courier/" + courierId)
                .then().statusCode(200)
                .and()
                .assertThat().body("ok", equalTo(true));
    }

    @Step("delete created courier by id and check deletion")
    public void deleteTestCourierData(String courierLogin, String courierPassword) {

        ScooterLoginCourier scooterLoginCourier =
                new ScooterLoginCourier();

        String courierId = scooterLoginCourier.loginCourier(courierLogin,
                courierPassword);

        deleteCreatedCourier(courierId);

        scooterLoginCourier.loginCourier(courierLogin, courierPassword).
                equals(String.format("No courier account with login %s and password %s found",
                        courierLogin, courierPassword));
    }
}
