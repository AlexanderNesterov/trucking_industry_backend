create table freight.orders
(
    id     serial primary key,
    is_done   boolean NOT NULL,
    truck_id integer NOT NULL,
    foreign key (truck_id) references freight.trucks(id)
);
