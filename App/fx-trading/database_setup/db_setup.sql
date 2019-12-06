create database fxtrading;

\connect fxtrading;

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

CREATE USER fxuser WITH ENCRYPTED PASSWORD 'password';
GRANT ALL PRIVILEGES ON DATABASE fxtrading TO fxuser;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO fxuser;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO fxuser;

INSERT INTO public.transactions (id, username, primary_ccy, secondary_ccy, rate, action, notional, tenor, date) VALUES (nextval('transactions_id_seq'), 'ana_maria', 'EUR', 'USD', 18459, 'BUY', 10000, 'SP', '2018-11-13 20:56:11.59403');
INSERT INTO public.transactions (id, username, primary_ccy, secondary_ccy, rate, action, notional, tenor, date) VALUES (nextval('transactions_id_seq'), 'alexandru', 'USD', 'GBP', 11701, 'BUY', 15000, '1M', '2018-11-19 19:32:02.773998');
INSERT INTO public.transactions (id, username, primary_ccy, secondary_ccy, rate, action, notional, tenor, date) VALUES (nextval('transactions_id_seq'), 'mihaela', 'EUR', 'GBP', 10957, 'BUY', 100000, '1M', '2018-11-19 19:39:50.7525');
INSERT INTO public.transactions (id, username, primary_ccy, secondary_ccy, rate, action, notional, tenor, date) VALUES (nextval('transactions_id_seq'), 'andreea', 'EUR', 'GBP', 14298, 'BUY', 22000, '1M', '2018-11-19 19:40:43.935658');
INSERT INTO public.transactions (id, username, primary_ccy, secondary_ccy, rate, action, notional, tenor, date) VALUES (nextval('transactions_id_seq'), 'elena', 'EUR', 'GBP', 10640, 'BUY', 50000, '1M', '2018-11-19 19:41:15.177145');
INSERT INTO public.transactions (id, username, primary_ccy, secondary_ccy, rate, action, notional, tenor, date) VALUES (nextval('transactions_id_seq'), 'adrian', 'USD', 'RON', 58966, 'BUY', 35000, 'SP', '2018-11-19 19:41:49.361552');
INSERT INTO public.transactions (id, username, primary_ccy, secondary_ccy, rate, action, notional, tenor, date) VALUES (nextval('transactions_id_seq'), 'andrei', 'GBP', 'RON', 57622, 'BUY', 10000, '3M', '2018-11-19 20:10:03.621845');
INSERT INTO public.transactions (id, username, primary_ccy, secondary_ccy, rate, action, notional, tenor, date) VALUES (nextval('transactions_id_seq'), 'alexandra', 'EUR', 'USD', 13345, 'BUY', 25000, '1M', '2018-11-19 20:11:50.19023');
