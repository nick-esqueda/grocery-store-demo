CREATE TABLE IF NOT EXISTS pickup_hours (
    id INT NOT NULL AUTO_INCREMENT,
    store_id INT NOT NULL,
    day_of_week INT NOT NULL, -- TODO: int (0-index) or enum?
    start_time DATE NOT NULL,
    end_time DATE NOT NULL,
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6),
    PRIMARY KEY (id),
    FOREIGN KEY (store_id) REFERENCES stores(id)
);
