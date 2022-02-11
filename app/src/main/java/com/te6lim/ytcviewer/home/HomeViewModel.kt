package com.te6lim.ytcviewer.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

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
    }

    private val _searchBarClicked = MutableLiveData<Boolean>()
    val searchBarClicked: LiveData<Boolean>
        get() = _searchBarClicked

    fun setSearchBarClicked(value: Boolean) {
        _searchBarClicked.value = value
    }
}