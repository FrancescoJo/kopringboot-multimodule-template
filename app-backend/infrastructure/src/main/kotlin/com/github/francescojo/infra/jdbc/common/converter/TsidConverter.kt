/*
 * kopringboot-multimodule-monorepo-template
 * Distributed under MIT licence
 */
package com.github.francescojo.infra.jdbc.common.converter

import io.hypersistence.tsid.TSID
import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter

/**
 * Converts TSID to 8-bytes long type and vice versa.
 * Currently not working on Aug - 2024.
 *
 * @since 2022-02-14
 */
@Converter
class TsidConverter : AttributeConverter<TSID, Long> {
    override fun convertToDatabaseColumn(attribute: TSID): Long = attribute.toLong()

    override fun convertToEntityAttribute(dbData: Long): TSID = TSID.from(dbData)
}
