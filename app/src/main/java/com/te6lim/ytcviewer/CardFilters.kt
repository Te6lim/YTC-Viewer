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
    CardFilter(AttributeFilters.Dark.query),
    CardFilter(AttributeFilters.Earth.query),
    CardFilter(AttributeFilters.Fire.query),
    CardFilter(AttributeFilters.Light.query),
    CardFilter(AttributeFilters.Water.query),
    CardFilter(AttributeFilters.Wind.query),
    CardFilter(AttributeFilters.Divine.query)
)

val levelFilters = listOf(
    CardFilter(LevelFilters.One.query, query = "1"),
    CardFilter(LevelFilters.Two.query, query = "2"),
    CardFilter(LevelFilters.Three.query, query = "3"),
    CardFilter(LevelFilters.Four.query, query = ""),
    CardFilter(LevelFilters.Five.query, query = "5"),
    CardFilter(LevelFilters.Six.query, query = "6"),
    CardFilter(LevelFilters.Seven.query, query = "7"),
    CardFilter(LevelFilters.Eight.query, query = "8"),
    CardFilter(LevelFilters.Nine.query, query = "9"),
    CardFilter(LevelFilters.Ten.query, query = "10"),
    CardFilter(LevelFilters.Eleven.query, query = "11"),
    CardFilter(LevelFilters.Twelve.query, query = "12"),
    CardFilter(LevelFilters.Thirteen.query, query = "13"),
    CardFilter(LevelFilters.Unknown.query, query = "?")
)

val spellFilters = listOf(
    CardFilter(SpellFilters.Normal.query),
    CardFilter(SpellFilters.Field.query),
    CardFilter(SpellFilters.Equip.query),
    CardFilter(SpellFilters.Continuous.query),
    CardFilter(SpellFilters.Quick_Play.query),
    CardFilter(SpellFilters.Ritual.query)
)

val trapFilters = listOf(
    CardFilter(TrapFilters.Normal.query),
    CardFilter(TrapFilters.Continuous.query),
    CardFilter(TrapFilters.Counter.query)
)