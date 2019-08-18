package pl.michalperlak.trf

import arrow.core.Option

data class Arbiter(
    val name: String,
    val id: String,
    val title: Option<ArbiterTitle> = Option.empty()
)

