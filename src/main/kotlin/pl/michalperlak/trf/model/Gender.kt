package pl.michalperlak.trf.model

import arrow.core.Option

enum class Gender {
    Male, Female;

    companion object {
        fun from(id: String): Option<Gender> =
            when (id) {
                "F", "f" -> Option.just(Female)
                "M", "m" -> Option.just(Male)
                else -> Option.empty()
            }
    }
}
