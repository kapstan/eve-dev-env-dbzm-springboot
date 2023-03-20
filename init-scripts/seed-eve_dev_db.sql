-- connect to development database
\c eve_dev_db;

-- create the uuid-ossp extension if it
-- doesn't already exist
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS messages (
    id SERIAL PRIMARY KEY,
    sender_id uuid DEFAULT uuid_generate_v4 (),
    rcvr_id uuid DEFAULT uuid_generate_v4 (),
    message TEXT
);

INSERT INTO messages (
    sender_id,
    rcvr_id,
    message
)
VALUES
    (
        '64b4d4dc7e73400db56d030067b2b46e',
        '065cdf165d4745579f58a704bb898913',
        'test message number 1'
    ),
    (
        '64b4d4dc7e73400db56d030067b2b46e',
        '065cdf165d4745579f58a704bb898913',
        'test message number 2'
    ),
    (
        '9bd306684c86419083e354120d52bbf1',
        '03504460ff77400baa0aba9f00c31da6',
        'yet another test message'
    ),
    (
        'ba43691a7e0342b180d65d2a58dfcd7c',
        '53e3c9be339144cc875000fd4f3dbcb2',
        'this is the last message i am going to add.'
    );