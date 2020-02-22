package pl.michalperlak.trf.parser

import pl.michalperlak.trf.util.splitByWhitespace
import java.time.LocalDate
import java.time.format.DateTimeFormatter

internal class RoundDatesParser : EntryParser<List<LocalDate>> {
    override fun parse(values: List<String>?): List<LocalDate> =
        values?.firstOrNull()?.let(::parseRoundDates) ?: emptyList()

    private fun parseRoundDates(roundDatesLine: String): List<LocalDate> =
        roundDatesLine
            .splitByWhitespace()
            .map { LocalDate.parse(it, ROUND_DATE_FORMAT) }

    companion object {
        val ROUND_DATE_FORMAT: DateTimeFormatter = DateTimeFormatter.ofPattern("yy/MM/dd")
    }
}