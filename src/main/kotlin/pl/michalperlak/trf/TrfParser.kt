package pl.michalperlak.trf

import arrow.core.Try
import arrow.core.getOrElse
import arrow.core.toOption
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
            federation = data[TournamentDataCode.Federation]?.let { getFederation(it.first()) }.toOption(),
            startDate = data[TournamentDataCode.DateOfStart]?.let { getDate(it.first()) } ?: LocalDate.now(),
            endDate = data[TournamentDataCode.DateOfEnd]?.let { getDate(it.first()) } ?: LocalDate.now(),
            numberOfPlayers = data[TournamentDataCode.NumberOfPlayers]?.first()?.toIntOrNull() ?: 0,
            numberOfRatedPlayers = data[TournamentDataCode.NumberOfRatedPlayers]?.first()?.toIntOrNull() ?: 0,
            numberOfTeams = data[TournamentDataCode.NumberOfTeams]?.first()?.toIntOrNull() ?: 0,
            type = data[TournamentDataCode.TypeOfTournament]?.first() ?: "",
            chiefArbiter = data[TournamentDataCode.ChiefArbiter]?.let { getArbiter(it.first()) }.toOption(),
            deputyArbiters = data[TournamentDataCode.DeputyChiefArbiter]?.map { getArbiter(it) }
                ?: emptyList(),
            rateOfPlay = data[TournamentDataCode.AllotedTimes]?.first() ?: "",
            roundDates = data[TournamentDataCode.RoundDates]?.let { getRoundDates(it.first()) } ?: emptyList(),
            playersData = data[TournamentDataCode.PlayerData]?.map { getPlayerData(it) } ?: emptyList(),
            teamsData = data[TournamentDataCode.TeamData]?.map { getTeamData(it) } ?: emptyList()
        )
    }

    private fun extractLineValue(line: String): String =
        if (line.length > LINE_CODE_LENGTH) line.substring(LINE_CODE_LENGTH) else ""

    private fun getFederation(federationLine: String): Federation = Federation(federationLine)

    private fun getDate(dateLine: String): LocalDate = LocalDate.parse(dateLine, START_END_DATE_FORMAT)

    private fun getRoundDates(roundDatesLine: String): List<LocalDate> = roundDatesLine
        .split("\\s+".toRegex())
        .map { LocalDate.parse(it.trim(), ROUND_DATE_FORMAT) }

    private fun getArbiter(arbiterLine: String): Arbiter {
        val parts = arbiterLine
            .split("\\s+".toRegex())
            .map { it.trim() }
            .filter { it.isNotEmpty() }
        return Arbiter(
            name = parts[1],
            id = if (parts.size > 2) parts[2].substring(1, parts[2].length - 1) else "",
            title = ArbiterTitle.from(parts[0])
        )
    }

    private fun getPlayerData(playerLine: String): PlayerData {
        println(playerLine)
        return PlayerData(
            startRank = playerLine.substring(0, 4).trim().toInt(),
            gender = Gender.from(playerLine[5]),
            title = Title.from(playerLine.substring(6, 9).trim()),
            name = playerLine.substring(10, 43).trim(),
            fideRating = playerLine.substring(44, 48).trim().toInt(),
            federation = Federation(playerLine.substring(49, 52).trim()),
            fideId = playerLine.substring(53, 64).trim().toLong(),
            birthDate = Try.invoke { getBirthDate(playerLine.substring(65, 75).trim()) }.toOption(),
            points = playerLine.substring(76, 80).trim().toDouble(),
            rank = playerLine.substring(81, 85).trim().toInt(),
            results = getPlayerResults(playerLine.substring(87))
        )
    }

    private fun getBirthDate(formattedDate: String): LocalDate = LocalDate.parse(formattedDate, BIRTH_DATE_FORMAT)

    private fun getPlayerResults(results: String): List<PlayerGameResult> {
        tailrec fun readPlayerResult(
            readResults: List<PlayerGameResult>,
            missingResults: String
        ): List<PlayerGameResult> =
            if (missingResults.isBlank()) {
                readResults
            } else {
                val resultString = missingResults.take(10)
                val result = PlayerGameResult(
                    opponentId = PlayerId(resultString.substring(0, 5).trim()),
                    gameColor = GameColor.from(resultString.substring(5, 7).trim())
                        .getOrElse { GameColor.NotPaired },
                    result = GameResult.from(resultString.substring(8).trim())
                        .getOrElse { GameResult.ZeroPointBye }
                )
                readPlayerResult(readResults + result, missingResults.drop(10))
            }
        return readPlayerResult(mutableListOf(), results)
    }

    private fun getTeamData(teamLine: String): TeamData {
        val parts = teamLine
            .split("\\s+".toRegex())
            .map { it.trim() }
            .filter { it.isNotBlank() }
        val teamName = parts[0]
        tailrec fun readTeamMember(teamLineParts: List<String>, teamMembers: List<PlayerId>): List<PlayerId> =
            if (teamLineParts.isEmpty()) teamMembers
            else readTeamMember(teamLineParts.drop(1), teamMembers + PlayerId(teamLineParts.first().trim()))

        val players = readTeamMember(parts.drop(1), mutableListOf())
        return TeamData(
            name = teamName,
            players = players
        )
    }

    private fun extractLineCode(line: String): TournamentDataCode {
        val code = line.substring(0, 4).trim()
        return TournamentDataCode
            .from(code)
            .getOrElse { TournamentDataCode.Unknown }
    }

    companion object {
        private const val LINE_CODE_LENGTH = 4
        private val START_END_DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        private val ROUND_DATE_FORMAT = DateTimeFormatter.ofPattern("yy/MM/dd")
        private val BIRTH_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy/MM/dd")
    }
}