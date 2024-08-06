/*
 * kopringboot-multimodule-monorepo-template
 * Distributed under MIT licence
 */
package com.github.francescojo.infra.jdbc.common.embedded

import com.github.francescojo.core.domain.DateAuditable
import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.time.Instant

/**
 * @since 2022-08-19
 */
@Embeddable
class DateEmbedded(
    @get:CreatedDate
    @get:Column(name = COLUMN_CREATED_AT)
    override var createdAt: Instant = Instant.now(),

    @get:LastModifiedDate
    @get:Column(name = COLUMN_UPDATED_AT)
    override var updatedAt: Instant = createdAt
) : DateAuditable {
    companion object {
        const val COLUMN_CREATED_AT = "created_at"
        const val COLUMN_UPDATED_AT = "updated_at"
    }
}
