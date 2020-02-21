package pl.michalperlak.trf.util

import arrow.core.Option
import arrow.core.firstOrNone
import java.lang.Exception
import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun parseDate(value: String, vararg formats: DateTimeFormatter): Option<LocalDate> {
    fun tryParse(
        current: Option<LocalDate>,
        formats: List<DateTimeFormatter>
    ): Option<LocalDate> =
        if (current.isDefined()) {
            current
        } else {
            val parsed = formats.firstOrNone().flatMap { tryParseDate(value, it) }
            tryParse(parsed, formats.drop(1))
        }
    return tryParse(Option.empty(), formats.toList())
}

private fun tryParseDate(value: String, format: DateTimeFormatter): Option<LocalDate> =
    try {
        Option.just(LocalDate.parse(value, format))
    } catch (e: Exception) {
        Option.empty()
    }