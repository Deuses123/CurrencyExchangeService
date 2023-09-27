CREATE TABLE limits (
                        id SERIAL PRIMARY KEY,
                        source_account NUMERIC(10, 2) NOT NULL,
                        limit_amount NUMERIC(10, 2),
                        limit_category VARCHAR(8) NOT NULL,
                        currency VARCHAR(3),
                        limit_datetime TIMESTAMPTZ
);
