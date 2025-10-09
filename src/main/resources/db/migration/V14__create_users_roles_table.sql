CREATE TABLE IF NOT EXISTS users_roles (
    id INT NOT NULL AUTO_INCREMENT,
    user_id INT NOT NULL,
    role_id INT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (role_id) REFERENCES roles(id)
);
