INSERT INTO customer (name, email, password) VALUES 
('Maria Silva', 'maria.silva@email.com', '$2a$12$wFFYmBkKfE6r8YQt.eocdeAS7qSdr5yTA3FWHF2WWC8qBcO8uEkBi'),
('Joao Souza', 'joao.souza@email.com', '$2a$12$b4pt7n2qW7op2LqVrocHXO6C2SXmD2S/t38Ohu571B/.o5CBrwZZi');

INSERT INTO item (name, price) VALUES
('Item A', 10.99),
('Item B', 20.50),
('Item C', 15.00);

INSERT INTO customer_order (order_date, customer_id) VALUES
('2023-09-15 10:30:00', 1),
('2023-09-16 14:00:00', 2);

INSERT INTO order_item (item_id, order_id, quantity) VALUES
(1, 1, 2),
(2, 1, 1),
(3, 2, 5);

INSERT INTO stock (product_id, quantity) VALUES
(1, 100),
(2, 150),
(3, 200);
