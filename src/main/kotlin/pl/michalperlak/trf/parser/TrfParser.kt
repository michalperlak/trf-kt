package pl.michalperlak.trf.parser

import arrow.core.Either
import arrow.core.getOrElse
import pl.michalperlak.trf.model.TournamentData
import pl.michalperlak.trf.parser.TournamentDataCode.*
import pl.michalperlak.trf.util.first
import pl.michalperlak.trf.util.firstInt
import pl.michalperlak.trf.util.of
import java.io.InputStream
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import java.time.LocalDate
import java.util.stream.Collectors

class TrfParser {
    private val startEndDateParser = StartEndDateParser(LocalDate.MIN)
    private val roundDatesParser = RoundDatesParser()
    private val playerDataParser = PlayerDataParser()
    private val federationParser = FederationParser()
    private val arbiterParser = ArbiterParser()
    private val teamDataParser = TeamDataParser()

    fun parse(trfFile: Path, charset: Charset = StandardCharsets.UTF_8): Either<Throwable, TournamentData> =
        Either.of {
            val lines = Files.readAllLines(trfFile, charset)
            parseLines(lines)
        }

    fun parse(inputStream: InputStream, charset: Charset = StandardCharsets.UTF_8): Either<Throwable, TournamentData> =
        Either.of {
            val lines = inputStream
                .bufferedReader(charset)
                .lines()
                .collect(Collectors.toList())
            parseLines(lines)
        }

    fun parse(lines: List<String>): Either<Throwable, TournamentData> = Either.of { parseLines(lines) }

    private fun parseLines(lines: List<String>): TournamentData {
        val data = lines
            .filter { it.length > LINE_CODE_LENGTH }
            .groupBy(::extractLineCode)
            .mapValues { it.value.map(::extractLineValue) }

        return TournamentData(
            name = data.first(TournamentName),
            city = data.first(City),
            federation = federationParser.parse(data[Federation]),
            startDate = startEndDateParser.parse(data[DateOfStart]),
            endDate = startEndDateParser.parse(data[DateOfEnd]),
            numberOfPlayers = data.firstInt(NumberOfPlayers),
            numberOfRatedPlayers = data.firstInt(NumberOfRatedPlayers),
            numberOfTeams = data.firstInt(NumberOfTeams),
            type = data.first(TypeOfTournament),
            chiefArbiter = arbiterParser.parseOne(data[ChiefArbiter]),
            deputyArbiters = arbiterParser.parse(data[DeputyChiefArbiter]),
            rateOfPlay = data.first(AllotedTimes),
            roundDates = roundDatesParser.parse(data[RoundDates]),
            playersData = playerDataParser.parse(data[PlayerData]),
            teamsData = teamDataParser.parse(data[TeamData])
        )
    }

    private fun extractLineValue(line: String): String =
        if (line.length > LINE_CODE_LENGTH) {
            line.substring(LINE_CODE_LENGTH)
        } else {
            ""
        }

    private fun extractLineCode(line: String): TournamentDataCode {
        val code = line.substring(0, LINE_CODE_LENGTH).trim()
        return TournamentDataCode
            .from(code)
            .getOrElse { Unknown }
    }

    private fun Map<TournamentDataCode, List<String>>.first(key: TournamentDataCode, default: String = ""): String =
        this[key]?.first(default) ?: default

    private fun Map<TournamentDataCode, List<String>>.firstInt(key: TournamentDataCode, default: Int = 0): Int =
        this[key]?.firstInt(default) ?: default

    companion object {
        private const val LINE_CODE_LENGTH = 4
    }
}