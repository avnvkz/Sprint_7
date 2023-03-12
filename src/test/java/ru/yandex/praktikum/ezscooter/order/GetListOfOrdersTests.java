package ru.yandex.praktikum.ezscooter.order;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.yandex.praktikum.ezscooter.client.CourierClient;
import ru.yandex.praktikum.ezscooter.client.OrderClient;
import ru.yandex.praktikum.ezscooter.model.courier.Courier;
import ru.yandex.praktikum.ezscooter.model.courier.CourierCredentials;
import ru.yandex.praktikum.ezscooter.model.courier.CourierGenerator;
import ru.yandex.praktikum.ezscooter.model.order.Order;
import ru.yandex.praktikum.ezscooter.model.order.OrderGenerator;
import ru.yandex.praktikum.ezscooter.model.order.Orders;

import java.util.ArrayList;
import java.util.Random;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

public class GetListOfOrdersTests {

    private OrderClient orderClient;
    private CourierClient courierClient;
    private ArrayList ordersJson;
    private Orders ordersPojo;
    private int courierId;
    private int track;
    private int orderId;

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
        courierClient = new CourierClient();
    }

    @Test
    public void getListOfOrdersWithoutParametersOk200() {
        ordersJson = orderClient.getList()
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .assertThat()
                .body("orders", notNullValue())
                .extract().body().path("orders");
        ordersPojo = orderClient.getListAsPojo();
    }

    @Test
    public void acceptCourierOrdersWithValidDataOk200(){
        Courier courier = CourierGenerator.getRandom();
        Order order = OrderGenerator.getRandom();

        courierClient.create(courier)
                .assertThat()
                .statusCode(SC_CREATED)
                .and()
                .assertThat()
                .body("ok", is(true));

        courierId = courierClient.login(CourierCredentials.from(courier))
                .assertThat()
                .body("id", notNullValue())
                .extract().path("id");

        track = orderClient.create(order)
                .assertThat()
                .statusCode(SC_CREATED)
                .and()
                .body("track", notNullValue())
                .extract().path("track");

        orderId = orderClient.findOrder(track)
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .assertThat()
                .body("order.id", notNullValue())
                .extract().body().path("order.id");

        orderClient.acceptOrder(orderId, courierId)
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .body("ok", is(true));

        ordersJson = orderClient.acceptCourier(courierId)
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .assertThat()
                .body("orders.size()", is(1))
                .extract().body().path("orders");

        courierClient.delete(courierId);
    }

    @Test
    public void acceptCourierWithInvalidDataNotFound() {
        Random random = new Random();
        courierId = random.nextInt(10);
        orderClient.acceptCourier(courierId)
                .assertThat()
                .statusCode(SC_NOT_FOUND)
                .and()
                .assertThat()
                .body("message", is("Курьер с идентификатором " + courierId + " не найден"));
    }

}
