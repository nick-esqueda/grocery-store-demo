CREATE TABLE IF NOT EXISTS stores (
    id INT NOT NULL AUTO_INCREMENT,
    address VARCHAR(255) NOT NULL UNIQUE,
    total_pickup_spots INT NOT NULL,
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6),
    PRIMARY KEY (id)
);