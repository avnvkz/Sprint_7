package ru.yandex.praktikum.ezscooter.client;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import ru.yandex.praktikum.ezscooter.client.base.ScooterRestClient;
import ru.yandex.praktikum.ezscooter.model.order.Order;
import ru.yandex.praktikum.ezscooter.model.order.Orders;


import static io.restassured.RestAssured.given;

public class OrderClient extends ScooterRestClient {

    private static final String ORDER_URI = BASE_URI + "orders";

    @Step("Create order {order}")
    public ValidatableResponse create(Order order) {
        return given()
                .spec(getBaseReqSpec())
                .body(order)
                .when()
                .post(ORDER_URI)
                .then();
    }

    @Step("Cancel order {track}")
    public ValidatableResponse cancel(int track) {
        return given()
                .spec(getBaseReqSpec())
                .body(track)
                .when()
                .put(ORDER_URI + "/cancel")
                .then();
    }

    @Step("Find order by number {track}")
    public ValidatableResponse findOrder(int track) {
        return given()
                .spec(getBaseReqSpec())
                .when()
                .get(ORDER_URI + "/track?t=" + track)
                .then();
    }

    @Step("Accept order {orderId} courier {courierID}")
    public ValidatableResponse acceptOrder(int orderId, int courierId) {
        return given()
                .spec(getBaseReqSpec())
                .when()
                .put(ORDER_URI + "/accept/" + orderId +"?courierId=" + courierId)
                .then();
    }

    @Step("Get a list of orders as POJO")
    public ValidatableResponse getList() {
        return given()
                .spec(getBaseReqSpec())
                .when()
                .get(ORDER_URI)
                .then();
    }

    @Step("Get a list of orders as POJO")
    public Orders getListAsPojo() {
        return given()
                .spec(getBaseReqSpec())
                .when()
                .get(ORDER_URI)
                .body().as(Orders.class);
    }

    @Step("Get a list of courier orders {courierId}")
    public ValidatableResponse acceptCourier(int courierId) {
        return given()
                .spec(getBaseReqSpec())
                .when()
                .get(ORDER_URI + "?courierId=" + courierId)
                .then();
    }

    @Step("Get a list of courier {courierId} orders station {station}")
    public ValidatableResponse acceptCourierNearStation(int courierId, String[] station) {
        return given()
                .spec(getBaseReqSpec())
                .when()
                .get(ORDER_URI + "?courierId=" + courierId
                        + "&nearestStation=[" + station + "]")
                .then();
    }

    @Step("Get a list of available orders with limit {limit} and page {page}")
    public ValidatableResponse availableWithLimitPage(int limit, int page) {
        return given()
                .spec(getBaseReqSpec())
                .when()
                .get(ORDER_URI + "?limit=" + limit + "&page=" + page)
                .then();
    }

    @Step("Get a list of available orders with limit {limit}, page {page} and station {station}")
    public ValidatableResponse availableWithLimitPageNearStation(int limit, int page, String[] station) {
        return given()
                .spec(getBaseReqSpec())
                .when()
                .get(ORDER_URI + "?limit=" + limit + "&page=" + page
                        + "&nearestStation=[" + station + "]")
                .then();
    }
}
