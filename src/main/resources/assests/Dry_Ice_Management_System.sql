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
                          customer_id VARCHAR(5) PRIMARY KEY,
                          name VARCHAR(100) NOT NULL,
                          nic VARCHAR(12) UNIQUE,
                          email VARCHAR(50) UNIQUE NOT NULL,
                          contact VARCHAR(20) UNIQUE NOT NULL,
                          address VARCHAR(100) NOT NULL,
                          description VARCHAR(100)
);

INSERT INTO Customer (customer_id, name, nic, email, contact, address, description) VALUES
                                                                                        ('C001', 'New Customer', '123456789112', 'customer@gmail.com', '0766631215', 'Galle', 'Regular buyer'),
                                                                                        ('C002', 'Nimal Perera', '901234567V', 'nimalp@gmail.com', '0771234567', 'Colombo', 'Wholesale customer'),
                                                                                        ('C003', 'Kamal Silva', '911234568V', 'kamals@gmail.com', '0719876543', 'Kandy', 'Retail order'),
                                                                                        ('C004', 'Tharindu Wijesinghe', '921234569V', 'tharinduw@gmail.com', '0751122334', 'Matara', 'Buys weekly'),
                                                                                        ('C005', 'Saman Kumara', '931234570V', 'samank@gmail.com', '0782233445', 'Anuradhapura', 'Bulk orders'),
                                                                                        ('C006', 'Dilani Fernando', '941234571V', 'dilanif@gmail.com', '0723344556', 'Negombo', 'Monthly customer'),
                                                                                        ('C007', 'Ishara Jayasuriya', '951234572V', 'isharaj@gmail.com', '0764455667', 'Kurunegala', 'Prefers evening delivery'),
                                                                                        ('C008', 'Ruwan Hettiarachchi', '961234573V', 'ruwanh@gmail.com', '0705566778', 'Badulla', 'Cash payments'),
                                                                                        ('C009', 'Harsha Rajapaksha', '971234574V', 'harshar@gmail.com', '0746677889', 'Nuwara Eliya', 'Occasional orders'),
                                                                                        ('C010', 'Thisara Madushanka', '981234575V', 'thisaram@gmail.com', '0737788990', 'Ratnapura', 'Follows up often');


-- ========================
--     Orders Table
-- ========================
CREATE TABLE Orders (
                        order_id INT PRIMARY KEY,
                        customer_id VARCHAR(5) NOT NULL,
                        order_date DATE NOT NULL,
                        order_time TIME,
                        description VARCHAR(100),
                        vehicle_number VARCHAR(50),
                        total_amount DECIMAL(10,2) NOT NULL,
                        FOREIGN KEY (customer_id) REFERENCES Customer(customer_id) ON DELETE CASCADE
);

-- ========================
--     Product Table
-- ========================
CREATE TABLE Product (
                         product_id VARCHAR(4) PRIMARY KEY,
                         name VARCHAR(100) NOT NULL,
                         weight DECIMAL(10,2) NOT NULL CHECK (weight > 0),
                         price_per_unit DECIMAL(10,2) NOT NULL CHECK (price_per_unit > 0)
);


INSERT INTO Product (product_id, name, weight, price_per_unit) VALUES
                                                                   ('I001', 'Block Ice', 50, 530.00),
                                                                   ('I002', 'Crushed Ice', 50, 560.00),
                                                                   ('I003', 'Flake Ice', 25, 280.00),
                                                                   ('I004', 'Crystal Ice', 10, 480.00);

-- ========================
--     Stock Table
-- ========================
CREATE TABLE Stock (
                       stock_id INT PRIMARY KEY AUTO_INCREMENT,
                       product_id VARCHAR(4) NOT NULL,
                       product_name VARCHAR(100) NOT NULL,
                       stock_quantity INT NOT NULL CHECK (stock_quantity >= 0),
                       stock_date DATE NOT NULL,
                       stock_time TIME NOT NULL,
                       FOREIGN KEY (product_id) REFERENCES Product(product_id) ON DELETE CASCADE
);


-- ========================
--     Order Details Table
-- ========================
CREATE TABLE Order_Details (
                               order_id INT NOT NULL,
                               product_id VARCHAR(4) NOT NULL,
                               quantity INT NOT NULL CHECK (quantity > 0),
                               unit_price DECIMAL(10,2) NOT NULL CHECK (unit_price > 0),
                               discount DECIMAL(10,2),
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
                          vehicle_id INT NOT NULL,
                          FOREIGN KEY (order_id) REFERENCES Orders(order_id) ON DELETE CASCADE,
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
                            in_time TIME DEFAULT NULL,
                            out_time TIME DEFAULT NULL,
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
                        status ENUM('PENDING','COMPLETED','CANCELLED') DEFAULT 'PENDING',
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
                          supplier_id VARCHAR(5) PRIMARY KEY,
                          name VARCHAR(100) NOT NULL,
                          nic VARCHAR(12) UNIQUE,
                          contact VARCHAR(20) UNIQUE NOT NULL,
                          email VARCHAR(50) UNIQUE NOT NULL,
                          address VARCHAR(100) NOT NULL
);

-- ========================
--     Raw Materials Table
-- ========================
CREATE TABLE Raw_Materials (
                               material_id INT PRIMARY KEY,
                               supplier_id VARCHAR(5) NOT NULL ,
                               name VARCHAR(100) NOT NULL,
                               unit_type ENUM('Kg', 'Litre', 'Unit') NOT NULL,
                               unit_cost DECIMAL(10,2) NOT NULL CHECK (unit_cost > 0),
                               lastUpdate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                               quantity_available INT NOT NULL CHECK (quantity_available >= 0),
                               FOREIGN KEY (supplier_id) REFERENCES Supplier(supplier_id) ON DELETE CASCADE

);
CREATE TABLE Inventory_Cart (
                                cart_id INT AUTO_INCREMENT PRIMARY KEY,
                                supplier_id VARCHAR(10) NOT NULL,
                                material_id INT NOT NULL,
                                name VARCHAR(100) NOT NULL,
                                unit_type ENUM('Kg', 'Litre', 'Unit') NOT NULL,
                                unit_price DECIMAL(10, 2) NOT NULL,
                                quantity INT NOT NULL,
                                total DECIMAL(10, 2) GENERATED ALWAYS AS (unit_price * quantity) STORED,

                                FOREIGN KEY (supplier_id) REFERENCES Supplier(supplier_id)ON DELETE CASCADE,
                                FOREIGN KEY (material_id) REFERENCES Raw_Materials(material_id)ON DELETE CASCADE
);


INSERT INTO Supplier (supplier_id, name, nic, contact, email, address) VALUES
                                                                           ('S001', 'ABC Traders', '123456789V', '0771234567', 'abc@traders.com', 'Colombo'),
                                                                           ('S002', 'XYZ Supplies', '987654321V', '0779876543', 'xyz@supplies.com', 'Kandy'),
                                                                           ('S003', 'Global Distributors', '456123789V', '0761234567', 'global@distributors.com', 'Galle'),
                                                                           ('S004', 'Mega Mart', '321654987V', '0714567890', 'mega@mart.com', 'Negombo'),
                                                                           ('S005', 'Industrial Hub', '741852963V', '0756547891', 'hub@industrial.com', 'Matara');


INSERT INTO Raw_Materials (material_id, supplier_id, name, unit_type, unit_cost, quantity_available) VALUES
                                                                                                         (1, 'S001', 'Glouse', 'Unit', 40.00, 20),
                                                                                                         (2, 'S001', 'Boat', 'Unit', 240.00, 4),
                                                                                                         (3, 'S002', 'Jacket', 'Unit', 240.00, 10),
                                                                                                         (4, 'S003', 'Paper Roll', 'Unit', 50.00, 8),
                                                                                                         (5, 'S002', 'Medicine', 'Unit', 5000.00, 1),
                                                                                                         (6, 'S003', 'Welfare Kit', 'Unit', 20000.00, 1),
                                                                                                         (7, 'S003', 'Document Pack', 'Unit', 5000.00, 1),
                                                                                                         (8, 'S004', 'Electricity Bill', 'Unit', 4000000.00, 1),
                                                                                                         (9, 'S004', 'Water Bill', 'Unit', 20000.00, 1),
                                                                                                         (10, 'S001', 'Salt', 'Kg', 50.00, 100),
                                                                                                         (11, 'S005', 'Oil', 'Litre', 300.00, 20),
                                                                                                         (12, 'S005', 'Ammonia Cylinder', 'Unit', 15000.00, 1),
                                                                                                         (13, 'S001', 'Grease', 'Kg', 800.00, 5),
                                                                                                         (14, 'S002', 'Vehicle Diesel', 'Litre', 350.00, 50000),
                                                                                                         (15, 'S005', 'Vehicle Service', 'Unit', 90000.00, 3);



-- ========================
--     Purchase Table
-- ========================
CREATE TABLE Purchase (
                          purchase_id INT PRIMARY KEY AUTO_INCREMENT,
                          supplier_id VARCHAR(5) NOT NULL ,
                          material_id INT NOT NULL,
                          purchase_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          quantity INT NOT NULL CHECK (quantity > 0),
                          FOREIGN KEY (supplier_id) REFERENCES Supplier(supplier_id) ON DELETE CASCADE,
                          FOREIGN KEY (material_id) REFERENCES Raw_Materials(material_id) ON DELETE CASCADE
);

-- ========================
--     Booking Table
-- ========================
CREATE TABLE Booking (
                         booking_id INT PRIMARY KEY AUTO_INCREMENT,
                         customer_id VARCHAR(5) NOT NULL,
                         product_id VARCHAR(4) NOT NULL,
                         request_date DATE NOT NULL,
                         request_time TIME NOT NULL,
                         quantity INT NOT NULL CHECK (quantity > 0),
                         status ENUM('Pending', 'Confirmed', 'Cancelled') DEFAULT 'Pending',

                         FOREIGN KEY (customer_id) REFERENCES Customer(customer_id) ON DELETE CASCADE,
                         FOREIGN KEY (product_id) REFERENCES Product(product_id) ON DELETE CASCADE
);



-- ===============================
--     SMS & Email Notifications
-- ===============================
CREATE TABLE SMS_Email (
                           notification_id INT PRIMARY KEY AUTO_INCREMENT,
                           customer_id VARCHAR(5) NOT NULL,
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
                           product_id VARCHAR(4) NOT NULL,
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

-- ========================
--     PendingOrder Table
-- ========================
CREATE TABLE PendingOrder (
                              pending_order_id INT AUTO_INCREMENT PRIMARY KEY,
                              order_id INT NOT NULL,
                              customer_id VARCHAR(5) NOT NULL,
                              product_id VARCHAR(4) NOT NULL,
                              quantity INT NOT NULL CHECK (quantity > 0),
                              request_time DATETIME NOT NULL,
                              status ENUM('PENDING','COMPLETED','CANCELLED') DEFAULT 'PENDING',
                              FOREIGN KEY (order_id) REFERENCES Orders(order_id),
                              FOREIGN KEY (customer_id) REFERENCES Customer(customer_id),
                              FOREIGN KEY (product_id) REFERENCES Product(product_id)
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
      ('E001', 'Kalana Methsara', '200227703653', '0778373015', 'kalanamethsara53@gmail.com', 'Manager',
       '140/C, Hapugala, Galle', '2023-06-15', '2002-10-03', 'Male',
       '123456789012', 'Commercial - Galle Branch', 'B5842235'),

      ('E002', 'Akila Abeyasekara', '199915400598', '0716689089', 'akilaabeysekara99@gmail.com', 'Supervisor',
       '456/B, Kithulampitiya, Galle', '2022-02-01', '1999-06-02', 'Male',
       '987654321098', 'BOC - Karapitiya Branch', 'A8765432'),

      ('E003', 'Savindu Navanjana', '199012345678', '0713456789', 'savindunavanjana08@gmail.com', 'Supervisor',
       '789/C, High Road, Galle', '2021-11-30', '1990-03-18', 'Male',
       '112233445566', 'Sampath Bank - Galle', 'D1234568'),

      ('E004', 'Chandana Jayasinghe', '198704567890', '0729876543', 'chandana@example.com', 'Cashier',
       '101/D, Kings Way, Negombo', '2020-08-25', '1987-07-10', 'Male',
       '223344556677', 'Commercial Bank - Negombo', 'M2345671'),

      ('E005', 'Saman Wijesinghe', '198206789012', '0788765432', 'saman@example.com', 'Driver',
       '321/E, Lakeview Avenue, Anuradhapura', '2021-01-15', '1982-12-25', 'Male',
       '334455667788', 'People’s Bank - Anuradhapura', 'C2345679'),

      ('E006', 'Kavinda Perera', '200103567890', '0701122334', 'kavinda@example.com', 'Driver',
       '654/F, Colombo Road, Matara', '2023-03-11', '2001-11-02', 'Male',
       '445566778899', 'BOC - Matara Branch', 'B2345670'),

      ('E007', 'Dinithi Fernando', '199703456789', '0712233445', 'dinithi@example.com', 'Worker',
       '987/G, Sunset Boulevard, Jaffna', '2020-07-19', '1997-01-12', 'Female',
       '556677889900', 'HNB - Jaffna Branch', 'C8765431'),

      ('E008', 'Ravindu Kumara', '199912345678', '0756789123', 'ravindu@example.com', 'Worker',
       '345/H, Queens Road, Kurunegala', '2022-05-10', '1999-09-25', 'Male',
       '667788990011', 'Sampath Bank - Kurunegala', 'A2345678'),

      ('E009', 'Shanika Gamage', '199102345678', '0772345678', 'shanika@example.com', 'Worker',
       '654/I, Horizon Street, Kegalle', '2021-09-05', '1991-03-20', 'Female',
       '778899001122', 'Commercial Bank - Kegalle', 'D3456789'),

      ('E010', 'Ruwan Tharanga', '199412345678', '0783456789', 'ruwan@example.com', 'Worker',
       '321/J, Old Town, Puttalam', '2020-12-01', '1994-07-14', 'Male',
       '889900112233', 'People’s Bank - Puttalam', 'M5678901'),

      ('E011', 'Thilini Perera', '199812345678', '0703344556', 'thilini@example.com', 'Worker',
       '12/A, Temple Road, Polonnaruwa', '2023-01-10', '1998-04-22', 'Female',
       '990011223344', 'BOC - Polonnaruwa Branch', 'T1234567'),

      ('E012', 'Isuru Madushan', '199605432189', '0714455667', 'isuru@example.com', 'Worker',
       '45/B, Park Lane, Gampaha', '2021-06-21', '1996-11-08', 'Male',
       '100200300400', 'HNB - Gampaha Branch', 'I2345678'),

      ('E013', 'Nimasha Senavirathna', '200003210987', '0725566778', 'nimasha@example.com', 'Worker',
       '78/C, Station Road, Matale', '2022-11-05', '2000-01-19', 'Female',
       '200300400500', 'Commercial Bank - Matale', 'N3456789'),

      ('E014', 'Kasun Jayalath', '199403219876', '0756677889', 'kasun@example.com', 'Worker',
       '89/D, Riverside, Badulla', '2020-04-18', '1994-06-27', 'Male',
       '300400500600', 'People’s Bank - Badulla', 'K4567890'),

      ('E015', 'Udari Rathnayake', '199703456123', '0767788990', 'udari@example.com', 'Worker',
       '90/E, Palm Grove, Nuwara Eliya', '2023-09-09', '1997-08-14', 'Female',
       '400500600700', 'Sampath Bank - Nuwara Eliya', 'U5678901');


INSERT INTO Booking (customer_id, product_id, request_date, request_time, quantity, status) VALUES
                                                                                                      ('C001', 'I001', '2025-05-01', '09:30:00', 50, 'Pending'),
                                                                                                      ('C002', 'I002', '2025-05-02', '10:15:00', 30, 'Confirmed'),
                                                                                                      ('C003', 'I003', '2025-06-03', '14:00:00', 40, 'Cancelled'),
                                                                                                      ('C004', 'I004', '2025-05-04', '08:45:00', 20, 'Pending'),
                                                                                                      ('C005', 'I004', '2025-07-15', '13:20:00', 60, 'Confirmed'),
                                                                                                      ('C006', 'I001', '2025-05-06', '11:10:00', 25, 'Pending'),
                                                                                                      ('C007', 'I002', '2025-04-07', '15:00:00', 35, 'Pending'),
                                                                                                      ('C008', 'I003', '2025-05-08', '09:00:00', 45, 'Confirmed'),
                                                                                                      ('C009', 'I004', '2025-07-09', '16:30:00', 70, 'Pending'),
                                                                                                      ('C010', 'I001', '2025-05-20', '07:30:00', 15, 'Cancelled');


-- Sample insert statements for the Stock table

-- Insert sample salary data
INSERT INTO Salary (employee_id, basic_amount, bonus, deduction, net_amount, pay_month, pay_year)
VALUES
    ('E001', 50000.00, 5000.00, 2000.00, 53000.00, 5, 2025),
    ('E002', 45000.00, 3000.00, 1500.00, 46500.00, 5, 2025),
    ('E003', 60000.00, 7000.00, 2500.00, 64500.00, 5, 2025),
    ('E004', 40000.00, 2000.00, 1000.00, 41000.00, 5, 2025),
    ('E005', 55000.00, 4500.00, 3000.00, 56500.00, 5, 2025),
    ('E006', 52000.00, 4000.00, 2000.00, 54000.00, 5, 2025),
    ('E007', 48000.00, 3500.00, 1800.00, 49700.00, 5, 2025),
    ('E008', 47000.00, 2000.00, 1200.00, 47800.00, 5, 2025),
    ('E009', 49000.00, 1000.00, 900.00, 49100.00, 5, 2025),
    ('E010', 51000.00, 3000.00, 1700.00, 52300.00, 5, 2025);
