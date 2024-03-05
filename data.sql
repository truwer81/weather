create table localizations
(
    id        bigint generated always as identity primary key,
    city      varchar(255),
    region    varchar(255),
    country   varchar(255),
    latitude  real not null,
    longitude real not null
);

CREATE TABLE weather (
    id BIGSERIAL PRIMARY KEY,
    localization_id BIGINT,
    expiry_time TIMESTAMP WITHOUT TIME ZONE,
    message VARCHAR(255),
    main_info VARCHAR(255),
    description TEXT,
    temp REAL,
    feels_like REAL,
    pressure REAL,
    humidity REAL,
    wind_speed REAL,
    wind_deg REAL,
    clouds_all REAL,
    weather_date DATE,
    timezone VARCHAR(255),
    CONSTRAINT fk_weather_localizations FOREIGN KEY (localization_id)
        REFERENCES localizations (id) ON DELETE SET NULL
);