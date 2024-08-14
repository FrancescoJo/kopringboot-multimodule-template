CREATE TABLE IF NOT EXISTS `users`
(
    `id`            BIGINT      NOT NULL PRIMARY KEY,
    `nickname`      VARCHAR(64) NOT NULL,
    `email`         VARCHAR(64) NOT NULL,
    `is_deleted`    BOOLEAN     NOT NULL DEFAULT FALSE,
    `created_at`    DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`    DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `version`       BIGINT      NOT NULL,

    CONSTRAINT `uk_users_email`    UNIQUE (`email`),
    CONSTRAINT `uk_users_identity` UNIQUE (`nickname`, `email`)
);
