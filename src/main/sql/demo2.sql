\c eve_dev_db

CREATE TABLE IF NOT EXISTS mytable (
    id SERIAL PRIMARY KEY,
    name TEXT
);

INSERT INTO mytable (name) VALUES (
    'JARED'
);