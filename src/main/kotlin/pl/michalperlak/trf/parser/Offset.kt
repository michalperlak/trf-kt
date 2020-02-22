package pl.michalperlak.trf.parser

data class Offset(val start: Int, val end: Int) {
    fun getValue(line: String): String =
        when {
            line.length < start -> ""
            line.length < end -> line.substring(start).trim()
            else -> line.substring(start, end).trim()
        }
}