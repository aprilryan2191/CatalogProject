create table categories
(
    id   serial8,
    name varchar not null,
    primary key (id)
);

insert into categories (name)
values ('Процессоры'),
       ('Мониторы');

create table characteristics
(
    id          serial8,
    category_id int8    not null,
    name        varchar not null,
    primary key (id),
    foreign key (category_id) references categories (id)
);

insert into characteristics (category_id, name)
values (1, 'Производитель'),
       (1, 'Количество ядер'),
       (1, 'Сокет'),
       (2, 'Производитель'),
       (2, 'Диагональ'),
       (2, 'Матрица'),
       (2, 'Разрешение');

create table products
(
    id          serial8,
    category_id int8    not null,
    name        varchar not null,
    price       int4 not null,
    primary key (id),
    foreign key (category_id) references categories (id)
);

insert into products (category_id, name, price)
values (1, 'Intel Core I9 9900', '399990'),
       (1, 'AMD Ryzen R7 7700', '349990'),
       (2, 'Samsung SU556270', '54990'),
       (2, 'AOC Z215S659', '39990');

create table products_info
(
    id                serial8,
    characteristic_id int8,
    product_id        int8,
    name              varchar not null,
    primary key (id),
    foreign key (characteristic_id) references characteristics (id),
    foreign key (product_id) references products (id)
);

insert into products_info (characteristic_id, product_id, name)
values (1, 1, 'Intel'),
       (2, 1, '8'),
       (3, 1, '1250'),
       (1, 2, 'AMD'),
       (2, 2, '12'),
       (3, 2, 'AM4'),
       (4, 3, 'Samsung'),
       (5, 3, '27'),
       (6, 3, 'TN'),
       (7, 3, '2560*1440'),
       (4, 4, 'AOC'),
       (5, 4, '21.5'),
       (6, 4, 'AH-IPS'),
       (7, 4, '1920*1080');

-- Вывести харакетеристики товара с id = 2
select pi.*
from products_info pi
         join products p on p.id = pi.product_id
where product_id = 2;

-- Вывести название характеристик товара с id = 2
select c.*
from characteristics c
         join products p on c.category_id = p.category_id
where p.id = 2;

-- Вывести товары у которых значение первой характеристики (Производитель) AMD.
select p.*
from products p
         join products_info pi on p.id = pi.product_id
where pi.name = 'AMD';
