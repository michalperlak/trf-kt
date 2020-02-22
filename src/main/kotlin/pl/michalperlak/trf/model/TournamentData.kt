package pl.michalperlak.trf.model

import arrow.core.Option
import java.time.LocalDate

data class TournamentData(
    val name: String,
    val city: String,
    val federation: Option<Federation>,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val numberOfPlayers: Int,
    val numberOfRatedPlayers: Int,
    val numberOfTeams: Int,
    val type: String,
    val chiefArbiter: Option<Arbiter>,
    val deputyArbiters: List<Arbiter>,
    val rateOfPlay: String,
    val roundDates: List<LocalDate>,
    val playersData: List<PlayerData>,
    val teamsData: List<TeamData>
)