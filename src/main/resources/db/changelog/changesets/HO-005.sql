create table freight.points
(
    id       serial primary key,
    type     boolean NOT NULL,
    city_id  integer NOT NULL,
    cargo_id integer NOT NULL,
    order_id integer NOT NULL,
    foreign key (cargo_id) references freight.trucks(id),
    foreign key (city_id) references freight.cities(id),
    foreign key (order_id) references freight.orders(id)
);
