-- ============================================================
-- GCA2 Support — Task Management System
-- Run this script to recreate the schema and seed data.
-- ============================================================

DROP DATABASE IF EXISTS gca2_support_db;
CREATE DATABASE gca2_support_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE gca2_support_db;

-- User
CREATE USER IF NOT EXISTS 'gca2_user'@'localhost' IDENTIFIED BY 'your_password';
GRANT ALL PRIVILEGES ON gca2_support_db.* TO 'gca2_user'@'localhost';
FLUSH PRIVILEGES;

-- Tasks table
CREATE TABLE tasks (
    task_id     INT AUTO_INCREMENT PRIMARY KEY,
    title       VARCHAR(255) NOT NULL,
    description TEXT,
    completed   BOOLEAN      NOT NULL DEFAULT FALSE
);

-- Seed data
INSERT INTO tasks (title, description, completed) VALUES
    ('Buy groceries',       'Milk, bread, eggs',                        FALSE),
    ('Write unit tests',    'Cover DAO, JSON, and socket layers',       FALSE),
    ('Submit GCA2',         'Upload to Moodle before deadline',         FALSE),
    ('Read t12 notes',      'DAO pattern and JDBC',                     TRUE),
    ('Fix connection pool', 'Server drops connection after 10 clients', FALSE);
