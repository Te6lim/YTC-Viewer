package com.te6lim.ytcviewer.resources

const val cardDetailsActivityIntentCardKey = "card"

enum class CardFilterCategory(val query: String) {
    Type("type"), Race("race"),
    Attribute("attribute"), Level("level"),
    Spell("race"), Trap("race");

    companion object {
        const val TypeArgumentForSpellCard = "spell card"
        const val TypeArgumentForTrapCard = "trap card"
        fun get(name: String) = valueOf(name)
    }
}

enum class TypeFilters(val query: String) {
    Effect_Monster("Effect Monster"),
    Flip_Effect_Monster("Flip Effect Monster"),
    Flip_Tuner_Effect_Monster("Flip Tuner Effect Monster"),
    Gemini_Monster("Gemini Monster"),
    Normal_Monster("Normal Monster"),
    Normal_Tuner_Monster("Normal Tuner Monster"),
    Pendulum_Effect_Monster("Pendulum Effect Monster"),
    Pendulum_Flip_Effect_Monster("Pendulum Flip Effect Monster"),
    Pendulum_Normal_Monster("Pendulum Normal Monster"),
    Pendulum_Tuner_Effect_Monster("Pendulum Tuner Effect Monster"),
    Ritual_Effect_Monster("Ritual Effect Monster"),
    Ritual_Monster("Ritual Monster"),
    Skill_Card("Skill Card"),
    Spirit_Monster("Spirit Monster"),
    Toon_Monster("Toon Monster"),
    Tuner_Monster("Tuner Monster"),
    Union_Effect_Monster("Union Effect Monster"),
    Fusion_Monster("Fusion Monster"),
    Link_Monster("Link Monster"),
    Pendulum_Effect_Fusion_Monster("Pendulum Effect Fusion Monster"),
    Synchro_Monster("Synchro Monster"),
    Synchro_Pendulum_Effect_Monster("Synchro Pendulum Effect Monster"),
    Synchro_Tuner_Monster("Synchro Tuner Monster"),
    XYZ_Monster("XYZ Monster"),
    XYZ_Pendulum_Effect_Monster("XYZ Pendulum Effect Monster")
}

enum class RaceFilters(val query: String) {
    Aqua("Aqua"),
    Beast("Beast"),
    Beast_Warrior("Beast-Warrior"),
    Creator_God("Creator-God"),
    Cyberse("Cyberse"),
    Dinosaur("Dinosaur"),
    Divine_Beast("Divine-Beast"),
    Dragon("Dragon"),
    Fairy("Fairy"),
    Fiend("Fiend"),
    Fish("Fish"),
    Insect("Insect"),
    Machine("Machine"),
    Plant("Plant"),
    Psychic("Psychic"),
    Pyro("Pyro"),
    Reptile("Reptile"),
    Rock("Rock"),
    Sea_Serpent("Sea Serpent"),
    Spellcaster("Spellcaster"),
    Thunder("Thunder"),
    Warrior("Warrior"),
    Winged_Beast("Winged Beast")
}

enum class AttributeFilters(val query: String) {
    Dark("DARK"),
    Earth("EARTH"),
    Fire("FIRE"),
    Light("LIGHT"),
    Water("WATER"),
    Wind("WIND"),
    Divine("DIVINE")
}

enum class LevelFilters(val query: String) {
    One("one"),
    Two("two"),
    Three("three"),
    Four("four"),
    Five("five"),
    Six("six"),
    Seven("seven"),
    Eight("eight"),
    Nine("nine"),
    Ten("ten"),
    Eleven("eleven"),
    Twelve("twelve"),
    Thirteen("thirteen"),
    Unknown("?")
}

enum class SpellFilters(val query: String) {
    Normal("Normal"),
    Field("Field"),
    Equip("Equip"),
    Continuous("Continuous"),
    Quick_Play("Quick-Play"),
    Ritual("Ritual")
}

enum class TrapFilters(val query: String) {
    Normal("Normal"),
    Continuous("Continuous"),
    Counter("Counter")
}