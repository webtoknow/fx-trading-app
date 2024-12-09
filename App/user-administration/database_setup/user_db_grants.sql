psql -U postgres  -d name


psql -U postgres -l


// show all users
psql -U postgres
\du


psql -U postgres

\c users

CREATE ROLE new_user LOGIN PASSWORD 'new_user_password';

REVOKE CONNECT ON DATABASE users  FROM PUBLIC;
GRANT CONNECT on DATABASE users  TO new_user;

GRANT USAGE ON SCHEMA public TO new_user;

GRANT ALL PRIVILEGES ON DATABASE users TO new_user;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO new_user;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO new_user;
GRANT USAGE, SELECT ON ALL SEQUENCES IN SCHEMA public TO new_user;
GRANT SELECT ON ALL TABLES IN SCHEMA public TO new_user;

psql -U new_user -d users

GRANT ALL ON TABLE public.users TO "new_user";

GRANT "postgres" TO "User1";
