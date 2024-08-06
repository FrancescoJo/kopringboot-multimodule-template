/*
 * kopringboot-multimodule-monorepo-template
 * Distributed under MIT licence
 */
package com.github.francescojo.infra.jdbc.common.converter

import com.github.francescojo.lib.util.toByteArray
import com.github.francescojo.lib.util.toUUID
import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter
import java.util.*

/**
 * Converts Uuid to 16-bytes long ByteArray and vice versa.
 *
 * @since 2022-02-14
 */
@Converter
class UuidConverter : AttributeConverter<UUID, ByteArray> {
    override fun convertToDatabaseColumn(attribute: UUID?): ByteArray? = attribute?.toByteArray()

    override fun convertToEntityAttribute(dbData: ByteArray?): UUID? = dbData?.toUUID()
}
