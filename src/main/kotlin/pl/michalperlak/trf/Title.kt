package pl.michalperlak.trf

import arrow.core.Option
import arrow.core.toOption

enum class Title {
    GM, WGM, IM, WIM, FM, WFM, CM, WCM;

    companion object {
        fun from(id: String): Option<Title> =
            values()
                .firstOrNull { it.name.equals(id, ignoreCase = true) }
                .toOption()
    }
}
