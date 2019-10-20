create table freight.drivers
(
    id     serial primary key,
    first_name   varchar(16) NOT NULL,
    last_name   varchar(16) NOT NULL,
    hours_per_month numeric NOT NULL,
    status varchar(3) NOT NULL,
    city_id integer NOT NULL,
    truck_id integer NOT NULL,
    foreign key (truck_id) references freight.trucks(id),
    foreign key (city_id) references freight.cities(id)

);
