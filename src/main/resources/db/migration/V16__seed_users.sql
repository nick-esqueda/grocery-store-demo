INSERT INTO users
(username, password, first_name, last_name, address, email, phone_number, recent_payment_method_token, created_at, updated_at)
VALUES
('admin', '$2a$12$glSHoTkZoOD4BdKlesvSXuKQnoIVTW0F3VB33UR0qp2E/AGXm6KW.', 'Admin', 'User', 'Apt. 213 6976 Roberta Unions, East Alphonse, OH 09425-1723', 'admin@gmail.com', '+17845781861', null, NOW(), null),
('demo_user', '$2a$12$kjc0mxwiGdvAYnHeuIY6PuHFuz4WCAAjY9YUElORMwQ.oq9EjVany', 'Demo', 'User', 'Suite 119 931 Terry Skyway, Abdulport, CT 62760', 'customer@gmail.com', '+15760332621', null, NOW(), null);
