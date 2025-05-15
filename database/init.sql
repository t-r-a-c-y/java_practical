CREATE DATABASE IF NOT EXISTS hotel_booking;

USE hotel_booking;

CREATE TABLE users (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       name VARCHAR(255) NOT NULL,
                       email VARCHAR(255) UNIQUE NOT NULL,
                       password VARCHAR(255) NOT NULL,
                       role ENUM('ADMIN', 'CUSTOMER') NOT NULL
);

CREATE TABLE hotels (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        name VARCHAR(255) NOT NULL,
                        location VARCHAR(255) NOT NULL
);

CREATE TABLE rooms (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       hotel_id BIGINT NOT NULL,
                       room_type VARCHAR(255) NOT NULL,
                       price DOUBLE NOT NULL,
                       is_available BOOLEAN NOT NULL,
                       FOREIGN KEY (hotel_id) REFERENCES hotels(id)
);

CREATE TABLE bookings (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          user_id BIGINT NOT NULL,
                          room_id BIGINT NOT NULL,
                          check_in DATE NOT NULL,
                          check_out DATE NOT NULL,
                          status ENUM('CONFIRMED', 'CANCELLED') NOT NULL,
                          FOREIGN KEY (user_id) REFERENCES users(id),
                          FOREIGN KEY (room_id) REFERENCES rooms(id)
);

CREATE TABLE billing (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         booking_id BIGINT NOT NULL,
                         amount DOUBLE NOT NULL,
                         generated_at DATETIME NOT NULL,
                         FOREIGN KEY (booking_id) REFERENCES bookings(id)
);

DELIMITER //

CREATE TRIGGER after_booking_insert
    AFTER INSERT ON bookings
    FOR EACH ROW
BEGIN
    IF NEW.status = 'CONFIRMED' THEN
        INSERT INTO billing (booking_id, amount, generated_at)
        VALUES (NEW.id, (
            SELECT r.price * DATEDIFF(NEW.check_out, NEW.check_in)
            FROM rooms r
            WHERE r.id = NEW.room_id
        ), NOW());
END IF;
END;//

DELIMITER ;