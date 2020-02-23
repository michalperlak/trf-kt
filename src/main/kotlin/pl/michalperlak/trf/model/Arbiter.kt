package pl.michalperlak.trf.model

import arrow.core.Option

data class Arbiter(
    val name: String,
    val fideId: String,
    val title: Option<ArbiterTitle> = Option.empty()
)

