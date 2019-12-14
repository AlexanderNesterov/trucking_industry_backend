package com.example.services.commons.message;

public class OrderExceptionMessage {
    public static final String ORDER_NOT_FOUND = "Order with id %d not found";
    public static final String ORDER_BY_DRIVER_NOT_FOUND = "Order with driver id %d not found";
    public static final String WRONG_ORDER_STATUS = "Attempt to set %s status to wrong order";
    public static final String WRONG_ORDER_OR_DRIVER = "Wrong order id %d or driver id %d";
    public static final String WRONG_ORDER = "Wrong order id or order status";
    public static final String DRIVER_ID_NULL = "Driver id and co-driver id cannot equals null";
    public static final String EQUALS_DRIVER_ID = "Driver id and co-driver id cannot be equals. " +
            "Driver id: %d , co-driver id: %d";
    public static final String WRONG_DRIVER = "Wrong driver id or driver status";
    public static final String WRONG_CO_DRIVER = "Wrong co-driver id or co-driver status";
    public static final String WRONG_TRUCK = "Wrong truck id or truck condition " +
            "or truck already include in another cargo";
    public static final String TOTAL_WEIGHT = "Incorrect total weight";
    public static final String NUMBER_OF_CARGO = "Incorrect number of cargo";
    public static final String EQUALS_LOCATIONS = "Cargo can't has equals load and discharge location";
    public static final String WRONG_CITY = "Wrong city id";
}
