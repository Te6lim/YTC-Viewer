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

                CardFilter.Race -> hashMapOf(
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

                else -> hashMapOf()
            }
        }
    }

    private val _searchBarClicked = MutableLiveData<Boolean>()
    val searchBarClicked: LiveData<Boolean>
        get() = _searchBarClicked

    fun setSearchBarClicked(value: Boolean) {
        _searchBarClicked.value = value
    }

    private val _checkedChipName = MutableLiveData<String?>()
    val checkedChipName: LiveData<String?>
        get() = _checkedChipName

    fun setChipChecked(value: String?) {
        _checkedChipName.value = value
    }

    private val _checkedChipId = MutableLiveData<Int?>()
    val checkedChipId: LiveData<Int?>
        get() = _checkedChipId

    fun storeCheckedChipId(value: Int?) {
        _checkedChipId.value = value
    }
}