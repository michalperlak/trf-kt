package pl.michalperlak.trf.model

enum class GameColor(
    vararg val codes: String
) {
    White("w"),
    Black("b"),
    NotPaired("-", "");

    companion object {
        fun from(code: String, default: GameColor = NotPaired): GameColor =
            values().firstOrNull { it.codes.contains(code) } ?: default
    }
}