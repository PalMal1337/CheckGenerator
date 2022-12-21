CREATE TABLE discount_cards
(
    discount_number INT PRIMARY KEY,
    discount INT NOT NULL CHECK (discount BETWEEN 1 AND 99)
);

CREATE TABLE products
(
    id SERIAL PRIMARY KEY,
    discount_number INT NOT NULL,
    product_name VARCHAR(40) NOT NULL,
    price DECIMAL(5,2) NOT NULL,
    FOREIGN KEY (discount_number) REFERENCES discount_cards
);

INSERT INTO discount_cards VALUES
(1432,10),(235,20),(1462,30),(9234,40);

INSERT INTO products(discount_number,product_name,price) VALUES
(1432,'Milk',2.35)
(1432,'Ice-Cream',2.35),
(null,'Sneakers',1.25),
(null,'Cheese',4.10),
(null,'Chicken',8.00),
(235,'Meat',10.30),
(235,'Soda',0.99),
(1462,'Fish',15.40),
(9234,'Sugar',0.49);
