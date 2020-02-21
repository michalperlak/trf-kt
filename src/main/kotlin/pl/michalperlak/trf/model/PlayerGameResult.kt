package pl.michalperlak.trf.model

data class PlayerGameResult(
    val gameColor: GameColor,
    val result: GameResult,
    val opponentId: PlayerId
)