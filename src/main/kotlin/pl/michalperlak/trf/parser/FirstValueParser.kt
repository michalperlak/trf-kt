package pl.michalperlak.trf.parser

import pl.michalperlak.trf.model.TournamentDataCode

class FirstValueParser<T : Any>(
    override val codes: Set<TournamentDataCode>,
    private val mapper: (String) -> T,
    private val defaultValue: T
) : EntryParser<T> {

    override fun parse(values: List<String>?): T {
        val first = values?.firstOrNull() ?: return defaultValue
        return mapper(first)
    }
}