import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import io.qameta.allure.Step;
import io.qameta.allure.TmsLink;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class ScooterLoginCourierTest {

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
    }

    @Step("login courier")
    public String loginCourierForTest(String login, String password) {

        String loginRequestBody = "{\"login\":\"" + login + "\"," + "\"password\":\"" + password + "\"}";

        Response response = given().header("Content-type", "application/json").and().body(loginRequestBody).when().post("http://qa-scooter.praktikum-services.ru/api/v1/courier/login");

        if (response.statusCode() == 200) {
            return response.jsonPath().get("id").toString();
        } else if (response.statusCode() == 404 && response.jsonPath().get("message").equals("Учетная запись не найдена")) {
            return String.format("No courier account with login %s and password %s found", login, password);
        } else return String.format("Error while getting courier id for login %s and password %s", login, password);
    }

    @Test
    @DisplayName("check login courier")
    @Description("basic test for login courier with /api/v1/courier/login endpoint")
    @TmsLink("TestCase-5")
    @Issue("BUG-5")
    public void loginCourierPositiveTest() {

        ScooterRegisterCourierTest scooterRegisterCourierTest = new ScooterRegisterCourierTest();
        ArrayList<String> courierData = scooterRegisterCourierTest.registerNewCourierAndReturnLoginPassword();

        String loginRequestBody = "{\"login\":\"" + courierData.get(0) + "\"," + "\"password\":\"" + courierData.get(1) + "\"}";

        Response response = given().header("Content-type", "application/json").and().body(loginRequestBody).when().post("/api/v1/courier/login");
        response.then().assertThat().body("id", notNullValue()).and().statusCode(200);

        ScooterDeleteCourierTest scooterDeleteCourierTest = new ScooterDeleteCourierTest();
        scooterDeleteCourierTest.deleteTestCourierDataForTest(courierData.get(0), courierData.get(1));
    }

    @Test
    @DisplayName("check login courier")
    @Description("test for login courier without login with /api/v1/courier/login endpoint")
    @TmsLink("TestCase-6")
    @Issue("BUG-6")
    public void loginCourierWithoutLoginNegativeTest() {
        ScooterRegisterCourierTest scooterRegisterCourierTest = new ScooterRegisterCourierTest();
        ArrayList<String> courierData = scooterRegisterCourierTest.registerNewCourierAndReturnLoginPassword();

        String loginRequestBody = "{\"login\":\"" + "" + "\"," + "\"password\":\"" + courierData.get(1) + "\"}";

        Response response = given().header("Content-type", "application/json").and().body(loginRequestBody).when().post("/api/v1/courier/login");
        response.then().assertThat().body("message", equalTo("Недостаточно данных для входа")).and().statusCode(400);

        ScooterDeleteCourierTest scooterDeleteCourierTest = new ScooterDeleteCourierTest();
        scooterDeleteCourierTest.deleteTestCourierDataForTest(courierData.get(0), courierData.get(1));
    }

    @Test
    @DisplayName("check login courier")
    @Description("test for login courier without password with /api/v1/courier/login endpoint")
    @TmsLink("TestCase-7")
    @Issue("BUG-7")
    public void loginCourierWithoutPasswordNegativeTest() {
        ScooterRegisterCourierTest scooterRegisterCourierTest = new ScooterRegisterCourierTest();
        ArrayList<String> courierData = scooterRegisterCourierTest.registerNewCourierAndReturnLoginPassword();

        String loginRequestBody = "{\"login\":\"" + courierData.get(0) + "\"," + "\"password\":\"" + "" + "\"}";

        Response response = given().header("Content-type", "application/json").and().body(loginRequestBody).when().post("/api/v1/courier/login");
        response.then().assertThat().body("message", equalTo("Недостаточно данных для входа")).and().statusCode(400);

        ScooterDeleteCourierTest scooterDeleteCourierTest = new ScooterDeleteCourierTest();
        scooterDeleteCourierTest.deleteTestCourierDataForTest(courierData.get(0), courierData.get(1));
    }

    @Test
    @DisplayName("check login courier")
    @Description("test for login courier with non-existing login and password with /api/v1/courier/login endpoint")
    @TmsLink("TestCase-7")
    @Issue("BUG-7")
    public void loginCourierNegativeTest() {
        ScooterRegisterCourierTest scooterRegisterCourierTest = new ScooterRegisterCourierTest();
        ArrayList<String> courierData = scooterRegisterCourierTest.registerNewCourierAndReturnLoginPassword();

        ScooterDeleteCourierTest scooterDeleteCourierTest = new ScooterDeleteCourierTest();
        scooterDeleteCourierTest.deleteTestCourierDataForTest(courierData.get(0), courierData.get(1));

        String loginRequestBody = "{\"login\":\"" + courierData.get(0) + "\"," + "\"password\":\"" + courierData.get(1) + "\"}";

        Response response = given().header("Content-type", "application/json").and().body(loginRequestBody).when().post("/api/v1/courier/login");
        response.then().assertThat().body("message", equalTo("Учетная запись не найдена")).and().statusCode(404);
    }
}