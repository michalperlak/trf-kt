package pl.michalperlak.trf

import java.time.LocalDate

data class TournamentData(
    val name: String,
    val city: String,
    val federation: Federation,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val numberOfPlayers: Int,
    val numberOfRatedPlayers: Int,
    val numberOfTeams: Int,
    val type: String,
    val chiefArbiter: Arbiter,
    val deputyArbiters: List<Arbiter>,
    val rateOfPlay: String,
    val roundDates: List<LocalDate>,
    val playersData: List<PlayerData>,
    val teamsData: List<TeamData>
)