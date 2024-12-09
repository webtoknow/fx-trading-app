
CREATE   TABLE user_table (
    user_id SERIAL PRIMARY KEY,
    user_name varchar(255),
    email varchar(255),
    password varchar(255)
);


CREATE  TABLE user_login (
    user_login_id SERIAL PRIMARY KEY,
    user_id INTEGER REFERENCES user_table(user_id),
    token varchar(255),
    token_expire_time varchar(255)
);


INSERT INTO user_table (user_name , email , password)
VALUES ('razvan', 'razvan@gmail.com', 'razvanPassword');

INSERT INTO user_table (user_name , email , password)
VALUES('mihai', 'mihai@gmail.com', 'mihaiPassword');


INSERT INTO user_table (user_name , email , password)
VALUES('andrei', 'andrei@gmail.com','AndreisPassword');
