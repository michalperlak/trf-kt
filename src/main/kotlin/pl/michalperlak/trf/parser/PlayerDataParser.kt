package pl.michalperlak.trf.parser

import arrow.core.Option
import pl.michalperlak.trf.model.*
import pl.michalperlak.trf.parser.PlayerDataParser.PlayerOffsets.BIRTH_DATE_OFFSET
import pl.michalperlak.trf.parser.PlayerDataParser.PlayerOffsets.FEDERATION_OFFSET
import pl.michalperlak.trf.parser.PlayerDataParser.PlayerOffsets.FIDE_ID_OFFSET
import pl.michalperlak.trf.parser.PlayerDataParser.PlayerOffsets.FIDE_RATING_OFFSET
import pl.michalperlak.trf.parser.PlayerDataParser.PlayerOffsets.GENDER_OFFSET
import pl.michalperlak.trf.parser.PlayerDataParser.PlayerOffsets.NAME_OFFSET
import pl.michalperlak.trf.parser.PlayerDataParser.PlayerOffsets.POINTS_OFFSET
import pl.michalperlak.trf.parser.PlayerDataParser.PlayerOffsets.RANK_OFFSET
import pl.michalperlak.trf.parser.PlayerDataParser.PlayerOffsets.RESULTS_OFFSET
import pl.michalperlak.trf.parser.PlayerDataParser.PlayerOffsets.START_RANK_OFFSET
import pl.michalperlak.trf.parser.PlayerDataParser.PlayerOffsets.TITLE_OFFSET
import pl.michalperlak.trf.parser.PlayerDataParser.ResultsOffsets.GAME_COLOR_OFFSET
import pl.michalperlak.trf.parser.PlayerDataParser.ResultsOffsets.GAME_RESULT_OFFSET
import pl.michalperlak.trf.parser.PlayerDataParser.ResultsOffsets.OPPONENT_ID_OFFSET
import pl.michalperlak.trf.parser.PlayerDataParser.ResultsOffsets.SINGLE_RESULT_LENGTH
import pl.michalperlak.trf.util.parseDate
import java.time.LocalDate
import java.time.format.DateTimeFormatter

internal class PlayerDataParser : EntryParser<List<PlayerData>> {
    override fun parse(values: List<String>?): List<PlayerData> =
        values?.map(::parsePlayerData) ?: emptyList()

    private fun parsePlayerData(playerLine: String): PlayerData {
        return PlayerData(
            startRank = START_RANK_OFFSET.getValue(playerLine).toInt(),
            gender = Gender.from(GENDER_OFFSET.getValue(playerLine)),
            title = Title.from(TITLE_OFFSET.getValue(playerLine)),
            name = NAME_OFFSET.getValue(playerLine),
            fideRating = FIDE_RATING_OFFSET.getValue(playerLine).toInt(),
            federation = Federation(FEDERATION_OFFSET.getValue(playerLine)),
            fideId = FIDE_ID_OFFSET.getValue(playerLine).toLong(),
            birthDate = getBirthDate(BIRTH_DATE_OFFSET.getValue(playerLine)),
            points = POINTS_OFFSET.getValue(playerLine).toDouble(),
            rank = RANK_OFFSET.getValue(playerLine).toInt(),
            results = getPlayerResults(RESULTS_OFFSET.getValue(playerLine))
        )
    }

    private fun getBirthDate(formattedDate: String): Option<LocalDate> =
        parseDate(formattedDate, BirthDateFormats.PRIMARY_BIRTH_DATE_FORMAT, BirthDateFormats.YEAR_ONLY_FORMAT)

    private fun getPlayerResults(results: String): List<PlayerGameResult> {
        tailrec fun readPlayerResult(
            readResults: List<PlayerGameResult>,
            missingResults: String
        ): List<PlayerGameResult> =
            if (missingResults.isBlank()) {
                readResults
            } else {
                val singleResult = missingResults.take(SINGLE_RESULT_LENGTH)
                val result = PlayerGameResult(
                    opponentId = PlayerId(OPPONENT_ID_OFFSET.getValue(singleResult)),
                    gameColor = GameColor.from(GAME_COLOR_OFFSET.getValue(singleResult)),
                    result = GameResult.from(GAME_RESULT_OFFSET.getValue(singleResult))
                )
                readPlayerResult(readResults + result, missingResults.drop(SINGLE_RESULT_LENGTH))
            }
        return readPlayerResult(emptyList(), results)
    }

    object PlayerOffsets {
        val START_RANK_OFFSET = Offset(0, 4)
        val GENDER_OFFSET = Offset(5, 6)
        val TITLE_OFFSET = Offset(6, 9)
        val NAME_OFFSET = Offset(10, 43)
        val FIDE_RATING_OFFSET = Offset(44, 48)
        val FEDERATION_OFFSET = Offset(49, 52)
        val FIDE_ID_OFFSET = Offset(53, 64)
        val BIRTH_DATE_OFFSET = Offset(65, 75)
        val POINTS_OFFSET = Offset(76, 80)
        val RANK_OFFSET = Offset(81, 85)
        val RESULTS_OFFSET = Offset(86, Int.MAX_VALUE)
    }

    object ResultsOffsets {
        const val SINGLE_RESULT_LENGTH = 10
        val OPPONENT_ID_OFFSET = Offset(0, 5)
        val GAME_COLOR_OFFSET = Offset(5, 7)
        val GAME_RESULT_OFFSET = Offset(7, 10)
    }

    object BirthDateFormats {
        val PRIMARY_BIRTH_DATE_FORMAT: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd")
        val YEAR_ONLY_FORMAT: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy")
    }
}