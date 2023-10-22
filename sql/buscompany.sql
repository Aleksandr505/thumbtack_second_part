DROP DATABASE IF EXISTS buscompany;
CREATE DATABASE `buscompany`;
USE `buscompany`;

CREATE TABLE user (
                      id integer(11) NOT NULL AUTO_INCREMENT,
                      firstName VARCHAR(50) NOT NULL,
                      lastName VARCHAR(50) NOT NULL,
                      patronymic VARCHAR(50),
                      login VARCHAR(50) NOT NULL,
                      password VARCHAR(50) NOT NULL,
                      user_type ENUM('ADMIN', 'CLIENT') NOT NULL,
                      version BIGINT NOT NULL,
                      PRIMARY KEY (id),
                      UNIQUE KEY user (login))
    ENGINE=INNODB DEFAULT CHARSET=utf8;
CREATE TABLE admin (
                       id integer(11) NOT NULL AUTO_INCREMENT,
                       user_id integer(11) NOT NULL,
                       position VARCHAR(50) NOT NULL,
                       FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE CASCADE,
                       PRIMARY KEY (id)
)
    ENGINE=INNODB DEFAULT CHARSET=utf8;
CREATE TABLE client (
                        id integer(11) NOT NULL AUTO_INCREMENT,
                        user_id integer(11) NOT NULL,
                        email VARCHAR(50) NOT NULL,
                        phone VARCHAR(50) NOT NULL,
                        FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE CASCADE,
                        PRIMARY KEY (id)
)
    ENGINE=INNODB DEFAULT CHARSET=utf8;
CREATE TABLE `session` (
                           user_id integer(11) NOT NULL,
                           cookie VARCHAR(50) NOT NULL,
                           FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE CASCADE,
                           PRIMARY KEY (user_id)
)
    ENGINE=INNODB DEFAULT CHARSET=utf8;
CREATE TABLE bus_depot (
                           id integer(11) NOT NULL AUTO_INCREMENT,
                           busName VARCHAR(50) NOT NULL,
                           placeCount integer(50) NOT NULL,
                           PRIMARY KEY (id)
)
    ENGINE=INNODB DEFAULT CHARSET=utf8;
CREATE TABLE trip (
                      id integer(11) NOT NULL AUTO_INCREMENT,
                      busBrand_id integer(11) NOT NULL,
                      fromStation VARCHAR(50) NOT NULL,
                      toStation VARCHAR(50) NOT NULL,
                      `start` time NOT NULL,
                      duration BIGINT NOT NULL,
                      price VARCHAR (50) NOT NULL,
                      approved boolean NOT NULL,
                      FOREIGN KEY (busBrand_id) REFERENCES bus_depot (id) ON DELETE CASCADE,
                      PRIMARY KEY (id)
)
    ENGINE=INNODB DEFAULT CHARSET=utf8;
CREATE TABLE trip_date (
                            id integer(11) NOT NULL AUTO_INCREMENT,
                            trip_id integer(11) NOT NULL,
                            `date` date NOT NULL,
                            freePlaces integer(11) NOT NULL,
                            FOREIGN KEY (trip_id) REFERENCES trip (id) ON DELETE CASCADE,
                            PRIMARY KEY (id)
)
    ENGINE=INNODB DEFAULT CHARSET=utf8;
CREATE TABLE `order` (
                         id integer(11) NOT NULL AUTO_INCREMENT,
                         trip_date_id integer(11) NOT NULL,
                         client_id integer(11) NOT NULL,
                         FOREIGN KEY (trip_date_id) REFERENCES trip_date (id) ON DELETE CASCADE,
                         FOREIGN KEY (client_id) REFERENCES client (user_id) ON DELETE CASCADE,
                         PRIMARY KEY (id)
)
    ENGINE=INNODB DEFAULT CHARSET=utf8;
CREATE TABLE passenger (
                           id integer(11) NOT NULL AUTO_INCREMENT,
                           firstName VARCHAR(50) NOT NULL,
                           lastName VARCHAR(50) NOT NULL,
                           passport VARCHAR(50) NOT NULL,
                           PRIMARY KEY (id),
                           UNIQUE KEY passenger (passport)
)
    ENGINE=INNODB DEFAULT CHARSET=utf8;
CREATE TABLE passenger_order (
                                 id integer(11) NOT NULL AUTO_INCREMENT,
                                 passenger_id integer(11) NOT NULL,
                                 order_id integer(11) NOT NULL,
                                 FOREIGN KEY (passenger_id) REFERENCES passenger (id) ON DELETE CASCADE,
                                 FOREIGN KEY (order_id) REFERENCES `order` (id) ON DELETE CASCADE,
                                 PRIMARY KEY (id)
)
    ENGINE=INNODB DEFAULT CHARSET=utf8;
CREATE TABLE place (
                       id integer(11) NOT NULL AUTO_INCREMENT,
                       passenger_id integer(11),
                       trip_date_id integer(11) NOT NULL,
                       placeNumber integer(11) NOT NULL,
                       FOREIGN KEY (passenger_id) REFERENCES passenger (id),
                       FOREIGN KEY (trip_date_id) REFERENCES trip_date (id) ON DELETE CASCADE,
                       PRIMARY KEY (id)
)
    ENGINE=INNODB DEFAULT CHARSET=utf8;