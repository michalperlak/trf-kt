package pl.michalperlak.trf.model

import arrow.core.Option

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
        fun from(code: String): Option<GameResult> = Option.fromNullable(
            values()
                .firstOrNull { it.codes.contains(code) }
        )
    }
}