/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package com.github.francescojo.infra.jdbc.user

import com.github.francescojo.infra.jdbc.common.Versioned
import com.github.francescojo.infra.jdbc.common.embedded.DateEmbedded
import com.github.francescojo.infra.jdbc.common.embedded.SoftDeletableEmbedded
import com.github.francescojo.infra.jdbc.common.embedded.VersionEmbedded
import jakarta.persistence.*

/**
 * @since 2021-08-10
 */
@Entity
@Table(name = UserEntity.TABLE)
class UserEntity(
    @Id @Column(name = COL_ID)
    // Better to change from Long to TSID with converter
    val id: Long,

    @Column(name = COL_NICKNAME)
    var nickname: String,

    @Column(name = COL_EMAIL)
    var email: String,

    @Embedded
    val timestamp: DateEmbedded = DateEmbedded(),

    @Embedded
    val deleted: SoftDeletableEmbedded = SoftDeletableEmbedded(),

    @Embedded
    val ver: VersionEmbedded = VersionEmbedded()
) : Versioned<Long> by ver {
    override fun toString(): String = """${UserEntity::class.simpleName}(
        |  ${::id.name} = $id,
        |  ${::nickname.name} = '$nickname',
        |  ${::email.name} = '$email',
        |  ${::timestamp.name} = $timestamp,
        |  ${::deleted.name} = $deleted,
        |  ${::ver.name} = $ver
        |)""".trimMargin()

    companion object {
        const val TABLE = "users"

        const val COL_ID = "id"
        const val COL_NICKNAME = "nickname"
        const val COL_EMAIL = "email"
    }
}
