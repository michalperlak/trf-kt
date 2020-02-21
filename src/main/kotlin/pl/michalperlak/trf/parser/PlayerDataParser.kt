package pl.michalperlak.trf.parser

import arrow.core.Option
import arrow.core.getOrElse
import pl.michalperlak.trf.model.*
import pl.michalperlak.trf.util.parseDate
import java.time.LocalDate

class PlayerDataParser {

    fun parse(playerLine: String): PlayerData {
        println(playerLine)
        return PlayerData(
            startRank = playerLine.substring(0, 4).trim().toInt(),
            gender = Gender.from(playerLine[5]),
            title = Title.from(playerLine.substring(6, 9).trim()),
            name = playerLine.substring(10, 43).trim(),
            fideRating = playerLine.substring(44, 48).trim().toInt(),
            federation = Federation(playerLine.substring(49, 52).trim()),
            fideId = playerLine.substring(53, 64).trim().toLong(),
            birthDate = getBirthDate(playerLine.substring(65, 75).trim()),
            points = playerLine.substring(76, 80).trim().toDouble(),
            rank = playerLine.substring(81, 85).trim().toInt(),
            results = getPlayerResults(playerLine.substring(87))
        )
    }

    private fun getBirthDate(formattedDate: String): Option<LocalDate> =
        parseDate(formattedDate, TrfParser.Companion.DateFormats.BIRTH_DATE_FORMAT, TrfParser.Companion.DateFormats.YEAR_ONLY_FORMAT)

    private fun getPlayerResults(results: String): List<PlayerGameResult> {
        tailrec fun readPlayerResult(
            readResults: List<PlayerGameResult>,
            missingResults: String
        ): List<PlayerGameResult> =
            if (missingResults.isBlank()) {
                readResults
            } else {
                val resultString = missingResults.take(TrfParser.Companion.PlayerResult.RESULT_LENGTH)
                val result = PlayerGameResult(
                    opponentId = PlayerId(resultString.substring(0, 5).trim()),
                    gameColor = GameColor.from(resultString.substring(5, 7).trim())
                        .getOrElse { GameColor.NotPaired },
                    result = GameResult.from(resultString.substring(7).trim())
                        .getOrElse { GameResult.ZeroPointBye }
                )
                readPlayerResult(readResults + result, missingResults.drop(10))
            }
        return readPlayerResult(mutableListOf(), results)
    }
}