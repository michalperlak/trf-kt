package pl.michalperlak.trf

import java.time.LocalDate

data class PlayerData(
    val id: Int,
    val startNumber: Int,
    val gender: Gender,
    val title: Title,
    val name: String,
    val fideRating: Int,
    val federation: Federation,
    val fideId: Long,
    val birthDate: LocalDate,
    val points: Points
) {
    companion object {
        fun parse(line: String): PlayerData = TODO()
    }
}