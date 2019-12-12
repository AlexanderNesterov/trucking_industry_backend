package com.example.services.commons.message;

public class OrderExceptionMessage {
    public static final String ORDER_NOT_FOUND = "Order with id %d not found";
    public static final String ORDER_BY_DRIVER_NOT_FOUND = "Order with driver id %d not found";
    public static final String WRONG_ORDER_STATUS = "Attempt to set %s status to wrong order";
    public static final String WRONG_ORDER_OR_DRIVER = "Wrong order id %d or driver id %d";
}
