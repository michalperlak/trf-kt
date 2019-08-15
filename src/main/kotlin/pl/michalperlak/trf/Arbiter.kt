package pl.michalperlak.trf

import arrow.core.Option

data class Arbiter(
    val name: String,
    val id: String,
    val title: Option<ArbiterTitle> = Option.empty()
)

enum class ArbiterTitle(
    val id: String
) {
    FIDEArbiter("FA"),
    InternationalArbiter("IA");

    companion object {
        fun from(id: String): Option<ArbiterTitle> = Option.fromNullable(
            values()
                .firstOrNull { it.id == id }
        )
    }
}
