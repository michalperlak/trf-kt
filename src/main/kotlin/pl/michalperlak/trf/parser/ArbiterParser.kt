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
        return Arbiter(
            name = parts[1],
            id = if (parts.size > 2) parts[2].substring(1, parts[2].length - 1) else "",
            title = ArbiterTitle.from(parts[0])
        )
    }
}