create table if not exists localizations
(
    id        bigint generated always as identity primary key,
    city      varchar(255),
    region    varchar(255),
    country   varchar(255),
    latitude  real not null,
    longitude real not null
);

create table if not exists weather (
    id bigserial primary key,
    localization_id bigint,
    expiry_time timestamp without time zone,
    message varchar(255),
    main_info varchar(255),
    description text,
    temp real,
    feels_like real,
    pressure real,
    humidity real,
    wind_speed real,
    wind_deg real,
    clouds_all real,
    weather_date date,
    timezone varchar(255),
    constraint fk_weather_localizations foreign key (localization_id)
        references localizations (id) on delete set null
);