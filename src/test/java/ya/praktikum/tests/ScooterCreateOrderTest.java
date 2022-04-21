package ya.praktikum.tests;

import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import io.qameta.allure.TmsLink;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ya.praktikum.data.Order;

import java.util.List;

import static org.hamcrest.CoreMatchers.notNullValue;

@RunWith(Parameterized.class)
public class ScooterCreateOrderTest {

    private final List<String> colorOption;

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

    @Test
    @DisplayName("check creating order")
    @Description("basic test for creating order with /api/v1/orders endpoint")
    @TmsLink("TestCase-8")
    @Issue("BUG-8")
    public void createOrderPositiveTest() {
        Order testOrder = new Order();
        Response actualResult = testOrder.create(colorOption);
        actualResult.then().assertThat().body("track", notNullValue()).and().statusCode(201);
    }
}