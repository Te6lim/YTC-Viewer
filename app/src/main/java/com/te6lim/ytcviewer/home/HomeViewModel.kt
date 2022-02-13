package com.te6lim.ytcviewer.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.te6lim.ytcviewer.R

class HomeViewModel(app: Application) : AndroidViewModel(app) {

    enum class CardFilterCategory {
        Type, Race, Attribute;
    }

    enum class NonMonsterCardFilterCategory {
        Spell, Trap;
    }

    enum class CardFilterType {
        Monster, NonMonster
    }

    companion object {
        fun getMonsterFilter(category: CardFilterCategory): List<CardFilter> {
            return when (category) {
                CardFilterCategory.Type -> listOf(
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

                CardFilterCategory.Race -> listOf(
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

                CardFilterCategory.Attribute -> listOf(
                    CardFilter("dark"),
                    CardFilter("earth"),
                    CardFilter("fire"),
                    CardFilter("light"),
                    CardFilter("water"),
                    CardFilter("wind"),
                    CardFilter("divine")
                )
            }
        }

        fun getNonMonsterFilter(filterCategory: NonMonsterCardFilterCategory): List<String> {
            return when (filterCategory) {

                NonMonsterCardFilterCategory.Spell -> listOf(
                    "Normal",
                    "Field",
                    "Equip",
                    "Continuous",
                    "Quick-Play",
                    "Ritual"
                )

                NonMonsterCardFilterCategory.Trap -> listOf(
                    "Normal",
                    "Continuous",
                    "Counter"
                )
            }
        }


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

        fun getNonMonsterFilterBackgrounds(category: NonMonsterCardFilterCategory): HashMap<String, Int> {
            return hashMapOf()
        }
    }

    private val _checkedCategories = MutableLiveData<Map<String, CardFilterCategory>>(
        mutableMapOf()
    )
    val checkedCategories: LiveData<Map<String, CardFilterCategory>>
        get() = _checkedCategories

    private val _searchBarClicked = MutableLiveData<Boolean>()
    val searchBarClicked: LiveData<Boolean>
        get() = _searchBarClicked

    fun setSearchBarClicked(value: Boolean) {
        _searchBarClicked.value = value
    }

    private val _checkedChipName = MutableLiveData<Pair<String, String>?>()
    val checkedChipName: LiveData<Pair<String, String>?>
        get() = _checkedChipName

    fun setChipChecked(value: Pair<String, String>?) {
        _checkedChipName.value = value
    }

    fun addCategoryToChecked(category: String) {
        val map = _checkedCategories.value!!.toMutableMap()
        map[category] = CardFilterCategory.valueOf(category)
        _checkedCategories.value = map
    }

    fun removeCategoryFromChecked(category: String) {
        val map = _checkedCategories.value!!.toMutableMap()
        map.remove(category)
        _checkedCategories.value = map
    }
}

data class CardFilter(val name: String, val isSelected: Boolean = false)