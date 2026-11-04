-- Schema for t21 exercise 05. Run in phpMyAdmin against your own database.
CREATE TABLE IF NOT EXISTS game_assets (
    id         INT          NOT NULL PRIMARY KEY,
    name       VARCHAR(100) NOT NULL,
    asset_data MEDIUMBLOB   NOT NULL
);
