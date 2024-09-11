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