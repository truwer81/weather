create table localizations
(
    id        bigint generated always as identity primary key,
    city      varchar(255),
    region    varchar(255),
    country   varchar(255),
    latitude  real not null,
    longitude real not null
);