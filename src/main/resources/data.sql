INSERT INTO USER (name, is_alive, password, role)
VALUES ('The Bad Guy', true, '123', 'USER');

INSERT INTO USER (name, is_alive, password, role)
VALUES ('Muhammad Muzammil', true, '123', 'ADMIN');

INSERT INTO USER (name, is_alive, password, role)
VALUES ('Light Yagami', true, '123', 'USER');

INSERT INTO DEATH_NOTE (name, owner_id)
VALUES ('Ryukkk Notebook',
        SELECT id
        FROM USER
        WHERE name = 'Light Yagami');
