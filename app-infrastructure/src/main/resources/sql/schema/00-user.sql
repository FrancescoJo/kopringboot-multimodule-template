CREATE TABLE IF NOT EXISTS `users`
(
    `seq`            BIGINT      NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `uuid`           BINARY(16)  NOT NULL,
    `nickname`       VARCHAR(64) NOT NULL,
    `email`          VARCHAR(64) NOT NULL,
    `deleted`        BOOLEAN     DEFAULT FALSE,
    `created_at`     DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`     DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `version`        BIGINT      NOT NULL,

    CONSTRAINT `uk_users_uuid`     UNIQUE (`uuid`),
    CONSTRAINT `uk_users_email`    UNIQUE (`email`),
    CONSTRAINT `uk_users_identity` UNIQUE (`nickname`, `email`)
);
