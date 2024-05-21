CREATE TABLE match_data
(
    id          SERIAL,
    match_id    VARCHAR(100) NOT NULL,
    market_id   BIGINT       NOT NULL,
    outcome_id  TEXT,
    specifiers  TEXT,
    date_insert TIMESTAMP
)