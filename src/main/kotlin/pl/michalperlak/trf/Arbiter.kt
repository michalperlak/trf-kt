package pl.michalperlak.trf

import arrow.core.Option

data class Arbiter(
    val name: String,
    val title: Option<ArbiterTitle>
)

enum class ArbiterTitle {
    FIDEArbiter, InternationalArbiter
}
