CREATE TABLE IF NOT EXISTS capacities (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(255)
    );

CREATE TABLE IF NOT EXISTS capacity_bootcamp (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    capacity_id BIGINT NOT NULL,
    bootcamp_id BIGINT NOT NULL,

    -- evita duplicados del mismo par
    UNIQUE KEY uk_cap_boot (capacity_id, bootcamp_id),

    -- índices para búsquedas rápidas
    KEY idx_capacidad (capacity_id),
    KEY idx_bootcamp (bootcamp_id)
    );
