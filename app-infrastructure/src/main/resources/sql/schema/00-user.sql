CREATE TABLE IF NOT EXISTS `users`
(
    `seq`            BIGINT      NOT NULL AUTO_INCREMENT,
    `id`             BINARY(16)  PRIMARY KEY,
    `nickname`       VARCHAR(64) NOT NULL,
    `email`          VARCHAR(64) NOT NULL,
    `deleted`        BOOLEAN     DEFAULT FALSE,
    `created_at`     DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`     DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `version`        BIGINT      NOT NULL,

    CONSTRAINT `uk_users_seq`      UNIQUE (`seq`),
    CONSTRAINT `uk_users_email`    UNIQUE (`email`),
    CONSTRAINT `uk_users_identity` UNIQUE (`nickname`, `email`)
);
