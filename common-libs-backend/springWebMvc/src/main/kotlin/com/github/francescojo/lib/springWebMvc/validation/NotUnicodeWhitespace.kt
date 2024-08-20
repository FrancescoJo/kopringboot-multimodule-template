/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package com.github.francescojo.lib.springWebMvc.validation

import com.github.francescojo.lib.text.isNullOrUnicodeBlank
import jakarta.validation.Constraint
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import jakarta.validation.Payload
import jakarta.validation.constraints.NotBlank
import org.springframework.stereotype.Component
import kotlin.reflect.KClass

/**
 * Supports validation of [CharSequence]s that contain whitespace only characters in ASCII and Unicode extended plane.
 * Annotated element must not be null and must contain at least one non-whitespace character.
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 19 - Jul - 2020
 * @see NotBlank
 */
@Target(
    AnnotationTarget.CLASS,
    AnnotationTarget.FIELD,
    AnnotationTarget.PROPERTY,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.VALUE_PARAMETER
)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [NotUnicodeWhitespaceValidator::class])
@MustBeDocumented
annotation class NotUnicodeWhitespace(
    val message: String = "Must not be a null or CharSequence with only whitespace characters.",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)

@Component
private class NotUnicodeWhitespaceValidator : ConstraintValidator<NotUnicodeWhitespace, CharSequence> {
    override fun isValid(value: CharSequence?, context: ConstraintValidatorContext?): Boolean =
        // Skip validation if given value is null
        if (value == null) {
            true
        } else {
            !value.toString().isNullOrUnicodeBlank()
        }
}
