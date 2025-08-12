CREATE TABLE IF NOT EXISTS pickup_hours (
    id INT NOT NULL AUTO_INCREMENT,
    store_id INT NOT NULL,
    day_of_week VARCHAR(10) NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6),
    PRIMARY KEY (id),
    FOREIGN KEY (store_id) REFERENCES stores(id)
);
