package com.te6lim.ytcviewer.filters

import android.os.Parcelable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.te6lim.ytcviewer.R
import kotlinx.parcelize.Parcelize

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

class FilterSelectionViewModel(private val category: String) : ViewModel() {

    companion object {

        private fun getFilterBackgrounds(category: CardFilterCategory): HashMap<String, Int> {
            return when (category) {
                CardFilterCategory.Type -> hashMapOf(
                    Pair("Effect Monster", R.color.effectMonster),
                    Pair("Flip Effect Monster", R.color.flipEffectMonster),
                    Pair("Flip Tuner Effect Monster", R.color.flipEffectMonster),
                    Pair("Gemini Monster", R.color.geminiMonster),
                    Pair("Normal Monster", R.color.normalMonster),
                    Pair("Normal Tuner Monster", R.color.normalMonster),
                    Pair("Pendulum Effect Monster", R.color.effectMonster),
                    Pair("Pendulum Flip Effect Monster", R.color.flipEffectMonster),
                    Pair("Pendulum Normal Monster", R.color.normalMonster),
                    Pair("Pendulum Tuner Effect Monster", R.color.effectMonster),
                    Pair("Ritual Effect Monster", R.color.ritualMonster),
                    Pair("Ritual Monster", R.color.ritualMonster),
                    Pair("Skill Card", R.color.skillCard),
                    Pair("Spirit Monster", R.color.effectMonster),
                    Pair("Toon Monster", R.color.toon),
                    Pair("Tuner Monster", R.color.effectMonster),
                    Pair("Union Effect Monster", R.color.effectMonster),
                    Pair("Fusion Monster", R.color.fusionMonster),
                    Pair("Link Monster", R.color.linkedMonster),
                    Pair("Pendulum Effect Fusion Monster", R.color.pendulumEffectFusionMonster),
                    Pair("Synchro Monster", R.color.synchroMonster),
                    Pair("Synchro Pendulum Effect Monster", R.color.synchroPendulumEFFectMonster),
                    Pair("Synchro Tuner Monster", R.color.synchroTunerMonster),
                    Pair("XYZ Monster", R.color.xyzMonster),
                    Pair("XYZ Pendulum Effect Monster", R.color.xyzPendulumEffectMonster)
                )

                CardFilterCategory.Race -> hashMapOf(
                    Pair("Aqua", R.color.aqua),
                    Pair("Beast", R.color.beast),
                    Pair("Beast-Warrior", R.color.warrior),
                    Pair("Creator-God", R.color.creatorGod),
                    Pair("Cyberse", R.color.cyberse),
                    Pair("Dinosaur", R.color.dinosaur),
                    Pair("Divine-Beast", R.color.divineBeast),
                    Pair("Dragon", R.color.dragon),
                    Pair("Fairy", R.color.fairy),
                    Pair("Fiend", R.color.fiend),
                    Pair("Fish", R.color.fish),
                    Pair("Insect", R.color.insect),
                    Pair("Machine", R.color.machine),
                    Pair("Plant", R.color.plant),
                    Pair("Psychic", R.color.psychic),
                    Pair("Pyro", R.color.pyro),
                    Pair("Reptile", R.color.reptile),
                    Pair("Rock", R.color.rock),
                    Pair("Sea Serpent", R.color.fish),
                    Pair("Spellcaster", R.color.spellCaster),
                    Pair("Thunder", R.color.lightning),
                    Pair("Warrior", R.color.warrior),
                    Pair("Winged Beast", R.color.beast),
                    Pair("Wyrm", R.color.dragon)
                )

                CardFilterCategory.Attribute -> hashMapOf(
                    Pair("dark", R.color.dark),
                    Pair("earth", R.color.earth),
                    Pair("fire", R.color.fire),
                    Pair("light", R.color.light),
                    Pair("water", R.color.water),
                    Pair("wind", R.color.wind),
                    Pair("divine", R.color.divine)
                )

                CardFilterCategory.Level -> hashMapOf()

                CardFilterCategory.Spell -> hashMapOf(
                    Pair("Normal", R.color.normalSpell),
                    Pair("Field", R.color.field),
                    Pair("Equip", R.color.equip),
                    Pair("Continuous", R.color.continuousSpell),
                    Pair("Quick-Play", R.color.quickPlay),
                    Pair("Ritual", R.color.ritual)
                )

                CardFilterCategory.Trap -> hashMapOf(
                    Pair("Normal", R.color.normalTrap),
                    Pair("Continuous", R.color.continuous),
                    Pair("Counter", R.color.counter)
                )
            }
        }

        fun getRaceIconResource() = hashMapOf(
            Pair("Aqua", R.drawable.aqua),
            Pair("Beast", R.drawable.beast),
            Pair("Beast-Warrior", R.drawable.beast_warrior),
            Pair("Creator-God", R.drawable.divine),
            Pair("Cyberse", R.drawable.machine),
            Pair("Dinosaur", R.drawable.dinosaur),
            Pair("Divine-Beast", R.drawable.divine_beast),
            Pair("Dragon", R.drawable.dragon),
            Pair("Fairy", R.drawable.fairy),
            Pair("Fiend", R.drawable.fiend),
            Pair("Fish", R.drawable.fish),
            Pair("Insect", R.drawable.insect),
            Pair("Machine", R.drawable.machine),
            Pair("Plant", R.drawable.plant),
            Pair("Psychic", R.drawable.psychic),
            Pair("Pyro", R.drawable.pyro),
            Pair("Reptile", R.drawable.reptile),
            Pair("Rock", R.drawable.rock),
            Pair("Sea Serpent", R.drawable.sea_serpent),
            Pair("Spellcaster", R.drawable.spellcaster),
            Pair("Thunder", R.drawable.thunder),
            Pair("Warrior", R.drawable.warrior),
            Pair("Winged Beast", R.drawable.winged_beast),
            Pair("Wyrm", R.drawable.dragon),
            Pair("Normal", R.drawable.normal),
            Pair("Field", R.drawable.field),
            Pair("Equip", R.drawable.equip),
            Pair("Continuous", R.drawable.continuous),
            Pair("Counter", R.drawable.counter),
            Pair("Quick-Play", R.drawable.quick_play),
            Pair("Ritual", R.drawable.ritual),
        )

        fun getAttributeIconResource() = hashMapOf(
            Pair("DARK", R.drawable.dark),
            Pair("EARTH", R.drawable.earth),
            Pair("FIRE", R.drawable.fire),
            Pair("LIGHT", R.drawable.light),
            Pair("WATER", R.drawable.water),
            Pair("WIND", R.drawable.wind),
            Pair("DIVINE", R.drawable.divine)
        )

        fun getLevelOrRankIcon(type: String): Int {
            return if (type == "XYZ Monster") R.drawable.rank
            else R.drawable.level
        }
    }

    private val types = listOf(
        CardFilter("Effect Monster"),
        CardFilter("Flip Effect Monster"),
        CardFilter("Flip Tuner Effect Monster"),
        CardFilter("Gemini Monster"),
        CardFilter("Normal Monster"),
        CardFilter("Normal Tuner Monster"),
        CardFilter("Pendulum Effect Monster"),
        CardFilter("Pendulum Flip Effect Monster"),
        CardFilter("Pendulum Normal Monster"),
        CardFilter("Pendulum Tuner Effect Monster"),
        CardFilter("Ritual Effect Monster"),
        CardFilter("Ritual Monster"),
        CardFilter("Skill Card"),
        CardFilter("Spirit Monster"),
        CardFilter("Toon Monster"),
        CardFilter("Tuner Monster"),
        CardFilter("Union Effect Monster"),
        CardFilter("Fusion Monster"),
        CardFilter("Link Monster"),
        CardFilter("Pendulum Effect Fusion Monster"),
        CardFilter("Synchro Monster"),
        CardFilter("Synchro Pendulum Effect Monster"),
        CardFilter("Synchro Tuner Monster"),
        CardFilter("XYZ Monster"),
        CardFilter("XYZ Pendulum Effect Monster")
    )

    private val races = listOf(
        CardFilter("Aqua"),
        CardFilter("Beast"),
        CardFilter("Beast-Warrior"),
        CardFilter("Creator-God"),
        CardFilter("Cyberse"),
        CardFilter("Dinosaur"),
        CardFilter("Divine-Beast"),
        CardFilter("Dragon"),
        CardFilter("Fairy"),
        CardFilter("Fiend"),
        CardFilter("Fish"),
        CardFilter("Insect"),
        CardFilter("Machine"),
        CardFilter("Plant"),
        CardFilter("Psychic"),
        CardFilter("Pyro"),
        CardFilter("Reptile"),
        CardFilter("Rock"),
        CardFilter("Sea Serpent"),
        CardFilter("Spellcaster"),
        CardFilter("Thunder"),
        CardFilter("Warrior"),
        CardFilter("Winged Beast")
    )

    private val attributes = listOf(
        CardFilter("dark"),
        CardFilter("earth"),
        CardFilter("fire"),
        CardFilter("light"),
        CardFilter("water"),
        CardFilter("wind"),
        CardFilter("divine")
    )

    private val levels = listOf(
        CardFilter("one"),
        CardFilter("two"),
        CardFilter("three"),
        CardFilter("four"),
        CardFilter("five"),
        CardFilter("six"),
        CardFilter("seven"),
        CardFilter("eight"),
        CardFilter("nine"),
        CardFilter("ten"),
        CardFilter("eleven"),
        CardFilter("twelve"),
        CardFilter("?")
    )

    private val spells = listOf(
        CardFilter("Normal"),
        CardFilter("Field"),
        CardFilter("Equip"),
        CardFilter("Continuous"),
        CardFilter("Quick-Play"),
        CardFilter("Ritual")
    )

    private val traps = listOf(
        CardFilter("Normal"),
        CardFilter("Continuous"),
        CardFilter("Counter")
    )

    private val _filterCategory = MutableLiveData<String>()
    val filterCategory: LiveData<String>
        get() = _filterCategory

    private val _filters = MutableLiveData<List<CardFilter>>(listOf())
    val filters: LiveData<List<CardFilter>>
        get() = _filters

    private val selectedFilters = mutableListOf<CardFilter>()

    init {
        _filterCategory.value = category
        _filters.value = getFilters(CardFilterCategory.valueOf(category))
    }

    fun getBackgroundsForFilters(): HashMap<String, Int> {
        return getFilterBackgrounds(CardFilterCategory.valueOf(category))
    }

    private fun getFilters(category: CardFilterCategory): List<CardFilter> {
        return when (category) {
            CardFilterCategory.Type -> types

            CardFilterCategory.Race -> races

            CardFilterCategory.Attribute -> attributes

            CardFilterCategory.Level -> levels

            CardFilterCategory.Spell -> spells

            CardFilterCategory.Trap -> traps
        }
    }

    fun addFilterToSelected(filter: CardFilter) {
        selectedFilters.add(filter)
    }

    fun removeFilterFromSelected(filter: CardFilter) {
        selectedFilters.remove(filter)
    }

    fun selectedFilters(): List<CardFilter> {
        return selectedFilters.toList()
    }

}

@Parcelize
data class CardFilter(val name: String, var isSelected: Boolean = false, val iconResource: Int? = null) :
    Parcelable {
    val isEffectMonster = name != "Normal Monster" && name != "Normal Tuner Monster"
            && name != "Pendulum Normal Monster"
}

@Suppress("UNCHECKED_CAST")
class FilterSelectionViewModelFactory(private val category: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FilterSelectionViewModel::class.java))
            return FilterSelectionViewModel(category) as T
        else throw IllegalArgumentException("Unknown ViewModel class")
    }
}