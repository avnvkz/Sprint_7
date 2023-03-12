package ru.yandex.praktikum.ezscooter.model.order;

import java.util.ArrayList;

public class Orders {

    private ArrayList<Order> orders;
    private PageInfo page;
    private ArrayList<AvailableStation> availableStations;

    public ArrayList<Order> getOrders() {
        return orders;
    }

    public PageInfo getPage() {
        return page;
    }

    public ArrayList<AvailableStation> getAvailableStations() {
        return availableStations;
    }

    public void setOrders(ArrayList<Order> orders) {
        this.orders = orders;
    }

    public void setPage(PageInfo page) {
        this.page = page;
    }

    public void setAvailableStations(ArrayList<AvailableStation> availableStations) {
        this.availableStations = availableStations;
    }
}
