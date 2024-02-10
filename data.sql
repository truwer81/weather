CREATE TABLE animal(id BIGINT NOT NULL, name VARCHAR(50), age INT, PRIMARY KEY (id));
create sequence animal_seq start with 4 increment by 50;
INSERT INTO animal VALUES (1, 'Reksio', 5), (2, 'Mruczek', 4), (3, 'Benio', 10);
ALTER TABLE animal ADD COLUMN created_date varchar(50);