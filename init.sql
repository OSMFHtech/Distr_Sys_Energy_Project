CREATE TABLE IF NOT EXISTS usage_hourly (
    hour TIMESTAMP PRIMARY KEY,
    community_produced DOUBLE PRECISION DEFAULT 0,
    community_used DOUBLE PRECISION DEFAULT 0,
    grid_used DOUBLE PRECISION DEFAULT 0
);

CREATE TABLE IF NOT EXISTS percentage_current (
    hour TIMESTAMP PRIMARY KEY,
    community_depleted DOUBLE PRECISION,
    grid_portion DOUBLE PRECISION
);