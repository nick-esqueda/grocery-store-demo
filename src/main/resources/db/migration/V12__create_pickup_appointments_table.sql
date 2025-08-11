CREATE TABLE IF NOT EXISTS pickup_appointments (
    id INT NOT NULL AUTO_INCREMENT,
    store_id INT NOT NULL,
    order_id INT NOT NULL,
    user_id INT NOT NULL,
    start_datetime TIMESTAMP NOT NULL, -- TODO: verify - want unix timestamps
    end_datetime TIMESTAMP NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6),
    PRIMARY KEY (id),
    FOREIGN KEY (store_id) REFERENCES stores(id),
    FOREIGN KEY (order_id) REFERENCES orders(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);
