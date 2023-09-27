CREATE TABLE transactions (
                              id SERIAL PRIMARY KEY,
                              account_from NUMERIC(10, 2) NOT NULL,
                              account_to NUMERIC(10, 2) NOT NULL,
                              expense_category VARCHAR(255) NOT NULL,
                              currency_shortname VARCHAR(3) NOT NULL,
                              sum NUMERIC(10, 2) NOT NULL,
                              datetime TIMESTAMPTZ,
                              limit_exceeded BOOLEAN DEFAULT FALSE,
                              limit_id BIGINT,
                              FOREIGN KEY (limit_id) REFERENCES limits(id)
);
