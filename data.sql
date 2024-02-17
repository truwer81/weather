POSTGRES

DROP TABLE IF EXISTS "City";
DROP TABLE IF EXISTS "CheckWeather";


CREATE TABLE City (
  cityName varchar(25) PRIMARY KEY,
  countryCode char(2) NOT NULL DEFAULT ''
);

CREATE TABLE CheckWeather (
  id serial PRIMARY KEY,
  cityName varchar(25),
  countryCode char(2) NOT NULL DEFAULT '',
  description varchar(45) NOT NULL DEFAULT '',
  temp real NOT NULL DEFAULT 0,
  feels_like real DEFAULT 0,
  temp_min real DEFAULT 0,
  temp_max real DEFAULT 0,
  pressure real DEFAULT 0,
  humidity real DEFAULT 0,
  windSpeed real DEFAULT 0,
  clouds_all real DEFAULT 0,
  forecastTimestamp TIMESTAMP,
  CONSTRAINT fk_city FOREIGN KEY (cityName) REFERENCES City(cityName)
);

SELECT * FROM City;
SELECT * FROM CheckWeather;