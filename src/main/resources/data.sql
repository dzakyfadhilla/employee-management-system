-- Insert sample branches
INSERT INTO branches (code, name, address, phone_number, created_at, updated_at) VALUES
('HO', 'Head Office', 'Jl. Sudirman No. 123, Jakarta Pusat', '021-12345678', NOW(), NOW()),
('JKT01', 'Jakarta Branch 1', 'Jl. Gatot Subroto No. 456, Jakarta Selatan', '021-87654321', NOW(), NOW()),
('BDG01', 'Bandung Branch 1', 'Jl. Asia Afrika No. 789, Bandung', '022-11223344', NOW(), NOW()),
('SBY01', 'Surabaya Branch 1', 'Jl. Basuki Rachmat No. 321, Surabaya', '031-55667788', NOW(), NOW());

-- Insert sample employees
INSERT INTO employees (employee_code, first_name, last_name, email, phone_number, hire_date, position, address, branch_id, created_at, updated_at) VALUES
('EMP001', 'John', 'Doe', 'john.doe@company.com', '081234567890', '2023-01-15', 'Manager', 'Jl. Kemang No. 100, Jakarta', 1, NOW(), NOW()),
('EMP002', 'Jane', 'Smith', 'jane.smith@company.com', '081234567891', '2023-02-20', 'Senior Developer', 'Jl. Menteng No. 200, Jakarta', 1, NOW(), NOW()),
('EMP003', 'Ahmad', 'Wijaya', 'ahmad.wijaya@company.com', '081234567892', '2023-03-10', 'HR Specialist', 'Jl. Blok M No. 300, Jakarta', 2, NOW(), NOW()),
('EMP004', 'Siti', 'Nurhaliza', 'siti.nurhaliza@company.com', '081234567893', '2023-04-05', 'Accountant', 'Jl. Kemang No. 400, Jakarta', 2, NOW(), NOW()),
('EMP005', 'Budi', 'Santoso', 'budi.santoso@company.com', '081234567894', '2023-05-12', 'Sales Representative', 'Jl. Dago No. 500, Bandung', 3, NOW(), NOW()),
('EMP006', 'Rina', 'Pratiwi', 'rina.pratiwi@company.com', '081234567895', '2023-06-18', 'Marketing Specialist', 'Jl. Cihampelas No. 600, Bandung', 3, NOW(), NOW()),
('EMP007', 'Agus', 'Setiawan', 'agus.setiawan@company.com', '081234567896', '2023-07-25', 'Operations Manager', 'Jl. Darmo No. 700, Surabaya', 4, NOW(), NOW()),
('EMP008', 'Dewi', 'Lestari', 'dewi.lestari@company.com', '081234567897', '2023-08-30', 'Customer Service', 'Jl. Gubeng No. 800, Surabaya', 4, NOW(), NOW());
