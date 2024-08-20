/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package com.github.francescojo.lib.springWebMvc.validation

import jakarta.validation.Constraint
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import jakarta.validation.Payload
import org.springframework.stereotype.Component
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation

/**
 * @since 2022-09-02
 */
@Target(
    AnnotationTarget.CLASS,
    AnnotationTarget.FIELD,
    AnnotationTarget.PROPERTY,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.VALUE_PARAMETER
)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [ValidCollectionValidator::class])
@MustBeDocumented
annotation class ValidCollection(
    val message: String = "There is/are some element(s) which violate(s) validation rule(s).",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = [],

    val validator: KClass<out Annotation>
)

@Component
private class ValidCollectionValidator : ConstraintValidator<ValidCollection, Collection<Any>> {
    private val validators = ArrayList<ConstraintValidator<in Annotation, in Any>>()

    override fun initialize(constraintAnnotation: ValidCollection) {
        val validatorClass = constraintAnnotation.validator

        require (validatorClass.hasAnnotation<Constraint>()) {
            "There is no ${Constraint::class.qualifiedName} declaration on $validatorClass."
        }

        val errors = ArrayList<KClass<*>>()
        validatorClass.findAnnotation<Constraint>()!!.validatedBy.forEach {
            @Suppress("SwallowedException")     // 예외 모아서 한번에 처리하므로 무시
            try {
                @Suppress("UNCHECKED_CAST")     // 이 로직 내에서는 Checked cast 임!!
                validators.add(it.createInstance() as ConstraintValidator<in Annotation, in Any>)
            } catch (e: ClassCastException) {
                errors.add(it)
            }
        }

        require (validators.isNotEmpty()) { "Empty 'validateBy' on $validatorClass." }
        require (errors.isEmpty()) {
            "${ConstraintValidator::class.qualifiedName} implementation has problems with $errors."
        }
    }

    override fun isValid(value: Collection<Any>?, context: ConstraintValidatorContext?): Boolean {
        if (value == null) {
            return true
        }

        val errIndexes = ArrayList<Int>()
        var index = 0
        value.forEach { element ->
            if (!validators.all { it.isValid(element, context) }) {
                errIndexes.add(index)
            }
            ++index
        }

        require (errIndexes.isEmpty()) { "Input error found on index $errIndexes." }

        return true
    }
}
