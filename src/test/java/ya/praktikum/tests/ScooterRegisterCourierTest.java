package ya.praktikum.tests;

import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import io.qameta.allure.TmsLink;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import ya.praktikum.data.ScooterDeleteCourier;
import ya.praktikum.data.ScooterRegisterCourier;
import ya.praktikum.models.ScooterCourierCredentials;

import java.util.ArrayList;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

public class ScooterRegisterCourierTest {

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
    }

    @Test
    @DisplayName("check registering new courier")
    @Description("basic test for registering new courier with /api/v1/courier endpoint")
    @TmsLink("TestCase-1")
    @Issue("BUG-1")
    public void registerNewCourierCheckStatusAndResponsePositiveTest() {

        ScooterRegisterCourier scooterRegisterCourier = new ScooterRegisterCourier();
        ArrayList<String> courierLoginPass = scooterRegisterCourier.registerNewCourierAndReturnLoginPassword();

        ScooterDeleteCourier scooterDeleteCourier = new ScooterDeleteCourier();
        scooterDeleteCourier.deleteTestCourierData(courierLoginPass.get(0), courierLoginPass.get(1));
    }

    @Test
    @DisplayName("check registering new courier")
    @Description("test for registering new courier without login with /api/v1/courier endpoint")
    @TmsLink("TestCase-2")
    @Issue("BUG-2")
    public void registerNewCourierWithEmptyLoginNegativeTest() {

        ScooterCourierCredentials courierCredentials = new ScooterCourierCredentials(
                "",
                RandomStringUtils.randomAlphabetic(10),
                RandomStringUtils.randomAlphabetic(10)
        );

        given()
                .header("Content-type", "application/json")
                .body(courierCredentials)
                .when()
                .post("/api/v1/courier")
                .then().statusCode(400)
                .and()
                .assertThat().body("message",
                        equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("check registering new courier")
    @Description("test for registering new courier without password with /api/v1/courier endpoint")
    @TmsLink("TestCase-3")
    @Issue("BUG-3")
    public void registerNewCourierWithEmptyPasswordNegativeTest() {

        ScooterCourierCredentials courierCredentials = new ScooterCourierCredentials(
                RandomStringUtils.randomAlphabetic(10),
                "",
                RandomStringUtils.randomAlphabetic(10)
        );

        given()
                .header("Content-type", "application/json")
                .body(courierCredentials)
                .when()
                .post("/api/v1/courier")
                .then().statusCode(400)
                .and()
                .assertThat().body("message",
                        equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("check registering new courier")
    @Description("test for registering couriers with equal logins with /api/v1/courier endpoint")
    @TmsLink("TestCase-4")
    @Issue("BUG-4")
    public void registerNewCouriersWithEqualLoginsNegativeTest() {

        ScooterCourierCredentials courierCredentialsOne = new ScooterCourierCredentials(
                RandomStringUtils.randomAlphabetic(10),
                RandomStringUtils.randomAlphabetic(10),
                RandomStringUtils.randomAlphabetic(10)
        );

        ScooterCourierCredentials courierCredentialsTwo = new ScooterCourierCredentials(
                courierCredentialsOne.getLogin(),
                RandomStringUtils.randomAlphabetic(10),
                RandomStringUtils.randomAlphabetic(10)
        );

        given()
                .header("Content-type", "application/json")
                .body(courierCredentialsOne)
                .when()
                .post("/api/v1/courier")
                .then().statusCode(201)
                .and()
                .assertThat().body("ok", equalTo(true));

        given()
                .header("Content-type", "application/json")
                .body(courierCredentialsTwo)
                .when()
                .post("/api/v1/courier")
                .then().statusCode(409)
                .and()
                .assertThat().body("message",
                        equalTo("Этот логин уже используется. Попробуйте другой."));

        ScooterDeleteCourier scooterDeleteCourier = new ScooterDeleteCourier();
        scooterDeleteCourier.deleteTestCourierData(courierCredentialsOne.getLogin(),
                courierCredentialsOne.getPassword());
    }
}