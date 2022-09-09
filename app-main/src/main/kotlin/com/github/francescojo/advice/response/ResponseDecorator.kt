/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package com.github.francescojo.advice.response

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.github.francescojo.endpoint.ResponseEnvelope
import org.springframework.core.MethodParameter
import org.springframework.http.MediaType
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice

/**
 * @since 2021-08-10
 */
@RestControllerAdvice
class ResponseDecorator : ResponseBodyAdvice<Any> {
    override fun supports(returnType: MethodParameter, converterType: Class<out HttpMessageConverter<*>>): Boolean {
        val methodReturnType = returnType.method?.returnType ?: return false
        return methodReturnType.declaredAnnotations.any {
            it.annotationClass == JsonSerialize::class
        }
    }

    override fun beforeBodyWrite(
        body: Any?,
        returnType: MethodParameter,
        selectedContentType: MediaType,
        selectedConverterType: Class<out HttpMessageConverter<*>>,
        request: ServerHttpRequest,
        response: ServerHttpResponse
    ): Any? = ResponseEnvelope.ok(body)
}
