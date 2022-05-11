package com.example.ddubuk;

public class Schedule {
    private static String key;
    private static String arrival;
    private static String budget;
    private static String use_money;
    private static String days="1";
    private static String departure;
    private static String detail;
    private static String travel_name = null;

    public static String getArrival() {
        return arrival;
    }

    public static void setArrival(String arrival) {
        Schedule.arrival = arrival;
    }

    public static String getBudget() {
        return budget;
    }

    public static void setBudget(String budget) {
        Schedule.budget = budget;
    }

    public static String getUse_money() {
        return use_money;
    }

    public static void setUse_money(String use) {
        Schedule.use_money = use;
    }

    public static String getDays() {
        return days;
    }

    public static void setDays(String days) {
        Schedule.days = days;
    }

    public static String getDeparture() {
        return departure;
    }

    public static void setDeparture(String departure) {
        Schedule.departure = departure;
    }

    public static String getDetail() {
        return detail;
    }

    public static void setDetail(String detail) {
        Schedule.detail = detail;
    }

    public static String getTravel_name() {
        return travel_name;
    }

    public static void setTravel_name(String travel_name) {
        Schedule.travel_name = travel_name;
    }

    public static String getKey() {
        return key;
    }

    public static void setKey(String key) {
        Schedule.key = key;
    }
}
