package pl.michalperlak.trf.model

import arrow.core.Option

data class Arbiter(
    val name: String,
    val id: String,
    val title: Option<ArbiterTitle> = Option.empty()
)

