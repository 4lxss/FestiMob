package com.example.festimob.data.api.games

// Maps filter labels to API age_min: we don't change the API, we just decide if a jeu belongs in Kids / Teenagers / Adults.
object GameAgeCategory {
    const val Kids = "Kids"
    const val Teenagers = "Teenagers"
    const val Adults = "Adults"

    val allOptions = listOf("All", Kids, Teenagers, Adults)

    // If the API didn't send age_min, we don't put that game in Kids/Teenagers/Adults — only in "All".
    fun matches(ageMin: Int?, category: String): Boolean {
        val age = ageMin ?: return false
        // Kids < 12, Teenagers 12–18, Adults > 18; "All" isn't passed here as a filter bucket.
        return when (category) {
            Kids -> age < 12
            Teenagers -> age in 12..18
            Adults -> age > 18
            else -> true
        }
    }
}
