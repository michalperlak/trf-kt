package pl.michalperlak.trf.parser

internal interface EntryParser<T : Any> {
    fun parse(values: List<String>?): T
}