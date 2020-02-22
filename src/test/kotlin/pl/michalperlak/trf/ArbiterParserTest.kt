package pl.michalperlak.trf

import arrow.core.getOrElse
import org.junit.jupiter.api.Test
import pl.michalperlak.trf.parser.ArbiterParser

internal class ArbiterParserTest {

    @Test
    fun `should correctly parse main arbiter`() {
        // given
        val lines = listOf("102 IA Mikel Larreategi Arana (22232540)")
        val parser = ArbiterParser()

        // when
        val result = parser.parseOne(lines)

        // then
        val arbiter = result.getOrElse { throw AssertionError("Expected result, but was none") }
        println(arbiter)
    }

    @Test
    fun `should correctly parse deputy arbiters`() {

    }

    @Test
    fun `should return empty result when no main arbiter lines available`() {

    }

    @Test
    fun `should return empty list when no deputy arbiter lines available`() {

    }
}