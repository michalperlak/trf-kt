package pl.michalperlak.trf

import arrow.core.Try
import java.io.InputStream
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import java.time.LocalDate
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
            .groupBy(this::extractLineCode)

        return TournamentData(
            name = data[TournamentDataCode.TournamentName]?.let { getName(it) } ?: "",
            city = data[TournamentDataCode.City]?.let { getCity(it) } ?: "",
            federation = data[TournamentDataCode.Federation]?.let { getFederation(it) } ?: Federation.UNKNOWN,
            startDate = data[TournamentDataCode.DateOfStart]?.let { getDate(it) } ?: LocalDate.now(),
            endDate = data[TournamentDataCode.DateOfEnd]?.let { getDate(it) } ?: LocalDate.now(),
            numberOfPlayers = data[TournamentDataCode.NumberOfPlayers]?.let { getValue(it) } ?: 0,
            numberOfRatedPlayers = data[TournamentDataCode.NumberOfRatedPlayers]?.let { getValue(it) } ?: 0,
            numberOfTeams = data[TournamentDataCode.NumberOfTeams]?.let { getValue(it) } ?: 0,
            type = data[TournamentDataCode.TypeOfTournament]?.let { getType(it) } ?: "",
            chiefArbiter = data[TournamentDataCode.ChiefArbiter]?.let { getArbiter(it) } ?: Arbiter.UNKNOWN,
            deputyArbiters = data[TournamentDataCode.DeputyChiefArbiter]?.map(this::getDeputyArbiter) ?: emptyList(),
            rateOfPlay = data[TournamentDataCode.AllotedTimes]?.let { getRateOfPlay(it) } ?: "",
            roundDates = emptyList(),
            playersData = emptyList(),
            teamsData = emptyList()
        )
    }

    private fun getName(nameLines: List<String>): String = nameLines
        .asSequence()
        .map { it.substring(4).trim() }
        .firstOrNull() ?: ""

    private fun getCity(cityLines: List<String>): String = cityLines
        .asSequence()
        .map { it.substring(4).trim() }
        .firstOrNull() ?: ""

    private fun getFederation(federationLines: List<String>): Federation = federationLines
        .asSequence()
        .map { Federation(it.substring(4).trim()) }
        .firstOrNull() ?: Federation.UNKNOWN

    private fun getDate(dateLines: List<String>): LocalDate = TODO()

    private fun getValue(valueLines: List<String>): Int = TODO()

    private fun getType(typeLines: List<String>): String = TODO()

    private fun getArbiter(arbiterLines: List<String>): Arbiter = TODO()

    private fun getDeputyArbiter(arbiterLine: String): Arbiter = TODO()

    private fun getRateOfPlay(rateLines: List<String>): String = TODO()

    private fun extractLineCode(line: String): TournamentDataCode {
        val code = line.substring(0, 4).trim()
        return TournamentDataCode.from(code)
    }
}