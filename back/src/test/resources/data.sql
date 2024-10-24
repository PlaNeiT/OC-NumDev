INSERT INTO TEACHERS (first_name, last_name)
VALUES ('Margot', 'DELAHAYE'),
       ('Helene', 'THIERCELIN');


INSERT INTO USERS (first_name, last_name, admin, email, password)
VALUES
    ('Yoga', 'Admin', true, 'yoga@studio.com', '$2a$10$.Hsa/ZjUVaHqi0tp9xieMeewrnZxrZ5pQRzddUXE/WjDu2ZThe6Iq'),
    ('John', 'Doe', false, 'john.doe@mail.me', '$2a$10$abcde12345');

INSERT INTO SESSIONS (name, description, date, teacher_id)
VALUES ('Yoga Session', 'A relaxing yoga session', NOW(), 1);

INSERT INTO PARTICIPATE (user_id, session_id)
VALUES (2, 1);
