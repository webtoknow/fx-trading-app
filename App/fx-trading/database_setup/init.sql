
--create database fxtrading;

create table transactions (
  id bigserial primary key,
  username varchar(50) not null,
  primary_ccy varchar(20) not null,
  secondary_ccy varchar(20) not null,
  rate bigint not null,
  action varchar(20) not null,
  notional bigint not null,
  tenor varchar(20) not null,
  date timestamp not null default now()
);


--CREATE USER fxuser WITH ENCRYPTED PASSWORD 'password';
GRANT ALL PRIVILEGES ON DATABASE fxtrading TO fxuser;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO fxuser;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO fxuser;