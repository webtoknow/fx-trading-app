create table transactions (
  transactionId bigserial primary key,
  username varchar(50),
  primaryCCy varchar(20),
  secondaryCcy varchar(20),
  rate bigint,
  action varchar(20),
  notional bigint,
  tenor varchar(20),
  date timestamp
);