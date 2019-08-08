package pl.michalperlak.trf

import arrow.core.Try
import java.io.InputStream
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.nio.file.Path

class TrfParser {
    fun parse(trfFile: Path, charset: Charset = StandardCharsets.UTF_8): Try<TournamentData> = TODO()
    fun parse(inputStream: InputStream, charset: Charset = StandardCharsets.UTF_8): Try<TournamentData> = TODO()
}