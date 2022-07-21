package com.te6lim.ytcviewer

import com.te6lim.ytcviewer.resources.*

val typeFiltersBackgrounds = hashMapOf(
    Pair(TypeFilters.Effect_Monster.query, R.color.effectMonster),
    Pair(TypeFilters.Flip_Effect_Monster.query, R.color.flipEffectMonster),
    Pair(TypeFilters.Flip_Tuner_Effect_Monster.query, R.color.flipEffectMonster),
    Pair(TypeFilters.Gemini_Monster.query, R.color.geminiMonster),
    Pair(TypeFilters.Normal_Monster.query, R.color.normalMonster),
    Pair(TypeFilters.Normal_Tuner_Monster.query, R.color.normalMonster),
    Pair(TypeFilters.Pendulum_Effect_Monster.query, R.color.effectMonster),
    Pair(TypeFilters.Pendulum_Flip_Effect_Monster.query, R.color.flipEffectMonster),
    Pair(TypeFilters.Pendulum_Normal_Monster.query, R.color.normalMonster),
    Pair(TypeFilters.Pendulum_Tuner_Effect_Monster.query, R.color.effectMonster),
    Pair(TypeFilters.Ritual_Effect_Monster.query, R.color.ritualMonster),
    Pair(TypeFilters.Ritual_Monster.query, R.color.ritualMonster),
    Pair(TypeFilters.Skill_Card.query, R.color.skillCard),
    Pair(TypeFilters.Spirit_Monster.query, R.color.effectMonster),
    Pair(TypeFilters.Toon_Monster.query, R.color.toon),
    Pair(TypeFilters.Tuner_Monster.query, R.color.effectMonster),
    Pair(TypeFilters.Union_Effect_Monster.query, R.color.effectMonster),
    Pair(TypeFilters.Fusion_Monster.query, R.color.fusionMonster),
    Pair(TypeFilters.Link_Monster.query, R.color.linkedMonster),
    Pair(TypeFilters.Pendulum_Effect_Fusion_Monster.query, R.color.pendulumEffectFusionMonster),
    Pair(TypeFilters.Synchro_Monster.query, R.color.synchroMonster),
    Pair(TypeFilters.Synchro_Pendulum_Effect_Monster.query, R.color.synchroPendulumEFFectMonster),
    Pair(TypeFilters.Synchro_Tuner_Monster.query, R.color.synchroTunerMonster),
    Pair(TypeFilters.XYZ_Monster.query, R.color.xyzMonster),
    Pair(TypeFilters.XYZ_Pendulum_Effect_Monster.query, R.color.xyzPendulumEffectMonster)
)

val raceFiltersBackgrounds = hashMapOf(
    Pair(RaceFilters.Aqua.query, R.color.aqua),
    Pair(RaceFilters.Beast.query, R.color.beast),
    Pair(RaceFilters.Beast_Warrior.query, R.color.warrior),
    Pair(RaceFilters.Creator_God.query, R.color.creatorGod),
    Pair(RaceFilters.Cyberse.query, R.color.cyberse),
    Pair(RaceFilters.Dinosaur.query, R.color.dinosaur),
    Pair(RaceFilters.Divine_Beast.query, R.color.divineBeast),
    Pair(RaceFilters.Dragon.query, R.color.dragon),
    Pair(RaceFilters.Fairy.query, R.color.fairy),
    Pair(RaceFilters.Fiend.query, R.color.fiend),
    Pair(RaceFilters.Fish.query, R.color.fish),
    Pair(RaceFilters.Insect.query, R.color.insect),
    Pair(RaceFilters.Machine.query, R.color.machine),
    Pair(RaceFilters.Plant.query, R.color.plant),
    Pair(RaceFilters.Psychic.query, R.color.psychic),
    Pair(RaceFilters.Pyro.query, R.color.pyro),
    Pair(RaceFilters.Reptile.query, R.color.reptile),
    Pair(RaceFilters.Rock.query, R.color.rock),
    Pair(RaceFilters.Sea_Serpent.query, R.color.fish),
    Pair(RaceFilters.Spellcaster.query, R.color.spellCaster),
    Pair(RaceFilters.Thunder.query, R.color.lightning),
    Pair(RaceFilters.Warrior.query, R.color.warrior),
    Pair(RaceFilters.Winged_Beast.query, R.color.beast),
    Pair("Wyrm", R.color.dragon)
)

val attributeBackgrounds = hashMapOf(
    Pair(AttributeFilters.Dark.query, R.color.dark),
    Pair(AttributeFilters.Earth.query, R.color.earth),
    Pair(AttributeFilters.Fire.query, R.color.fire),
    Pair(AttributeFilters.Light.query, R.color.light),
    Pair(AttributeFilters.Water.query, R.color.water),
    Pair(AttributeFilters.Wind.query, R.color.wind),
    Pair(AttributeFilters.Divine.query, R.color.divine)
)

val levelFiltersBackgrounds = hashMapOf(
    Pair(LevelFilters.One.query, R.color.fade),
    Pair(LevelFilters.Two.query, R.color.fade),
    Pair(LevelFilters.Three.query, R.color.fade),
    Pair(LevelFilters.Four.query, R.color.fade),
    Pair(LevelFilters.Five.query, R.color.fade),
    Pair(LevelFilters.Six.query, R.color.fade),
    Pair(LevelFilters.Seven.query, R.color.fade),
    Pair(LevelFilters.Eight.query, R.color.fade),
    Pair(LevelFilters.Nine.query, R.color.fade),
    Pair(LevelFilters.Ten.query, R.color.fade),
    Pair(LevelFilters.Eleven.query, R.color.fade),
    Pair(LevelFilters.Twelve.query, R.color.fade),
    Pair(LevelFilters.Thirteen.query, R.color.fade),
    Pair(LevelFilters.Unknown.query, R.color.fade),
)

val spellFiltersBackgrounds = hashMapOf(
    Pair(SpellFilters.Normal.query, R.color.normalSpell),
    Pair(SpellFilters.Field.query, R.color.field),
    Pair(SpellFilters.Equip.query, R.color.equip),
    Pair(SpellFilters.Continuous.query, R.color.continuousSpell),
    Pair(SpellFilters.Quick_Play.query, R.color.quickPlay),
    Pair(SpellFilters.Ritual.query, R.color.ritual)
)

val trapFiltersBackgrounds = hashMapOf(
    Pair(TrapFilters.Normal.query, R.color.normalTrap),
    Pair(TrapFilters.Continuous.query, R.color.continuous),
    Pair(TrapFilters.Counter.query, R.color.counter)
)