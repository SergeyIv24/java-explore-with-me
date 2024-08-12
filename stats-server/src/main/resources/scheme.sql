CREATE TABLE IF NOT EXISTS apps (
    app_id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    app_name VARCHAR NOT NULL UNIQUE REFERENCES statistics (app),

);

CREATE TABLE IF NOT EXISTS statistics (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    app BIGINT NOT NULL,
    uri VARCHAR NOT NULL,
    ip VARCHAR NOT NULL,
    timestamp TIMESTAMP NOT NULL
);