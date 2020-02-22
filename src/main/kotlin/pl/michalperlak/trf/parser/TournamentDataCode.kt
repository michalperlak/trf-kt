package pl.michalperlak.trf.parser

import arrow.core.Option

internal enum class TournamentDataCode(
    val code: String
) {
    TournamentName("012"),
    City("022"),
    Federation("032"),
    DateOfStart("042"),
    DateOfEnd("052"),
    NumberOfPlayers("062"),
    NumberOfRatedPlayers("072"),
    NumberOfTeams("082"),
    TypeOfTournament("092"),
    ChiefArbiter("102"),
    DeputyChiefArbiter("112"),
    AllotedTimes("122"),
    RoundDates("132"),
    PlayerData("001"),
    TeamData("013"),
    Unknown("");

    companion object {
        fun from(code: String): Option<TournamentDataCode> = Option.fromNullable(
            values()
                .firstOrNull { it.code.equals(code, ignoreCase = true) }
        )
    }
}