package ru.yandex.praktikum.ezscooter.model.order;

public class ListOfOrders {

    private int courierId;
    private String[] nearestStation;
    private int limit;
    private int page;

    public ListOfOrders(int courierId, String[] nearestStation, int limit, int page) {
        this.courierId = courierId;
        this.nearestStation = nearestStation;
        this.limit = limit;
        this.page = page;
    }

    public int getCourierId() {
        return courierId;
    }

    public String[] getNearestStation() {
        return nearestStation;
    }

    public int getLimit() {
        return limit;
    }

    public int getPage() {
        return page;
    }

    public void setCourierId(int courierId) {
        this.courierId = courierId;
    }

    public void setNearestStation(String[] nearestStation) {
        this.nearestStation = nearestStation;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public void setPage(int page) {
        this.page = page;
    }
}
