package com.example.ddubuk;

public class ItineraryData {
    private String arrival;
    private String travel_name;
    private String departure;
    private String days;
    private String budget;
    private String use_money;
    private String key;

    public ItineraryData() {
    }

    public String getArrival() {
        return arrival;
    }

    public String getTravel_name() {
        return travel_name;
    }

    public String getDeparture() {
        return departure;
    }

    public String getDays() {
        return days;
    }

    public void setArrival(String arrival) {
        this.arrival = arrival;
    }

    public void setTravel_name(String travel_name) {
        this.travel_name = travel_name;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUse_money() {
        return use_money;
    }

    public void setUse_money(String use_money) {
        this.use_money = use_money;
    }
}