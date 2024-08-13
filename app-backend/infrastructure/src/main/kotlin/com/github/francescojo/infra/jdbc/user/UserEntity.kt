/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package com.github.francescojo.infra.jdbc.user

import com.github.francescojo.infra.jdbc.common.Versioned
import com.github.francescojo.infra.jdbc.common.converter.TsidConverter
import com.github.francescojo.infra.jdbc.common.embedded.DateEmbedded
import com.github.francescojo.infra.jdbc.common.embedded.SoftDeletableEmbedded
import com.github.francescojo.infra.jdbc.common.embedded.VersionEmbedded
import io.hypersistence.tsid.TSID
import jakarta.persistence.Column
import jakarta.persistence.Convert
import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

/**
 * @since 2021-08-10
 */
@Entity
@Table(name = UserEntity.TABLE)
class UserEntity(
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = COL_SEQ, updatable = false)
    var seq: Long = 0L,

    @Id @Column(name = COL_ID, columnDefinition = "BIGINT")
    @Convert(converter = TsidConverter::class)
    val id: TSID,

    @Column(name = COL_NICKNAME)
    var nickname: String,

    @Column(name = COL_EMAIL)
    var email: String,

    @Embedded
    val dateEmbedded: DateEmbedded = DateEmbedded(),

    @Embedded
    val softDeletableEmbedded: SoftDeletableEmbedded = SoftDeletableEmbedded(),

    @Embedded
    val versionEmbedded: VersionEmbedded = VersionEmbedded()
) : Versioned<Long> by versionEmbedded {
    companion object {
        const val TABLE = "users"

        const val COL_SEQ = "seq"
        const val COL_ID = "id"
        const val COL_NICKNAME = "nickname"
        const val COL_EMAIL = "email"
    }
}
