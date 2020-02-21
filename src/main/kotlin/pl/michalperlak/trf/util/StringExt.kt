package pl.michalperlak.trf.util

fun String.splitByWhitespace(): List<String> =
    split("\\s+".toRegex())
        .map { it.trim() }
        .filter { it.isNotBlank() }