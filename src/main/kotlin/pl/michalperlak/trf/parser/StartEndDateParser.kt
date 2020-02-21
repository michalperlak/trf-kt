package pl.michalperlak.trf.parser

import pl.michalperlak.trf.model.TournamentDataCode
import java.time.LocalDate
import java.util.*

class StartEndDateParser(
    private val defaultValue: LocalDate
) : EntryParser<LocalDate> {
    override val codes: Set<TournamentDataCode>
        get() = EnumSet.of(TournamentDataCode.DateOfStart, TournamentDataCode.DateOfEnd)

    override fun parse(values: List<String>?): LocalDate {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun getDate(dateLine: String): LocalDate = LocalDate.parse(
        dateLine,
        TrfParser.Companion.DateFormats.START_END_DATE_FORMAT
    )
}