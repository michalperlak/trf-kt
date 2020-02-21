package pl.michalperlak.trf.parser

import pl.michalperlak.trf.model.TournamentDataCode

internal interface EntryParser<T : Any> {
    val codes: Set<TournamentDataCode>

    fun parse(values: List<String>?): T
}