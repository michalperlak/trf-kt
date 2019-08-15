package pl.michalperlak.trf

data class PlayerGameResult(
    val gameColor: GameColor,
    val result: GameResult,
    val opponentId: PlayerId
)