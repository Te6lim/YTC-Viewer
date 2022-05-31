package com.te6lim.ytcviewer

import com.te6lim.ytcviewer.filters.CardFilter

val typeFilters = listOf(
    CardFilter(TypeFilters.Effect_Monster.query),
    CardFilter(TypeFilters.Flip_Effect_Monster.query),
    CardFilter(TypeFilters.Flip_Tuner_Effect_Monster.query),
    CardFilter(TypeFilters.Gemini_Monster.query),
    CardFilter(TypeFilters.Normal_Monster.query),
    CardFilter(TypeFilters.Normal_Tuner_Monster.query),
    CardFilter(TypeFilters.Pendulum_Effect_Monster.query),
    CardFilter(TypeFilters.Pendulum_Flip_Effect_Monster.query),
    CardFilter(TypeFilters.Pendulum_Normal_Monster.query),
    CardFilter(TypeFilters.Pendulum_Tuner_Effect_Monster.query),
    CardFilter(TypeFilters.Ritual_Effect_Monster.query),
    CardFilter(TypeFilters.Ritual_Monster.query),
    CardFilter(TypeFilters.Skill_Card.query),
    CardFilter(TypeFilters.Spirit_Monster.query),
    CardFilter(TypeFilters.Toon_Monster.query),
    CardFilter(TypeFilters.Tuner_Monster.query),
    CardFilter(TypeFilters.Union_Effect_Monster.query),
    CardFilter(TypeFilters.Fusion_Monster.query),
    CardFilter(TypeFilters.Link_Monster.query),
    CardFilter(TypeFilters.Pendulum_Effect_Fusion_Monster.query),
    CardFilter(TypeFilters.Synchro_Monster.query),
    CardFilter(TypeFilters.Synchro_Pendulum_Effect_Monster.query),
    CardFilter(TypeFilters.Synchro_Tuner_Monster.query),
    CardFilter(TypeFilters.XYZ_Monster.query),
    CardFilter(TypeFilters.XYZ_Pendulum_Effect_Monster.query)
)

val raceFilters = listOf(
    CardFilter(RaceFilters.Aqua.query),
    CardFilter(RaceFilters.Beast.query),
    CardFilter(RaceFilters.Beast_Warrior.query),
    CardFilter(RaceFilters.Creator_God.query),
    CardFilter(RaceFilters.Cyberse.query),
    CardFilter(RaceFilters.Dinosaur.query),
    CardFilter(RaceFilters.Divine_Beast.query),
    CardFilter(RaceFilters.Dragon.query),
    CardFilter(RaceFilters.Fairy.query),
    CardFilter(RaceFilters.Fiend.query),
    CardFilter(RaceFilters.Fish.query),
    CardFilter(RaceFilters.Insect.query),
    CardFilter(RaceFilters.Machine.query),
    CardFilter(RaceFilters.Plant.query),
    CardFilter(RaceFilters.Psychic.query),
    CardFilter(RaceFilters.Pyro.query),
    CardFilter(RaceFilters.Reptile.query),
    CardFilter(RaceFilters.Rock.query),
    CardFilter(RaceFilters.Sea_Serpent.query),
    CardFilter(RaceFilters.Spellcaster.query),
    CardFilter(RaceFilters.Thunder.query),
    CardFilter(RaceFilters.Warrior.query),
    CardFilter(RaceFilters.Winged_Beast.query)
)

val attributeFilters = listOf(
    CardFilter("dark"),
    CardFilter("earth"),
    CardFilter("fire"),
    CardFilter("light"),
    CardFilter("water"),
    CardFilter("wind"),
    CardFilter("divine")
)

val levelFilters = listOf(
    CardFilter("one", query = "1"),
    CardFilter("two", query = "2"),
    CardFilter("three", query = "3"),
    CardFilter("four", query = ""),
    CardFilter("five", query = "5"),
    CardFilter("six", query = "6"),
    CardFilter("seven", query = "7"),
    CardFilter("eight", query = "8"),
    CardFilter("nine", query = "9"),
    CardFilter("ten", query = "10"),
    CardFilter("eleven", query = "11"),
    CardFilter("twelve", query = "12"),
    CardFilter("thirteen", query = "13"),
    CardFilter("?", query = "?")
)

val spellFilters = listOf(
    CardFilter("Normal"),
    CardFilter("Field"),
    CardFilter("Equip"),
    CardFilter("Continuous"),
    CardFilter("Quick-Play"),
    CardFilter("Ritual")
)

val trapFilters = listOf(
    CardFilter("Normal"),
    CardFilter("Continuous"),
    CardFilter("Counter")
)