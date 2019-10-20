create table freight.busy_drivers
(
    id        serial primary key,
    driver_id integer NOT NULL,
    order_id  integer NOT NULL,
    foreign key (driver_id) references freight.drivers(id),
    foreign key (order_id) references freight.orders(id)
);
