package pl.michalperlak.trf.model

import arrow.core.Option
import java.time.LocalDate

data class PlayerData(
    val startRank: Int,
    val gender: Option<Gender>,
    val title: Option<Title>,
    val name: String,
    val fideRating: Int,
    val federation: Federation,
    val fideId: Long,
    val birthDate: Option<LocalDate>,
    val points: Double,
    val rank: Int,
    val results: List<PlayerGameResult>
)