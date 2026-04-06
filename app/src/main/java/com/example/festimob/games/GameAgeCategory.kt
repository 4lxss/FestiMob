package com.example.festimob.games

/** Age group filters using [com.example.festimob.data.api.JeuDto.age_min] from the API. */
object GameAgeCategory {
    const val Kids = "Kids"
    const val Teenagers = "Teenagers"
    const val Adults = "Adults"

    val allOptions = listOf("All", Kids, Teenagers, Adults)

    /** Kids: [age_min] &lt; 12. Teenagers: 12…18. Adults: &gt; 18. Null [age_min] never matches a specific group. */
    fun matches(ageMin: Int?, category: String): Boolean {
        val age = ageMin ?: return false
        return when (category) {
            Kids -> age < 12
            Teenagers -> age in 12..18
            Adults -> age > 18
            else -> true
        }
    }
}
