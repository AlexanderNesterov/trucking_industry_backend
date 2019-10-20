create table freight.cargo
(
    id     serial primary key,
    name   varchar(32) NOT NULL,
    weight numeric NOT NULL,
    status varchar(3) NOT NULL
);
