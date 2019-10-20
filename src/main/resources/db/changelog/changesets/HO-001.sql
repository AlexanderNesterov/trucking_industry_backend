create schema freight;

create table freight.cities
(
    id   serial primary key,
    name varchar(32) NOT NULL
);

create table freight.trucks
(
    id          serial primary key,
    reg_num     varchar(7) NOT NULL UNIQUE,
    shift_size  integer NOT NULL,
    capacity    numeric NOT NULL,
    condition   boolean,
    city_id     integer NOT NULL,
    foreign key (city_id) references freight.cities (id)
);

create table freight.cities_distances
(
    id             serial primary key,
    first_city_id  integer NOT NULL,
    second_city_id integer NOT NULL,
    distance       numeric NOT NULL,
    foreign key (first_city_id) references freight.cities (id),
    foreign key (second_city_id) references freight.cities (id)
);
