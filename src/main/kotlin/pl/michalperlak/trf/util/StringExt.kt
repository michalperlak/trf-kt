package pl.michalperlak.trf.util

internal fun String.splitByWhitespace(): List<String> =
    split("\\s+".toRegex())
        .map { it.trim() }
        .filter { it.isNotBlank() }