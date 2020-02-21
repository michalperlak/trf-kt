package pl.michalperlak.trf

import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import pl.michalperlak.trf.model.TournamentData
import pl.michalperlak.trf.parser.TrfParser
import java.io.InputStream
import java.util.stream.Stream

class TrfParserTest {

    @ParameterizedTest
    @ArgumentsSource(TrfTestCaseProvider::class)
    fun `should correctly parse TRF file`(resourcePath: String, expected: TournamentData?) {
        val parser = TrfParser()
        val tournamentData = inputStream(resourcePath).use {
            parser.parse(it)
        }.fold({ it.printStackTrace(); null }, { it })

//        println(tournamentData)
    }

    private fun inputStream(resourcePath: String): InputStream =
        TrfParserTest::class.java.getResourceAsStream(resourcePath)

    class TrfTestCaseProvider : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> =
            Stream.of(
                Arguments.of("/data/tournament1.trf", null)
            )
    }
}