package pl.michalperlak.trf.model

import arrow.core.Option

enum class GameColor(
    vararg val codes: String
) {
    White("w"),
    Black("b"),
    NotPaired("-", "");

    companion object {
        fun from(code: String): Option<GameColor> = Option.fromNullable(
            values()
                .firstOrNull { it.codes.contains(code) }
        )
    }
}