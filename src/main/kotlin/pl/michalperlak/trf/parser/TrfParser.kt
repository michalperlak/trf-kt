package pl.michalperlak.trf.parser

import arrow.core.Try
import arrow.core.getOrElse
import arrow.core.toOption
import pl.michalperlak.trf.model.*
import pl.michalperlak.trf.parser.TrfParser.Companion.DateFormats.ROUND_DATE_FORMAT
import pl.michalperlak.trf.util.splitByWhitespace
import java.io.InputStream
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.stream.Collectors

class TrfParser {
    private val startEndDateParser = StartEndDateParser(LocalDate.MIN)
    private val playerDataParser = PlayerDataParser()
    private val teamDataParser = TeamDataParser()

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
            startDate = startEndDateParser.parse(data[TournamentDataCode.DateOfStart]),
            endDate = startEndDateParser.parse(data[TournamentDataCode.DateOfEnd]),
            numberOfPlayers = data[TournamentDataCode.NumberOfPlayers]?.first()?.toIntOrNull() ?: 0,
            numberOfRatedPlayers = data[TournamentDataCode.NumberOfRatedPlayers]?.first()?.toIntOrNull() ?: 0,
            numberOfTeams = data[TournamentDataCode.NumberOfTeams]?.first()?.toIntOrNull() ?: 0,
            type = data[TournamentDataCode.TypeOfTournament]?.first() ?: "",
            chiefArbiter = data[TournamentDataCode.ChiefArbiter]?.let { getArbiter(it.first()) }.toOption(),
            deputyArbiters = data[TournamentDataCode.DeputyChiefArbiter]?.map { getArbiter(it) }
                ?: emptyList(),
            rateOfPlay = data[TournamentDataCode.AllotedTimes]?.first() ?: "",
            roundDates = data[TournamentDataCode.RoundDates]?.let { getRoundDates(it.first()) } ?: emptyList(),
            playersData = data[TournamentDataCode.PlayerData]?.map { playerDataParser.parse(it) } ?: emptyList(),
            teamsData = data[TournamentDataCode.TeamData]?.map { teamDataParser.parse(it) } ?: emptyList()
        )
    }

    private fun extractLineValue(line: String): String =
        if (line.length > LINE_CODE_LENGTH) {
            line.substring(LINE_CODE_LENGTH)
        } else {
            ""
        }

    private fun getFederation(federationLine: String): Federation =
        Federation(federationLine)

    private fun getRoundDates(roundDatesLine: String): List<LocalDate> = roundDatesLine
        .splitByWhitespace()
        .map { LocalDate.parse(it, ROUND_DATE_FORMAT) }

    private fun getArbiter(arbiterLine: String): Arbiter {
        val parts = arbiterLine.splitByWhitespace()
        return Arbiter(
            name = parts[1],
            id = if (parts.size > 2) parts[2].substring(1, parts[2].length - 1) else "",
            title = ArbiterTitle.from(parts[0])
        )
    }

    private fun extractLineCode(line: String): TournamentDataCode {
        val code = line.substring(0, LINE_CODE_LENGTH).trim()
        return TournamentDataCode
            .from(code)
            .getOrElse { TournamentDataCode.Unknown }
    }

    companion object {
        private const val LINE_CODE_LENGTH = 4

        object PlayerResult {
            const val RESULT_LENGTH = 10
        }

        object DateFormats {
            val START_END_DATE_FORMAT: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
            val ROUND_DATE_FORMAT: DateTimeFormatter = DateTimeFormatter.ofPattern("yy/MM/dd")
            val BIRTH_DATE_FORMAT: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd")
            val YEAR_ONLY_FORMAT: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy")
        }
    }
}