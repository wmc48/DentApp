CREATE TABLE employee (
    employee_id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    surname VARCHAR(50) NOT NULL,
    email VARCHAR(50) NOT NULL,
    phone VARCHAR(50) NOT NULL
);
DROP TABLE IF EXISTS visit;
CREATE TABLE reservation (
    visit_id SERIAL PRIMARY KEY,
    employee_id INT NOT NULL,
    patient_id INT NOT NULL,
    date_and_time TIMESTAMP NOT NULL,
    confirmed BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (employee_id) REFERENCES employee(employee_id),
    FOREIGN KEY (patient_id) REFERENCES patient(patient_id),
    CONSTRAINT uniq_visit_dr UNIQUE (employee_id, date_and_time)
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

CREATE TABLE patient (
    patient_id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    surname VARCHAR(50) NOT NULL,
    pesel VARCHAR(11) NOT NULL,
    email VARCHAR(50) NOT NULL,
    phone VARCHAR(15) NOT NULL,
)


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

CREATE TABLE role (
    role_id SERIAL PRIMARY KEY,
    role_name VARCHAR(50) UNIQUE NOT NULL,
    description TEXT NOT NULL
);

CREATE TABLE users (
    user_id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    last_login TIMESTAMP WITH TIME ZONE,
	    is_active BOOLEAN DEFAULT FALSE
);

CREATE TABLE user_role
(
	user_id INT NOT NULL,
	role_id INT NOT NULL,
	PRIMARY KEY (user_id, role_id),
	CONSTRAINT fk_user_role_user
		FOREIGN KEY (user_id)
			REFERENCES users (user_id),
		CONSTRAINT fk_user_role_role
		FOREIGN KEY (role_id)
			REFERENCES role (role_id)
)

INSERT INTO role (role_name, description) VALUES
('admin', 'Administrator systemu z pełnymi uprawnieniami'),
('staff', 'Personel medyczny który zarządza rezerwacjami'),
('doctor', 'Lekarz z możliwością dodawania zaleceń i recept, podglądem rezerwacji'),
('patient', 'Pacjent zarządzający swoimi rezerwacjami');

insert into users (user_id, username, password_hash, is_active)
values(1, 'admin', '$2a$12$OH4ZfjYxC13YS6SsQtNnZuFVZfwIkjUP3Q.3RSh6aIWbu/wdfWkLi', true);
insert into users (user_id, username, password_hash, is_active)
values(2, 'test1', '$2a$12$YjTlUNUDgV6pQ0mSHrP7aOXyYsh0dI.93r8c5FNFZ1R9c3axp.eau', true);
insert into users (user_id, username, password_hash, is_active)
values(3, 'doc1', '$2a$12$YjTlUNUDgV6pQ0mSHrP7aOXyYsh0dI.93r8c5FNFZ1R9c3axp.eau', true);

insert into user_role (user_id, role_id) values(1, 1);
insert into user_role (user_id, role_id) values(2, 4);
insert into user_role (user_id, role_id) values(3, 3);

CREATE TABLE service (
    service_id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    price NUMERIC(10, 2) NOT NULL,
    notes TEXT,
    category_id INT,
    CONSTRAINT fk_category FOREIGN KEY (category_id) REFERENCES category(category_id)
);

CREATE TABLE service_employee (
    service_id INT NOT NULL,
    employee_id INT NOT NULL,
    PRIMARY KEY (service_id, employee_id),
    CONSTRAINT fk_service FOREIGN KEY (service_id) REFERENCES service(service_id),
    CONSTRAINT fk_employee FOREIGN KEY (employee_id) REFERENCES employee(employee_id)
);
CREATE TABLE category (
    category_id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    description TEXT
);