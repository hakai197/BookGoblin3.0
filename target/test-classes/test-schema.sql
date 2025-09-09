-- database m2_final_project
BEGIN TRANSACTION;

-- *************************************************************************************************
-- Drop all db objects in the proper order
-- *************************************************************************************************
DROP TABLE IF EXISTS users;

-- *************************************************************************************************
-- Create the tables and constraints
-- *************************************************************************************************

--users (name is pluralized because 'user' is a SQL keyword)
CREATE TABLE users (
	user_id SERIAL,
	username varchar(50) NOT NULL UNIQUE,
	password_hash varchar(200) NOT NULL,
	role varchar(50) NOT NULL,
	CONSTRAINT PK_user PRIMARY KEY (user_id)
);
CREATE TABLE Shipper (
    shipper_id SERIAL,
    name VARCHAR(100) NOT NULL,
    address VARCHAR(100) NOT NULL,
    city VARCHAR(50) NOT NULL,
    state VARCHAR(2) NOT NULL,
    zip_code VARCHAR(10) NOT NULL,
    CONSTRAINT PK_Shipper PRIMARY KEY (shipper_id)
);

CREATE TABLE Trailer (
    trailer_id SERIAL,
    trailer_number VARCHAR(20) NOT NULL,
    trailer_type VARCHAR(50) NOT NULL,
    shipper_id INTEGER NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'Unassigned',
    CONSTRAINT PK_Trailer PRIMARY KEY (trailer_id),
    CONSTRAINT FK_Trailer_Shipper FOREIGN KEY (shipper_id) REFERENCES Shipper(shipper_id) ON DELETE CASCADE,
    CONSTRAINT UQ_Trailer_number UNIQUE (trailer_number)
);

CREATE TABLE Customer (
    customer_id SERIAL,
    name VARCHAR(100) NOT NULL,
    address VARCHAR(100) NOT NULL,
    city VARCHAR(50) NOT NULL,
    state VARCHAR(2) NOT NULL,
    zip_code VARCHAR(10) NOT NULL,
    CONSTRAINT PK_Customer PRIMARY KEY (customer_id)
);

CREATE TABLE Unloader (
    employee_id SERIAL,
    name VARCHAR(100) NOT NULL,
    shift VARCHAR(100) NOT NULL,
    employee_number VARCHAR(100) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'Unassigned',
    CONSTRAINT PK_Unloader PRIMARY KEY (employee_id),
    CONSTRAINT UQ_Employee_number UNIQUE (employee_number)
);

CREATE TABLE Order_Number (
    order_id SERIAL,
    order_number VARCHAR(20) NOT NULL UNIQUE,
    customer_id INTEGER NOT NULL,
    shipper_id INTEGER NOT NULL,
    trailer_id INTEGER NOT NULL,
    door_number VARCHAR(10) NOT NULL,
    handling_unit INTEGER NOT NULL,
    weight INTEGER NOT NULL,
    status VARCHAR (20) NOT NULL,
    CONSTRAINT PK_Order_Number PRIMARY KEY (order_id),
    CONSTRAINT FK_Order_Number_Customer FOREIGN KEY (customer_id) REFERENCES Customer(customer_id) ON DELETE CASCADE,
    CONSTRAINT FK_Order_Number_Shipper FOREIGN KEY (shipper_id) REFERENCES Shipper(shipper_id) ON DELETE CASCADE,
    CONSTRAINT FK_Order_Number_Trailer FOREIGN KEY (trailer_id) REFERENCES Trailer(trailer_id) ON DELETE CASCADE
);

COMMIT TRANSACTION;
