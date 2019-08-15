package pl.michalperlak.trf

import arrow.core.Try
import java.io.InputStream
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.stream.Collectors

class TrfParser {
    fun parse(trfFile: Path, charset: Charset = StandardCharsets.UTF_8): Try<TournamentData> = Try {
        val lines = Files.readAllLines(trfFile, charset)
        parseLines(lines)
    }

    fun parse(inputStream: InputStream, charset: Charset = StandardCharsets.UTF_8): Try<TournamentData> = Try {
        val lines = inputStream
            .bufferedReader(charset)
            .lines()
            .collect(Collectors.toList())
        parseLines(lines)
    }

    private fun parseLines(lines: List<String>): TournamentData {
        val data = lines
            .filter { it.length > LINE_CODE_LENGTH }
            .groupBy(this::extractLineCode)
            .mapValues { it.value.map(this::extractLineValue) }

        return TournamentData(
            name = data[TournamentDataCode.TournamentName]?.first() ?: "",
            city = data[TournamentDataCode.City]?.first() ?: "",
            federation = data[TournamentDataCode.Federation]?.let { getFederation(it.first()) } ?: Federation.UNKNOWN,
            startDate = data[TournamentDataCode.DateOfStart]?.let { getDate(it.first()) } ?: LocalDate.now(),
            endDate = data[TournamentDataCode.DateOfEnd]?.let { getDate(it.first()) } ?: LocalDate.now(),
            numberOfPlayers = data[TournamentDataCode.NumberOfPlayers]?.first()?.toIntOrNull() ?: 0,
            numberOfRatedPlayers = data[TournamentDataCode.NumberOfRatedPlayers]?.first()?.toIntOrNull() ?: 0,
            numberOfTeams = data[TournamentDataCode.NumberOfTeams]?.first()?.toIntOrNull() ?: 0,
            type = data[TournamentDataCode.TypeOfTournament]?.first() ?: "",
            chiefArbiter = data[TournamentDataCode.ChiefArbiter]?.let { getArbiter(it.first()) } ?: Arbiter.UNKNOWN,
            deputyArbiters = data[TournamentDataCode.DeputyChiefArbiter]?.let { getDeputyArbiters(it.first()) }
                ?: emptyList(),
            rateOfPlay = data[TournamentDataCode.AllotedTimes]?.first() ?: "",
            roundDates = data[TournamentDataCode.RoundDates]?.let { getRoundDates(it.first()) } ?: emptyList(),
            playersData = data[TournamentDataCode.PlayerData]?.map { getPlayerData(it) } ?: emptyList(),
            teamsData = data[TournamentDataCode.TeamData]?.map { getTeamData(it) } ?: emptyList()
        )
    }

    private fun extractLineValue(line: String): String =
        if (line.length > LINE_CODE_LENGTH) line.substring(LINE_CODE_LENGTH).trim() else ""

    private fun getFederation(federationLine: String): Federation = Federation(federationLine)

    private fun getDate(dateLine: String): LocalDate = LocalDate.parse(dateLine, START_END_DATE_FORMAT)

    private fun getRoundDates(roundDatesLine: String): List<LocalDate> = roundDatesLine
        .split("\\s+".toRegex())
        .map { LocalDate.parse(it.trim(), ROUND_DATE_FORMAT) }

    private fun getArbiter(arbiterLine: String): Arbiter = TODO()

    private fun getDeputyArbiters(deputyArbitersLine: String): List<Arbiter> = TODO()

    private fun getPlayerData(playerLine: String): PlayerData = TODO()

    private fun getTeamData(teamLine: String): TeamData = TODO()

    private fun extractLineCode(line: String): TournamentDataCode {
        val code = line.substring(0, 4).trim()
        return TournamentDataCode.from(code)
    }

    companion object {
        private const val LINE_CODE_LENGTH = 4
        private val START_END_DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        private val ROUND_DATE_FORMAT = DateTimeFormatter.ofPattern("yy/MM/dd")
    }
}