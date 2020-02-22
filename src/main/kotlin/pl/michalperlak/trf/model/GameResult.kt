package pl.michalperlak.trf.model

enum class GameResult(
    vararg val codes: String
) {
    ForfeitLoss("-"),
    ForfeitWin("+"),
    WinLessThanOneMove("W"),
    DrawLessThanOneMove("D"),
    LossLessThanOneMove("L"),
    RegularWin("1"),
    RegularDraw("="),
    RegularLoss("0"),
    HalfPointBye("H"),
    FullPointBye("F"),
    PairingAllocatedBye("U"),
    ZeroPointBye("Z", "");

    companion object {
        fun from(code: String, default: GameResult = ZeroPointBye): GameResult =
            values().firstOrNull { it.codes.contains(code) } ?: default
    }
}