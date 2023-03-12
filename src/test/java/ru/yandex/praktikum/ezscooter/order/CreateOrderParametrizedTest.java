package ru.yandex.praktikum.ezscooter.order;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.yandex.praktikum.ezscooter.client.OrderClient;
import ru.yandex.praktikum.ezscooter.model.order.Color;
import ru.yandex.praktikum.ezscooter.model.order.Order;
import ru.yandex.praktikum.ezscooter.model.order.OrderGenerator;
import java.util.Arrays;
import java.util.Collection;

import static org.apache.http.HttpStatus.SC_CREATED;
import static org.hamcrest.CoreMatchers.notNullValue;

@RunWith(Parameterized.class)
public class CreateOrderParametrizedTest {

    private OrderClient orderClient;
    private Color[] color;
    private int track;

    public CreateOrderParametrizedTest(Color[] color) {
        this.color = color;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> orderColors() {
        return Arrays.asList(new Object[][]{
                { new Color[] {Color.GRAY} },
                { new Color[] {Color.BLACK} },
                { new Color[] {Color.GRAY, Color.BLACK} },
                { new Color[] {} }
            }
        );
    }

    @BeforeClass
    public static void globalSetUp() {
        RestAssured.filters(
                new RequestLoggingFilter(), new ResponseLoggingFilter(),
                new AllureRestAssured()
        );
    }

    @Before
    public void setUp() {
        orderClient = new OrderClient();
    }

    @After
    public void cancelOrder() {
        orderClient.cancel(track);
    }

    @Test
    public void createOrderWithValidDateCreated201() {
        Order order = OrderGenerator.getRandom();
        order.setColor(color);
        track = orderClient.create(order)
                .assertThat()
                .statusCode(SC_CREATED)
                .and()
                .body("track", notNullValue())
                .extract().path("track");
    }
}
