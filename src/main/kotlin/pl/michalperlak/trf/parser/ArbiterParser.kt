package pl.michalperlak.trf.parser

import arrow.core.Option
import pl.michalperlak.trf.model.Arbiter
import pl.michalperlak.trf.model.ArbiterTitle
import pl.michalperlak.trf.util.splitByWhitespace

internal class ArbiterParser : EntryParser<List<Arbiter>> {

    fun parseOne(values: List<String>?): Option<Arbiter> =
        Option.fromNullable(
            values?.firstOrNull()?.let(::parseArbiter)
        )

    override fun parse(values: List<String>?): List<Arbiter> =
        values?.map(::parseArbiter) ?: emptyList()

    private fun parseArbiter(arbiterLine: String): Arbiter {
        val parts = arbiterLine.splitByWhitespace()
        val title = ArbiterTitle.from(parts[0])
        return Arbiter(
            name = getArbiterName(parts, title.isDefined()),
            fideId = getFideId(parts),
            title = ArbiterTitle.from(parts[0])
        )
    }

    private fun getArbiterName(parts: List<String>, hasTitle: Boolean): String {
        val elements = if (hasTitle) parts.drop(1) else parts
        val lettersOnlyPattern = Regex("[A-Za-z]+")
        return elements
            .takeWhile { it.matches(lettersOnlyPattern) }
            .joinToString(separator = " ")
    }

    private fun getFideId(parts: List<String>): String {
        val idRegex = Regex("\\([0-9]+\\)")
        return parts
            .firstOrNull { it.matches(idRegex) }
            ?.let { it.substring(1, it.length - 1) }
            ?: ""
    }
}