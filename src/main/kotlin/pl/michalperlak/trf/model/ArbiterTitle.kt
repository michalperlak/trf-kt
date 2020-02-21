package pl.michalperlak.trf.model

import arrow.core.Option

enum class ArbiterTitle(
    val id: String
) {
    FIDEArbiter("FA"),
    InternationalArbiter("IA");

    companion object {
        fun from(id: String): Option<ArbiterTitle> =
            Option.fromNullable(
                values()
                    .firstOrNull { it.id == id }
            )
    }
}