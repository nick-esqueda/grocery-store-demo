CREATE TABLE IF NOT EXISTS payments (
    id INT NOT NULL AUTO_INCREMENT,
    user_id INT NOT NULL,
    order_id INT NOT NULL,
    total_price DECIMAL(6, 2) NOT NULL,
    payment_method_token VARCHAR(255) NOT NULL,
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6),
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (order_id) REFERENCES orders(id)
);
