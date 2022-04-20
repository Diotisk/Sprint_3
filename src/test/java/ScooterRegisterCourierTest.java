import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import io.qameta.allure.Step;
import io.qameta.allure.TmsLink;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

public class ScooterRegisterCourierTest {

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
    }

    /*
    метод регистрации нового курьера
    возвращает список из логина и пароля
    если регистрация не удалась, возвращает пустой список
    */
    @Step("register new Courier and return login and password")
    public ArrayList<String> registerNewCourierAndReturnLoginPassword() {

        // с помощью библиотеки RandomStringUtils генерируем логин
        // метод randomAlphabetic генерирует строку, состоящую только из букв, в качестве параметра передаём длину строки
        String courierLogin = RandomStringUtils.randomAlphabetic(10);
        // с помощью библиотеки RandomStringUtils генерируем пароль
        String courierPassword = RandomStringUtils.randomAlphabetic(10);
        // с помощью библиотеки RandomStringUtils генерируем имя курьера
        String courierFirstName = RandomStringUtils.randomAlphabetic(10);

        // создаём список, чтобы метод мог его вернуть
        ArrayList<String> loginPass = new ArrayList<>();

        // собираем в строку тело запроса на регистрацию, подставляя в него логин, пароль и имя курьера
        String registerRequestBody = "{\"login\":\"" + courierLogin + "\"," + "\"password\":\"" + courierPassword + "\"," + "\"firstName\":\"" + courierFirstName + "\"}";

        // отправляем запрос на регистрацию курьера и сохраняем ответ в переменную response класса Response
        Response response = given().header("Content-type", "application/json").and().body(registerRequestBody).when().post("http://qa-scooter.praktikum-services.ru/api/v1/courier");

        // если регистрация прошла успешно (код ответа 201), добавляем в список логин и пароль курьера
        if (response.statusCode() == 201 && response.jsonPath().getBoolean("ok")) {
            loginPass.add(courierLogin);
            loginPass.add(courierPassword);
        }

        // возвращаем список
        return loginPass;

    }

    @Test
    @DisplayName("check registering new courier")
    @Description("basic test for registering new courier with /api/v1/courier endpoint")
    @TmsLink("TestCase-1")
    @Issue("BUG-1")
    public void registerNewCourierCheckStatusAndResponsePositiveTest() {

        // с помощью библиотеки RandomStringUtils генерируем логин
        // метод randomAlphabetic генерирует строку, состоящую только из букв, в качестве параметра передаём длину строки
        String courierLogin = RandomStringUtils.randomAlphabetic(10);
        // с помощью библиотеки RandomStringUtils генерируем пароль
        String courierPassword = RandomStringUtils.randomAlphabetic(10);
        // с помощью библиотеки RandomStringUtils генерируем имя курьера
        String courierFirstName = RandomStringUtils.randomAlphabetic(10);

        // собираем в строку тело запроса на регистрацию, подставляя в него логин, пароль и имя курьера
        String registerRequestBody = "{\"login\":\"" + courierLogin + "\"," + "\"password\":\"" + courierPassword + "\"," + "\"firstName\":\"" + courierFirstName + "\"}";

        // отправляем запрос на регистрацию курьера и сохраняем ответ в переменную response класса Response
        given().header("Content-type", "application/json").and().body(registerRequestBody).when().post("/api/v1/courier").then().statusCode(201).and().assertThat().body("ok", equalTo(true));

        ScooterDeleteCourierTest scooterDeleteCourier = new ScooterDeleteCourierTest();
        scooterDeleteCourier.deleteTestCourierDataForTest(courierLogin, courierPassword);
    }

    @Test
    @DisplayName("check registering new courier")
    @Description("test for registering new courier without login with /api/v1/courier endpoint")
    @TmsLink("TestCase-2")
    @Issue("BUG-2")
    public void registerNewCourierWithEmptyLoginNegativeTest() {
        String courierLogin = "";
        // с помощью библиотеки RandomStringUtils генерируем пароль
        String courierPassword = RandomStringUtils.randomAlphabetic(10);
        // с помощью библиотеки RandomStringUtils генерируем имя курьера
        String courierFirstName = RandomStringUtils.randomAlphabetic(10);

        // собираем в строку тело запроса на регистрацию, подставляя в него логин, пароль и имя курьера
        String registerRequestBody = "{\"login\":\"" + courierLogin + "\"," + "\"password\":\"" + courierPassword + "\"," + "\"firstName\":\"" + courierFirstName + "\"}";

        // отправляем запрос на регистрацию курьера и сохраняем ответ в переменную response класса Response
        given().header("Content-type", "application/json").and().body(registerRequestBody).when().post("/api/v1/courier").then().statusCode(400).and().assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("check registering new courier")
    @Description("test for registering new courier without password with /api/v1/courier endpoint")
    @TmsLink("TestCase-3")
    @Issue("BUG-3")
    public void registerNewCourierWithEmptyPasswordNegativeTest() {
        String courierLogin = RandomStringUtils.randomAlphabetic(10);
        String courierPassword = "";
        String courierFirstName = RandomStringUtils.randomAlphabetic(10);

        // собираем в строку тело запроса на регистрацию, подставляя в него логин, пароль и имя курьера
        String registerRequestBody = "{\"login\":\"" + courierLogin + "\"," + "\"password\":\"" + courierPassword + "\"," + "\"firstName\":\"" + courierFirstName + "\"}";

        // отправляем запрос на регистрацию курьера и сохраняем ответ в переменную response класса Response
        given().header("Content-type", "application/json").and().body(registerRequestBody).when().post("/api/v1/courier").then().statusCode(400).and().assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("check registering new courier")
    @Description("test for registering couriers with equal logins with /api/v1/courier endpoint")
    @TmsLink("TestCase-4")
    @Issue("BUG-4")
    public void registerNewCouriersWithEqualLoginsNegativeTest() {
        String courierLogin = RandomStringUtils.randomAlphabetic(10);
        String courierPassword = RandomStringUtils.randomAlphabetic(10);
        String courierFirstName = RandomStringUtils.randomAlphabetic(10);

        String anotherCourierLogin = courierLogin;
        String anotherCourierPassword = RandomStringUtils.randomAlphabetic(10);
        String anotherCourierFirstName = RandomStringUtils.randomAlphabetic(10);

        String registerRequestBody = "{\"login\":\"" + courierLogin + "\"," + "\"password\":\"" + courierPassword + "\"," + "\"firstName\":\"" + courierFirstName + "\"}";

        String anotherRegisterRequestBody = "{\"login\":\"" + anotherCourierLogin + "\"," + "\"password\":\"" + anotherCourierPassword + "\"," + "\"firstName\":\"" + anotherCourierFirstName + "\"}";

        given().header("Content-type", "application/json").and().body(registerRequestBody).when().post("/api/v1/courier").then().statusCode(201).and().assertThat().body("ok", equalTo(true));

        given().header("Content-type", "application/json").and().body(anotherRegisterRequestBody).when().post("/api/v1/courier").then().statusCode(409).and().assertThat().body("message", equalTo("Этот логин уже используется. Попробуйте другой."));

        ScooterDeleteCourierTest scooterDeleteCourier = new ScooterDeleteCourierTest();
        scooterDeleteCourier.deleteTestCourierDataForTest(courierLogin, courierPassword);
    }
}