package pl.michalperlak.trf.util

internal fun List<String>?.first(default: String = ""): String =
    this?.firstOrNull() ?: default

internal fun List<String>?.firstInt(default: Int = 0): Int =
    this?.firstOrNull()?.toIntOrNull() ?: default