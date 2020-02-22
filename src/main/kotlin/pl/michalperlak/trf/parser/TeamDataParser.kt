package pl.michalperlak.trf.parser

import pl.michalperlak.trf.model.PlayerId
import pl.michalperlak.trf.model.TeamData
import pl.michalperlak.trf.util.splitByWhitespace

internal class TeamDataParser : EntryParser<List<TeamData>> {
    override fun parse(values: List<String>?): List<TeamData> =
        values?.map(::parseTeamData) ?: emptyList()

    private fun parseTeamData(teamLine: String): TeamData {
        val parts = teamLine.splitByWhitespace()
        val teamName = parts[0]
        tailrec fun readTeamMember(teamLineParts: List<String>, teamMembers: List<PlayerId>): List<PlayerId> =
            if (teamLineParts.isEmpty()) teamMembers
            else readTeamMember(
                teamLineParts.drop(1), teamMembers + PlayerId(teamLineParts.first().trim())
            )
        val players = readTeamMember(parts.drop(1), emptyList())
        return TeamData(
            name = teamName,
            players = players
        )
    }
}