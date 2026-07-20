DROP DATABASE IF EXISTS storage;
CREATE DATABASE storage;
USE storage;

DROP TABLE IF EXISTS product;
DROP TABLE IF EXISTS order_items;
DROP TABLE IF EXISTS orders;

CREATE TABLE product (	
	code int PRIMARY KEY AUTO_INCREMENT,
	name VARCHAR(100) NOT NULL,
	description VARCHAR(100),
	price DECIMAL(10,2) DEFAULT 0.00,
	quantity INT DEFAULT 0,
	immagine_copertina VARCHAR(255) DEFAULT NULL,
	mime_type VARCHAR(50) DEFAULT NULL
);

CREATE TABLE users(
	id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(20) DEFAULT 'user'
);

CREATE TABLE orders (
    id INT PRIMARY KEY AUTO_INCREMENT,

    user_id INT NOT NULL,

    order_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    name VARCHAR(25) NOT NULL,
    surname VARCHAR(30) NOT NULL,
    address VARCHAR(80) NOT NULL,
    numberaddress INT NOT NULL,

    total DECIMAL(10,2) NOT NULL,

    mail VARCHAR(100) NOT NULL,

    payment_method VARCHAR(20) NOT NULL,
    payment_last4 CHAR(4) DEFAULT NULL,

    status VARCHAR(20) NOT NULL DEFAULT 'CONFIRMED',

    FOREIGN KEY (user_id)
        REFERENCES users(id)
);

CREATE TABLE order_items (
    id INT PRIMARY KEY AUTO_INCREMENT,

    order_id INT NOT NULL,

    product_code INT NOT NULL,
    product_name VARCHAR(100) NOT NULL,

    unit_price DECIMAL(10,2) NOT NULL,
    quantity INT NOT NULL,

    FOREIGN KEY (order_id)
        REFERENCES orders(id)
        ON DELETE CASCADE
);

INSERT INTO product(name,description,price,quantity,immagine_copertina,mime_type) VALUES
	('LANCIA STRATOS TURBO EMINENECE ANDRUET-BICHE TOUR DE FRANCE 1976 #436','Modello 1:10',150.00,5,'Modellini-auto-scala-1-24-da-collezione.jpg','image/jpeg'),
    ('PORSCHE 911 SC GR.4 ROHRL-GEISTDORFER SAN REMO 1982 #1 - ARE 701-24','Modello 1:10',300.00,5,'170333.jpg','image/jpeg');
    
INSERT INTO users(username,password_hash,role) VALUES 
	('admin@gmail.com','c7ad44cbad762a5da0a452f9e854fdc1e0e7a52a38015f23f3eab1d80b931dd472634dfac71cd34ebc35d16ab7fb8a90c81f975113d6c7538dc69dd8de9077ec','admin');