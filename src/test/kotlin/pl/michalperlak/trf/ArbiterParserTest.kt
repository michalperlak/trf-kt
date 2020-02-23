package pl.michalperlak.trf

import arrow.core.Option
import arrow.core.getOrElse
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import pl.michalperlak.trf.model.Arbiter
import pl.michalperlak.trf.model.ArbiterTitle
import pl.michalperlak.trf.model.ArbiterTitle.InternationalArbiter
import pl.michalperlak.trf.parser.ArbiterParser

internal class ArbiterParserTest {

    @Test
    fun `should correctly parse main arbiter with title and fide id`() {
        // given
        val lines = listOf("IA Mikel Larreategi Arana (22232540)")
        val parser = ArbiterParser()

        // when
        val result = parser.parseOne(lines)

        // then
        val arbiter = result.get()
        assertAll(
            { assertEquals(Option.just(InternationalArbiter), arbiter.title) },
            { assertEquals("Mikel Larreategi Arana", arbiter.name) },
            { assertEquals("22232540", arbiter.fideId) }
        )
    }

    @Test
    fun `should correctly parse main arbiter with title`() {
        // given
        val lines = listOf("FA John Doe")
        val parser = ArbiterParser()

        // when
        val result = parser.parseOne(lines)

        // then
        val arbiter = result.get()
        assertAll(
            { assertEquals(Option.just(ArbiterTitle.FIDEArbiter), arbiter.title) },
            { assertEquals("John Doe", arbiter.name) },
            { assertEquals("", arbiter.fideId) }
        )
    }

    @Test
    fun `should correctly parse main arbiter with fide id`() {
        // given
        val lines = listOf("Jan Kowalski (1234556)")
        val parser = ArbiterParser()

        // when
        val result = parser.parseOne(lines)

        // then
        val arbiter = result.get()
        assertAll(
            { assertEquals(Option.empty<Arbiter>(), arbiter.title) },
            { assertEquals("Jan Kowalski", arbiter.name) },
            { assertEquals("1234556", arbiter.fideId) }
        )
    }

    @Test
    fun `should correctly parse deputy arbiters`() {

    }

    @Test
    fun `should return empty result when no main arbiter lines available`() {
        // given
        val lines: List<String>? = null
        val parser = ArbiterParser()

        // when
        val result = parser.parseOne(lines)

        // then
        assertTrue(result.isEmpty())
    }

    @Test
    fun `should return empty list when no deputy arbiter lines available`() {
        // given
        val lines: List<String>? = null
        val parser = ArbiterParser()

        // when
        val result = parser.parse(lines)

        // then
        assertEquals(emptyList<Arbiter>(), result)
    }

    private fun Option<Arbiter>.get(): Arbiter =
        getOrElse { throw AssertionError("Expected result, but was none") }
}