package pl.michalperlak.trf.parser

import java.time.LocalDate
import java.time.format.DateTimeFormatter

internal class StartEndDateParser(
    private val defaultValue: LocalDate
) : EntryParser<LocalDate> {

    override fun parse(values: List<String>?): LocalDate =
        values?.firstOrNull()?.let(::parseDate) ?: defaultValue

    private fun parseDate(dateLine: String): LocalDate = LocalDate.parse(dateLine, START_END_DATE_FORMAT)

    companion object {
        val START_END_DATE_FORMAT: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    }
}