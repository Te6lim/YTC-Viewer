package com.te6lim.ytcviewer.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.te6lim.ytcviewer.R

class HomeViewModel(private val app: Application) : AndroidViewModel(app) {

    enum class CardFilter {
        Type, Race, Attribute;
    }

    enum class NonMonsterCardFilter {
        Spell, Trap;
    }

    companion object {
        fun getFilerSelections(filter: CardFilter): List<String> {
            return when (filter) {
                CardFilter.Type -> listOf(
                    "Effect Monster",
                    "Flip Effect Monster",
                    "Flip Tuner Effect Monster",
                    "Gemini Monster",
                    "Normal Monster",
                    "Normal Tuner Monster",
                    "Pendulum Effect Monster",
                    "Pendulum Flip Effect Monster",
                    "Pendulum Normal Monster",
                    "Pendulum Tuner Effect Monster",
                    "Ritual Effect Monster",
                    "Ritual Monster",
                    "Skill Card",
                    "Spirit Monster",
                    "Toon Monster",
                    "Tuner Monster",
                    "Union Effect Monster"
                )

                CardFilter.Race -> listOf(
                    "Aqua",
                    "Beast",
                    "Beast-Warrior",
                    "Creator-God",
                    "Cyberse",
                    "Dinosaur",
                    "Divine-Beast",
                    "Dragon",
                    "Fairy",
                    "Fiend",
                    "Fish",
                    "Insect",
                    "Machine",
                    "Plant",
                    "Psychic",
                    "Pyro",
                    "Reptile",
                    "Rock",
                    "Sea Serpent",
                    "Spellcaster",
                    "Thunder",
                    "Warrior",
                    "Winged Beast"
                )

                CardFilter.Attribute -> listOf(
                    "dark", "earth", "fire", "light", "water", "wind", "divine"
                )
            }
        }

        fun getNonMonsterFilter(filter: NonMonsterCardFilter): List<String> {
            return when (filter) {

                NonMonsterCardFilter.Spell -> listOf(
                    "Normal",
                    "Field",
                    "Equip",
                    "Continuous",
                    "Quick-Play",
                    "Ritual"
                )

                NonMonsterCardFilter.Trap -> listOf(
                    "Normal",
                    "Continuous",
                    "Counter"
                )
            }
        }


        fun getFilterBackgrounds(filter: CardFilter): HashMap<String, Int> {
            return when (filter) {
                CardFilter.Type -> hashMapOf(
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
                else -> HashMap()
            }
        }
    }

    private val _searchBarClicked = MutableLiveData<Boolean>()
    val searchBarClicked: LiveData<Boolean>
        get() = _searchBarClicked

    fun setSearchBarClicked(value: Boolean) {
        _searchBarClicked.value = value
    }

    //Test
    private val _chipChecked = MutableLiveData<String?>()
    val chipChecked: LiveData<String?>
        get() = _chipChecked

    fun setChipChecked(value: String?) {
        _chipChecked.value = value
    }
}