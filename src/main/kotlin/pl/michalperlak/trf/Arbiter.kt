package pl.michalperlak.trf

import arrow.core.Option

data class Arbiter(
    val name: String,
    val title: Option<ArbiterTitle> = Option.empty()
) {
    companion object {
        val UNKNOWN: Arbiter = Arbiter("UNKNOWN")
    }
}

enum class ArbiterTitle(val id: String) {
    FIDEArbiter("FA"), InternationalArbiter("IA")
}
