package pl.michalperlak.trf.parser

import arrow.core.Option
import pl.michalperlak.trf.model.Federation

internal class FederationParser : EntryParser<Option<Federation>> {
    override fun parse(values: List<String>?): Option<Federation> =
        Option
            .fromNullable(values?.firstOrNull())
            .map { Federation(it) }
}