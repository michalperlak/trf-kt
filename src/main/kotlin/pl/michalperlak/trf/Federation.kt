package pl.michalperlak.trf

data class Federation(val name: String) {
    companion object {
        val UNKNOWN: Federation = Federation("")
    }
}
