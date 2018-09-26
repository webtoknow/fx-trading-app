create table transactions (
  transactionId bigserial primary key,
  username varchar(50) not null,
  primaryCCy varchar(20) not null,
  secondaryCcy varchar(20) not null,
  rate bigint not null,
  action varchar(20) not null,
  notional bigint not null,
  tenor varchar(20) not null,
  date timestamp not null default now()
);