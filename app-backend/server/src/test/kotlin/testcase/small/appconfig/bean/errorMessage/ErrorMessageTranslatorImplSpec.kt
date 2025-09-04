/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package testcase.small.appconfig.bean.errorMessage

import com.github.francescojo.appconfig.bean.errorMessage.ErrorMessageTranslatorImpl
import com.github.francescojo.appconfig.bean.errorMessage.MessageTemplateHolderJa
import com.github.francescojo.core.exception.ErrorCodes
import com.github.francescojo.core.exception.external.RequestedResourceNotFoundException
import com.github.francescojo.core.exception.external.RequestedServiceNotFoundException
import com.github.francescojo.core.exception.internal.UnhandledException
import com.github.francescojo.lib.annotation.SmallTest
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import java.util.*

/**
 * @since 2025-09-04
 */
@SmallTest
internal class ErrorMessageTranslatorImplSpec {
    private lateinit var sut: ErrorMessageTranslatorImpl

    @BeforeEach
    fun setUp() {
        sut = ErrorMessageTranslatorImpl(
            log = LoggerFactory.getLogger(ErrorMessageTranslatorImpl::class.java)
        ).also { it.onPostConstruct() }
    }

    @DisplayName("Falls back to default English for English variants")
    @Test
    fun fallsBackEnglishVariants() {
        // given:
        val locale = Locale.of("en", "AU")
        val exception = RequestedServiceNotFoundException()

        // when:
        val message = sut.translate(locale, exception)

        // then:
        message shouldBe ErrorCodes.SERVICE_NOT_FOUND.defaultMessage
    }

    @DisplayName("Falls back to default English if no matching locale is found")
    @Test
    fun fallsBackNoMatch() {
        // given:
        val locale = Locale.of("fr", "FR")
        val exception = UnhandledException()

        // when:
        val message = sut.translate(locale, exception)

        // then:
        message shouldBe ErrorCodes.UNHANDLED_EXCEPTION.defaultMessage
    }

    @DisplayName("Matches to U.S. English for \"en-US\"")
    @Test
    fun matchesUsEnglish() {
        // given:
        val resourceName = "dummy"
        val locale = Locale.of("en", "US")
        val exception = RequestedResourceNotFoundException(resourceName)

        // when:
        val message = sut.translate(locale, exception)

        // then:
        message shouldBe "The requested resource '$resourceName' could not be found."
    }

    @DisplayName("Matches to Japanese even if Script and Country are given but only for \"ja\" is present")
    @Test
    fun matchesLanguageOnly() {
        // given:
        val locale = Locale.Builder()
            .setLanguage("ja")
            .setScript("Jpan")
            .setRegion("JP")
            .build()
        val exception = UnhandledException()

        // when:
        val message = sut.translate(locale, exception)

        // then:
        message shouldBe MessageTemplateHolderJa().getTemplateOf(ErrorCodes.UNHANDLED_EXCEPTION)
    }

    @DisplayName("Default exception message is used if no template is found or template is empty")
    @Test
    fun usesDefaultMessage() {
        // given:
        val locale = Locale.of("en")
        val exception = UnhandledException()

        // when:
        val message = sut.translate(locale, exception)

        // then:
        message shouldBe ErrorCodes.UNHANDLED_EXCEPTION.defaultMessage
    }
}
