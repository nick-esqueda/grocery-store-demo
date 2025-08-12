CREATE TABLE IF NOT EXISTS pickup_hours_adjustments (
    id INT NOT NULL AUTO_INCREMENT,
    store_id INT NOT NULL,
    start_datetime TIMESTAMP NOT NULL,
    end_datetime TIMESTAMP NOT NULL,
    is_available TINYINT(1) NOT NULL DEFAULT 0,
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6),
    PRIMARY KEY (id),
    FOREIGN KEY (store_id) REFERENCES stores(id)
);
