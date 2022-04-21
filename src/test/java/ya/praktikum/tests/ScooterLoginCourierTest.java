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
import ya.praktikum.data.ScooterDeleteCourier;
import ya.praktikum.data.ScooterLoginCourier;
import ya.praktikum.data.ScooterRegisterCourier;
import ya.praktikum.models.ScooterCourierCredentials;

import java.util.ArrayList;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class ScooterLoginCourierTest {

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
    }

    @Test
    @DisplayName("check login courier")
    @Description("basic test for login courier with /api/v1/courier/login endpoint")
    @TmsLink("TestCase-5")
    @Issue("BUG-5")
    public void loginCourierPositiveTest() {

        ScooterRegisterCourier scooterRegisterCourier = new ScooterRegisterCourier();
        ArrayList<String> courierData = scooterRegisterCourier.registerNewCourierAndReturnLoginPassword();

        ScooterCourierCredentials courierCredentials = new ScooterCourierCredentials(
                courierData.get(0),
                courierData.get(1),
                RandomStringUtils.randomAlphabetic(10)
        );

                given()
                        .header("Content-type", "application/json")
                        .body(courierCredentials)
                        .when()
                        .post("/api/v1/courier/login")
                        .then()
                        .assertThat()
                        .body("id", notNullValue())
                        .and()
                        .statusCode(200);

        ScooterDeleteCourier scooterDeleteCourierTest = new ScooterDeleteCourier();
        scooterDeleteCourierTest.deleteTestCourierData(courierData.get(0), courierData.get(1));
    }

    @Test
    @DisplayName("check login courier")
    @Description("test for login courier without login with /api/v1/courier/login endpoint")
    @TmsLink("TestCase-6")
    @Issue("BUG-6")
    public void loginCourierWithoutLoginNegativeTest() {
        ScooterRegisterCourier scooterRegisterCourier = new ScooterRegisterCourier();
        ArrayList<String> courierData = scooterRegisterCourier.registerNewCourierAndReturnLoginPassword();

        ScooterCourierCredentials courierCredentials = new ScooterCourierCredentials(
                "",
                courierData.get(1),
                RandomStringUtils.randomAlphabetic(10)
        );

                given()
                        .header("Content-type", "application/json")
                        .body(courierCredentials)
                        .when()
                        .post("/api/v1/courier/login")
                        .then().assertThat().body("message",
                                equalTo("Недостаточно данных для входа"))
                        .and().statusCode(400);

        ScooterDeleteCourier scooterDeleteCourierTest = new ScooterDeleteCourier();
        scooterDeleteCourierTest.deleteTestCourierData(courierData.get(0), courierData.get(1));
    }

    @Test
    @DisplayName("check login courier")
    @Description("test for login courier without password with /api/v1/courier/login endpoint")
    @TmsLink("TestCase-7")
    @Issue("BUG-7")
    public void loginCourierWithoutPasswordNegativeTest() {
        ScooterRegisterCourier scooterRegisterCourier = new ScooterRegisterCourier();
        ArrayList<String> courierData = scooterRegisterCourier.registerNewCourierAndReturnLoginPassword();

        ScooterCourierCredentials courierCredentials = new ScooterCourierCredentials(
                courierData.get(0),
                "",
                RandomStringUtils.randomAlphabetic(10)
        );

                given()
                        .header("Content-type", "application/json")
                        .body(courierCredentials)
                        .when()
                        .post("/api/v1/courier/login")
                        .then().assertThat().body("message",
                                equalTo("Недостаточно данных для входа"))
                        .and().statusCode(400);

        ScooterDeleteCourier scooterDeleteCourierTest = new ScooterDeleteCourier();
        scooterDeleteCourierTest.deleteTestCourierData(courierData.get(0), courierData.get(1));
    }

    @Test
    @DisplayName("check login courier")
    @Description("test for login courier with non-existing login and password with /api/v1/courier/login endpoint")
    @TmsLink("TestCase-7")
    @Issue("BUG-7")
    public void loginCourierNegativeTest() {
        ScooterRegisterCourier scooterRegisterCourier = new ScooterRegisterCourier();
        ArrayList<String> courierData = scooterRegisterCourier.registerNewCourierAndReturnLoginPassword();

        ScooterDeleteCourier scooterDeleteCourierTest = new ScooterDeleteCourier();
        scooterDeleteCourierTest.deleteTestCourierData(courierData.get(0), courierData.get(1));

        ScooterCourierCredentials courierCredentials = new ScooterCourierCredentials(
                courierData.get(0),
                courierData.get(1),
                RandomStringUtils.randomAlphabetic(10)
        );

                given()
                        .header("Content-type", "application/json")
                        .body(courierCredentials)
                        .when()
                        .post("/api/v1/courier/login")
                        .then().assertThat().body("message",
                                equalTo("Учетная запись не найдена"))
                        .and().statusCode(404);
    }
}