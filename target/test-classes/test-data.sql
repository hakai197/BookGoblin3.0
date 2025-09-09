BEGIN TRANSACTION;

-- Users
INSERT INTO users (username, password_hash, role) VALUES ('user1','user1','ROLE_USER');
INSERT INTO users (username, password_hash, role) VALUES ('user2','user2','ROLE_USER');
INSERT INTO users (username, password_hash, role) VALUES ('user3','user3','ROLE_USER');

INSERT INTO Shipper (name, address, city, state, zip_code) VALUES
('R&L Carriers', '600 Gilliam Road', 'Wilmington', 'OH', '43178');


INSERT INTO Trailer (trailer_number, trailer_type, shipper_id) VALUES
('OF4328', 'Local/City', 1);


INSERT INTO Customer (order_number, name, address, city, state, zip_code, door_number, trailer_id, handling_unit, weight) VALUES
('4887500387', 'Alec Holland', '1972 Wein Way', 'Baton Rouge', 'LA', '87767'),
('087045987', 'Eobard Thawne', '3500 Flash Road', 'Central City', 'KA', '53202'),
('398847905', 'Buddy Barker', '39 Animals Street', 'Star City', 'NV', '89011');

INSERT INTO Unloader (name, shift, employee_number) VALUES
('Erik Lehnsherr', '2nd Shift', '182734');


-- OF1234 Trailer Orders
INSERT INTO Order_Number (order_number, customer_id, shipper_id, trailer_id, door_number, handling_unit, weight,status) VALUES
('123456789', 1, 1, 1, 'Door 7', 2, 61, 'shipped'),
('234567891', 2, 1, 1, 'Door 33', 4, 113, 'shipped'),
('345678911', 3, 1, 1, 'Door 19', 6, 99, 'shipped'),







COMMIT TRANSACTION;
