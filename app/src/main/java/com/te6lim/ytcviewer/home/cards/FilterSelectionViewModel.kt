package com.te6lim.ytcviewer.home.cards

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.te6lim.ytcviewer.R

class FilterSelectionViewModel(private val category: String, private val type: String) :
    ViewModel() {

    enum class CardFilterType {
        Monster, NonMonster;
    }

    enum class CardFilterCategory {
        Type, Race, Attribute;
    }

    enum class NonMonsterCardFilterCategory {
        Spell, Trap;
    }

    companion object {

        fun getMonsterFilterBackgrounds(category: CardFilterCategory): HashMap<String, Int> {
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
                    Pair("Union Effect Monster", R.color.effectMonster)
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
                    Pair("Winged Beast", R.color.beast)
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
            }
        }

        fun getNonMonsterFilterBackgrounds(category: NonMonsterCardFilterCategory)
                : HashMap<String, Int> {
            return when (category) {
                NonMonsterCardFilterCategory.Spell -> hashMapOf(
                    Pair("Normal", R.color.normalSpell),
                    Pair("Field", R.color.field),
                    Pair("Equip", R.color.equip),
                    Pair("Continuous", R.color.continuousSpell),
                    Pair("Quick-Play", R.color.quickPlay),
                    Pair("Ritual", R.color.ritual)
                )
                NonMonsterCardFilterCategory.Trap -> hashMapOf(
                    Pair("Normal", R.color.normalTrap),
                    Pair("Continuous", R.color.continuous),
                    Pair("Counter", R.color.counter)
                )
            }
        }
    }

    private val _filterList = MutableLiveData<List<CardFilter>>(listOf())
    val filterList: LiveData<List<CardFilter>>
        get() = _filterList

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
        CardFilter("Union Effect Monster")
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

    init {
        if (CardFilterType.valueOf(type) == CardFilterType.Monster)
            _filterList.value = getMonsterFilter(CardFilterCategory.valueOf(category))
        else _filterList.value = getNonMonsterFilter(
            NonMonsterCardFilterCategory.valueOf(category)
        )
    }

    fun getBackgroundsForFilters(): HashMap<String, Int> {
        return if (CardFilterType.valueOf(type) == CardFilterType.Monster)
            getMonsterFilterBackgrounds(CardFilterCategory.valueOf(category))
        else getNonMonsterFilterBackgrounds(NonMonsterCardFilterCategory.valueOf(category))
    }

    private fun getMonsterFilter(category: CardFilterCategory): List<CardFilter> {
        return when (category) {
            CardFilterCategory.Type -> types

            CardFilterCategory.Race -> races

            CardFilterCategory.Attribute -> attributes
        }
    }

    private fun getNonMonsterFilter(filterCategory: NonMonsterCardFilterCategory): List<CardFilter> {
        return when (filterCategory) {

            NonMonsterCardFilterCategory.Spell -> spells

            NonMonsterCardFilterCategory.Trap -> traps
        }
    }

}

data class CardFilter(val name: String, var isSelected: Boolean = false) {
    companion object {
        var previousSelectedFilter: CardFilter? = null
    }
}

@Suppress("UNCHECKED_CAST")
class FilterSelectionViewModelFactory(
    private val category: String, private val type: String
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FilterSelectionViewModel::class.java))
            return FilterSelectionViewModel(category, type) as T
        else throw IllegalArgumentException("Unknown ViewModel class")
    }
}