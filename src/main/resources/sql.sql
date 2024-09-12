CREATE TABLE employee (
    employee_id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    surname VARCHAR(50) NOT NULL,
    email VARCHAR(50) NOT NULL,
    phone VARCHAR(50) NOT NULL
);

CREATE TABLE visit (
    visit_id SERIAL PRIMARY KEY,
    dr_id INT NOT NULL,
    date_and_time DATETIME NOT NULL,
    confirmed BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (dr_id) REFERENCES doctor(dr_id),
    CONSTRAINT uniq_visit_dr UNIQUE (dr_id, date_and_time)
);

CREATE TABLE shifts (
    shift_id SERIAL PRIMARY KEY,
    shift_name VARCHAR(50),
    start_time TIME,
    end_time TIME
);


CREATE TABLE schedule (
    schedule_id SERIAL PRIMARY KEY,
    employee_id INT REFERENCES employee(employee_id),
    shift_id INT REFERENCES shifts(shift_id),
    work_date DATE
);
INSERT INTO shifts (shift_name, start_time, end_time)
VALUES
('morning', '07:00', '15:00'),
('afternoon', '11:00', '19:00'),
('evening', '14:00', '22:00');

INSERT INTO schedule (employee_id, shift_id, work_date)
VALUES
(2, 1, '2024-09-13'),
(6, 2, '2024-09-13'),
(7, 3, '2024-09-13'),
(2, 1, '2024-09-14'),
(6, 2, '2024-09-14'),
(7, 3, '2024-09-14');