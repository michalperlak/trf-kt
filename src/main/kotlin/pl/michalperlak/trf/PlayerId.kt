package pl.michalperlak.trf

class PlayerId(
    val id: String
) {

    companion object {
        val HALF_POINT_BYE = PlayerId("H")
        val FULL_POINT_BYE = PlayerId("F")
        val PAIRING_ALLOCATED_BYE = PlayerId("U")
        val ZERO_POINT_BYE = PlayerId("Z")
    }
}
