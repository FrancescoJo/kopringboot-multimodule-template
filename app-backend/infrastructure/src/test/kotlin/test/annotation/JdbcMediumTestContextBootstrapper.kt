/*
 * kopringboot-multimodule-monorepo-template
 * Distributed under MIT licence
 */
package test.annotation

//import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.SpringBootTestContextBootstrapper
import org.springframework.core.annotation.MergedAnnotations
import org.springframework.test.context.TestContextAnnotationUtils

/**
 * A merged Spring Boot TestContextBootstrapper for JdbcMediumTest.
 *
 * @see org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTestContextBootstrapper
 * @see org.springframework.boot.test.autoconfigure.jdbc.JdbcTestContextBootstrapper
 * @see org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTestContextBootstrapper
 *
 * @since 2024-08-13
 */
class JdbcMediumTestContextBootstrapper : SpringBootTestContextBootstrapper() {
    override fun getProperties(testClass: Class<*>): Array<String>? {
        val jdbcTest = TestContextAnnotationUtils.findMergedAnnotation(testClass, JdbcTest::class.java)
//        val dataJdbcTest = TestContextAnnotationUtils.findMergedAnnotation(testClass, DataJdbcTest::class.java)
        val dataJpaTest = MergedAnnotations.from(testClass, MergedAnnotations.SearchStrategy.INHERITED_ANNOTATIONS)
            .get(DataJpaTest::class.java)
            .getValue("properties", Array<String>::class.java)
            .orElse(null);

        return if (jdbcTest == null && /*dataJdbcTest == null && */dataJpaTest == null) {
            null
        } else {
            val properties = mutableListOf<String>()

            jdbcTest?.properties?.let { properties.addAll(it) }
//            dataJdbcTest?.properties?.let { properties.addAll(it) }
            dataJpaTest?.let { properties.addAll(it) }

            /* return if */ properties.toTypedArray()
        }
    }
}
