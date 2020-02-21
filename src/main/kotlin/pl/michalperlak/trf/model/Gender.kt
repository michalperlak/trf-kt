package pl.michalperlak.trf.model

enum class Gender {
    Male, Female;

    companion object {
        fun from(id: Char): Gender =
            if (id == 'F' || id == 'f') Female
            else Male
    }
}
