create database users;

\connect users;

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

CREATE ROLE new_user LOGIN PASSWORD 'new_user_password';
REVOKE CONNECT ON DATABASE users  FROM PUBLIC;
GRANT USAGE ON SCHEMA public TO new_user;
GRANT ALL PRIVILEGES ON DATABASE users TO new_user;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO new_user;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO new_user;

INSERT INTO user_table (user_name , email , password) VALUES ('user', 'user@test.com', 'password');