DROP DATABASE IF EXISTS Dry_Ice_Management_System;
CREATE DATABASE Dry_Ice_Management_System;
USE Dry_Ice_Management_System;

-- ========================
--      User Table
-- ========================
CREATE TABLE User (
                      userName VARCHAR(20) PRIMARY KEY,
                      password VARCHAR(20) NOT NULL,
                      name VARCHAR(50) NOT NULL,
                      email VARCHAR(50) NOT NULL,
                      role VARCHAR(20) NOT NULL
);

INSERT INTO User VALUES
                     ('Admin', '#00ADpw', 'Admin Manager','lankaice1@gmail.com', 'Admin'),
                     ('Kalana', '#00MKpw', 'Kalana Methsara','kalanamethsara53@gmail.com', 'Manager'),
                     ('Akila', '#00AApw', 'Akila Abeysekara','akilaabeysekara99@gmail.com', 'Supervisor'),
                     ('Tharushika', '#00TDpw', 'Tharushika Devindi','tharushikadevindi754@gmail.com', 'Cashier');


-- ========================
--     Employee Table
-- ========================
CREATE TABLE Employee (
                          employee_id VARCHAR(5) PRIMARY KEY,
                          name VARCHAR(100) NOT NULL,
                          nic VARCHAR(12) UNIQUE,
                          contact VARCHAR(10) UNIQUE NOT NULL,
                          email VARCHAR(50) UNIQUE NOT NULL,
                          job_role ENUM('Worker', 'Supervisor', 'Cashier', 'Driver','Manager') NOT NULL,
                          address TEXT NOT NULL,
                          join_date DATE NOT NULL,
                          date_of_birth DATE NOT NULL,
                          gender ENUM('Male', 'Female', 'Other'),
                          bank_account_no VARCHAR(30),
                          bank_branch VARCHAR(100),
                          license_number VARCHAR(8) NULL
);


-- ========================
--     Vehicle Table
-- ========================
CREATE TABLE Vehicle (
                         vehicle_id INT PRIMARY KEY AUTO_INCREMENT,
                         vehicle_number VARCHAR(50) UNIQUE NOT NULL,
                         status ENUM('Active', 'Inactive', 'Under Repair') NOT NULL,
                         capacity INT NOT NULL CHECK (capacity > 0),
                         model VARCHAR(50) NOT NULL
);

INSERT INTO Vehicle (vehicle_number, status, capacity, model) VALUES
                                                                  ('LM 9181', 'Active', 250, 'Ashok Leyland'),
                                                                  ('LO 3988', 'Active', 250, 'Ashok Leyland'),
                                                                  ('DAH 7975', 'Active', 50, 'Mini Truck'),
                                                                  ('QY 7515', 'Active', 40, 'Dayun Three-wheeler'),
                                                                  ('LP-1802', 'Inactive', 300, 'Hino Ranger'),
                                                                  ('QD-4567', 'Under Repair', 300, 'Mitsubishi Canter'),
                                                                  ('AB-7890', 'Under Repair', 300, 'Isuzu Elf');

-- ========================
--     Customer Table
-- ========================
CREATE TABLE Customer (
                          customer_id INT PRIMARY KEY AUTO_INCREMENT,
                          name VARCHAR(100) NOT NULL,
                          nic VARCHAR(12) UNIQUE,
                          email VARCHAR(50) UNIQUE NOT NULL,
                          contact VARCHAR(20) UNIQUE NOT NULL,
                          address TEXT NOT NULL
);

INSERT INTO Customer (name, nic, email, contact, address) VALUES
    ('Customer', 'customer3015', 'customer@gmail.com', '0778373015', 'Galle');

-- ========================
--     Orders Table
-- ========================
CREATE TABLE Orders (
                        order_id INT PRIMARY KEY AUTO_INCREMENT,
                        customer_id INT NOT NULL,
                        order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        order_time TIME,
                        status ENUM('Pending', 'Completed', 'Cancelled') NOT NULL,
                        total_amount DECIMAL(10,2) NOT NULL,
                        FOREIGN KEY (customer_id) REFERENCES Customer(customer_id) ON DELETE CASCADE
);

-- ========================
--     Product Table
-- ========================
CREATE TABLE Product (
                         product_id INT PRIMARY KEY,
                         name VARCHAR(100) NOT NULL,
                         weight DECIMAL(10,2) NOT NULL CHECK (weight > 0),
                         price_per_unit DECIMAL(10,2) NOT NULL CHECK (price_per_unit > 0)
);

INSERT INTO Product (product_id, name, weight, price_per_unit) VALUES
                                                                   (5001, 'Block Ice(half)', 25, 280.00),
                                                                   (5002, 'Block Ice', 50, 560.00);

-- ========================
--     Stock Table
-- ========================
CREATE TABLE Stock (
                       stock_id INT PRIMARY KEY AUTO_INCREMENT,
                       product_id INT NOT NULL,
                       stock_quantity INT NOT NULL CHECK (stock_quantity >= 0),
                       last_update TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                       FOREIGN KEY (product_id) REFERENCES Product(product_id) ON DELETE CASCADE
);

-- ========================
--     Order Details Table
-- ========================
CREATE TABLE Order_Details (
                               orderDetails_id INT PRIMARY KEY AUTO_INCREMENT,
                               order_id INT NOT NULL,
                               product_id INT NOT NULL,
                               quantity INT NOT NULL CHECK (quantity > 0),
                               price DECIMAL(10,2) NOT NULL CHECK (price > 0),
                               request_time TIME,
                               FOREIGN KEY (order_id) REFERENCES Orders(order_id) ON DELETE CASCADE,
                               FOREIGN KEY (product_id) REFERENCES Product(product_id) ON DELETE CASCADE
);

-- ========================
--     Billing Table
-- ========================
CREATE TABLE Billing (
                         bill_id INT PRIMARY KEY AUTO_INCREMENT,
                         order_id INT NOT NULL,
                         billing_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         payment_status ENUM('Paid', 'Unpaid', 'Refunded') NOT NULL,
                         amount DECIMAL(10,2) NOT NULL CHECK (amount > 0),
                         FOREIGN KEY (order_id) REFERENCES Orders(order_id) ON DELETE CASCADE
);

-- ========================
--     Order Payment Table
-- ========================
CREATE TABLE Order_Payment (
                               payment_id VARCHAR(5) PRIMARY KEY ,
                               order_id INT NOT NULL,
                               bill_id INT NOT NULL,
                               payment_method ENUM('Cash', 'Card', 'Online') NOT NULL,
                               amount DECIMAL(10,2) NOT NULL CHECK (amount > 0),
                               payment_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                               status ENUM('Success', 'Failed', 'Pending') NOT NULL,
                               FOREIGN KEY (order_id) REFERENCES Orders(order_id) ON DELETE CASCADE,
                               FOREIGN KEY (bill_id) REFERENCES Billing(bill_id) ON DELETE CASCADE
);

-- ========================
--     Delivery Table
-- ========================
CREATE TABLE Delivery (
                          delivery_id INT PRIMARY KEY AUTO_INCREMENT,
                          order_id INT NOT NULL,
                          delivery_date DATE NOT NULL,
                          delivery_time TIME NOT NULL,
                          delivery_address TEXT NOT NULL,
                          delivery_status ENUM('Pending', 'Completed', 'Failed') NOT NULL,
                          driver_id VARCHAR(5) NOT NULL,
                          vehicle_id INT NOT NULL,
                          FOREIGN KEY (order_id) REFERENCES Orders(order_id) ON DELETE CASCADE,
                          FOREIGN KEY (driver_id) REFERENCES Employee(employee_id) ON DELETE CASCADE,
                          FOREIGN KEY (vehicle_id) REFERENCES Vehicle(vehicle_id) ON DELETE CASCADE
);

-- ========================
--     Attendance Table
-- ========================
CREATE TABLE Attendance (
                            attendance_id INT PRIMARY KEY AUTO_INCREMENT,
                            employee_id  VARCHAR(5) NOT NULL,
                            date DATE NOT NULL,
                            shift ENUM('Morning', 'Night') NOT NULL,
                            status ENUM('Present', 'Absent', 'Leave') NOT NULL,
                            UNIQUE (employee_id, date, shift),
                            FOREIGN KEY (employee_id) REFERENCES Employee(employee_id) ON DELETE CASCADE
);

-- ========================
--     Salary Table
-- ========================
CREATE TABLE Salary (
                        salary_id INT PRIMARY KEY AUTO_INCREMENT,
                        employee_id  VARCHAR(5) NOT NULL,
                        basic_amount DECIMAL(10,2) NOT NULL CHECK (basic_amount > 0),
                        bonus DECIMAL(10,2) DEFAULT 0 CHECK (bonus >= 0),
                        deduction DECIMAL(10,2) DEFAULT 0 CHECK (deduction >= 0),
                        net_amount DECIMAL(10,2) NOT NULL CHECK (net_amount >= 0),
                        pay_month INT NOT NULL CHECK (pay_month BETWEEN 1 AND 12),
                        pay_year INT NOT NULL CHECK (pay_year >= 2000),
                        FOREIGN KEY (employee_id) REFERENCES Employee(employee_id) ON DELETE CASCADE
);

-- ========================
--     Salary Payment Table
-- ========================
CREATE TABLE Salary_Payment (
                                payment_id INT PRIMARY KEY AUTO_INCREMENT,
                                salary_id INT NOT NULL,
                                paid_date DATE NOT NULL,
                                amount DECIMAL(10,2) NOT NULL CHECK (amount > 0),
                                status ENUM('Paid', 'Pending') NOT NULL,
                                FOREIGN KEY (salary_id) REFERENCES Salary(salary_id) ON DELETE CASCADE
);

-- ========================
--     Supplier Table
-- ========================
CREATE TABLE Supplier (
                          supplier_id INT PRIMARY KEY AUTO_INCREMENT,
                          name VARCHAR(100) NOT NULL,
                          nic VARCHAR(12) UNIQUE,
                          contact VARCHAR(20) UNIQUE NOT NULL,
                          email VARCHAR(50) UNIQUE NOT NULL,
                          address TEXT NOT NULL
);

-- ========================
--     Raw Materials Table
-- ========================
CREATE TABLE Raw_Materials (
                               material_id INT PRIMARY KEY AUTO_INCREMENT,
                               name VARCHAR(100) NOT NULL,
                               unit_type ENUM('Kg', 'Litre', 'Unit') NOT NULL,
                               unit_cost DECIMAL(10,2) NOT NULL CHECK (unit_cost > 0),
                               quantity_available INT NOT NULL CHECK (quantity_available >= 0)
);

-- ========================
--     Purchase Table
-- ========================
CREATE TABLE Purchase (
                          purchase_id INT PRIMARY KEY AUTO_INCREMENT,
                          supplier_id INT NOT NULL,
                          material_id INT NOT NULL,
                          purchase_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          quantity INT NOT NULL CHECK (quantity > 0),
                          FOREIGN KEY (supplier_id) REFERENCES Supplier(supplier_id) ON DELETE CASCADE,
                          FOREIGN KEY (material_id) REFERENCES Raw_Materials(material_id) ON DELETE CASCADE
);

-- ===============================
--     SMS & Email Notifications
-- ===============================
CREATE TABLE SMS_Email (
                           notification_id INT PRIMARY KEY AUTO_INCREMENT,
                           customer_id INT(5) NOT NULL,
                           order_id INT NOT NULL,
                           type ENUM('SMS', 'Email') NOT NULL,
                           sent_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                           FOREIGN KEY (customer_id) REFERENCES Customer(customer_id) ON DELETE CASCADE,
                           FOREIGN KEY (order_id) REFERENCES Orders(order_id) ON DELETE CASCADE
);

-- ========================
--     Transport Table
-- ========================
CREATE TABLE Transport (
                           transport_id INT PRIMARY KEY AUTO_INCREMENT,
                           product_id INT NOT NULL,
                           vehicle_id INT NOT NULL,
                           driver_id VARCHAR(5) NOT NULL,
                           transport_date DATE NOT NULL,
                           start_time TIME NOT NULL,
                           end_time TIME NOT NULL,
                           quantity INT NOT NULL CHECK (quantity > 0),
                           from_location VARCHAR(255) NOT NULL,
                           to_location VARCHAR(255) NOT NULL,
                           status ENUM('Scheduled', 'In Transit', 'Delivered', 'Cancelled') NOT NULL,
                           FOREIGN KEY (product_id) REFERENCES Product(product_id) ON DELETE CASCADE,
                           FOREIGN KEY (vehicle_id) REFERENCES Vehicle(vehicle_id) ON DELETE CASCADE,
                           FOREIGN KEY (driver_id) REFERENCES Employee(employee_id) ON DELETE CASCADE
);
-- -----------------------------------------------------------------------------------------------------------------

USE Dry_Ice_Management_System;
SHOW TABLES;
-- #626ED4
INSERT INTO Employee (
    employee_id, name, nic, contact, email, job_role,
    address, join_date, date_of_birth, gender,
    bank_account_no, bank_branch, license_number
) VALUES
      ('E001', 'Kalana Methsara', '200112345678', '0712345678', 'kalana@example.com', 'Supervisor',
       '123/A, Main Street, Colombo', '2023-06-15', '2001-05-22', 'Male',
       '123456789012', 'BOC - Colombo Branch', 'B1234567'),

      ('E002', 'Tharu Perera', '199505123456', '0771234567', 'tharu@example.com', 'Worker',
       '456/B, Second Lane, Kandy', '2022-02-01', '1995-08-15', 'Female',
       '987654321098', 'HNB - Kandy Branch', 'A8765432'),

      ('E003', 'Nadeesha Silva', '199012345678', '0713456789', 'nadeesha@example.com', 'Driver',
       '789/C, High Road, Galle', '2021-11-30', '1990-03-18', 'Female',
       '112233445566', 'Sampath Bank - Galle', 'D1234568'),

      ('E004', 'Chandana Jayasinghe', '198704567890', '0729876543', 'chandana@example.com', 'Manager',
       '101/D, Kings Way, Negombo', '2020-08-25', '1987-07-10', 'Male',
       '223344556677', 'Commercial Bank - Negombo', 'M2345671'),

      ('E005', 'Saman Wijesinghe', '198206789012', '0788765432', 'saman@example.com', 'Cashier',
       '321/E, Lakeview Avenue, Anuradhapura', '2021-01-15', '1982-12-25', 'Male',
       '334455667788', 'People’s Bank - Anuradhapura', 'C2345679'),

      ('E006', 'Kavinda Perera', '200103567890', '0701122334', 'kavinda@example.com', 'Supervisor',
       '654/F, Colombo Road, Matara', '2023-03-11', '2001-11-02', 'Male',
       '445566778899', 'BOC - Matara Branch', 'B2345670'),

      ('E007', 'Dinithi Fernando', '199703456789', '0712233445', 'dinithi@example.com', 'Cashier',
       '987/G, Sunset Boulevard, Jaffna', '2020-07-19', '1997-01-12', 'Female',
       '556677889900', 'HNB - Jaffna Branch', 'C8765431'),

      ('E008', 'Ravindu Kumara', '199912345678', '0756789123', 'ravindu@example.com', 'Worker',
       '345/H, Queens Road, Kurunegala', '2022-05-10', '1999-09-25', 'Male',
       '667788990011', 'Sampath Bank - Kurunegala', 'A2345678'),

      ('E009', 'Shanika Gamage', '199102345678', '0772345678', 'shanika@example.com', 'Driver',
       '654/I, Horizon Street, Kegalle', '2021-09-05', '1991-03-20', 'Female',
       '778899001122', 'Commercial Bank - Kegalle', 'D3456789'),

      ('E010', 'Ruwan Tharanga', '199412345678', '0783456789', 'ruwan@example.com', 'Manager',
       '321/J, Old Town, Puttalam', '2020-12-01', '1994-07-14', 'Male',
       '889900112233', 'People’s Bank - Puttalam', 'M5678901');
