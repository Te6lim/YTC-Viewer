package com.te6lim.ytcviewer.cardDetails

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.te6lim.ytcviewer.R
import com.te6lim.ytcviewer.YTCApplication
import com.te6lim.ytcviewer.cardFilters.FilterSelectionViewModel
import com.te6lim.ytcviewer.databinding.ActivityDetailsCardBinding
import com.te6lim.ytcviewer.resources.cardDetailsActivityIntentCardKey

class CardDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsCardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_details_card)
        setSupportActionBar(binding.toolbar as Toolbar)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            elevation = if (!(application as YTCApplication).toDarkMode) {
                this@CardDetailsActivity.resources.getDimension(R.dimen.small_spacing)
            } else this@CardDetailsActivity.resources.getDimension(R.dimen.no_spacing)
        }

        val cardId = intent.getLongExtra(cardDetailsActivityIntentCardKey, -1)

        val repository = (application as YTCApplication).repository

        val viewModel = ViewModelProvider(
            this, CardDetailsViewModelFactory(repository, cardId)
        )[CardDetailsViewModel::class.java]

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.databaseCard.observe(this) {
            it?.let { card ->

                supportActionBar?.title = card.name

                with(binding) {
                    cardSetList.adapter = CardSetListAdapter().apply { submitList(card.cardSets) }
                    cardTypeText.text = card.type
                    atkText.text = card.atk.toString()
                    defText.text = card.def.toString()
                    levelText.text = getString(
                        R.string.level, card.level.toString()
                    )
                    raceText.text = getString(R.string.race, card.race)
                    attributeArchetypeText.text =
                        getString(R.string.race, card.attribute)
                    if (card.isNonMonsterCard())
                        attributeArchetypeTitleText.text = getString(R.string.archetype_title)
                    monsterProperties.visibility =
                        if (card.isNonMonsterCard()) View.GONE else View.VISIBLE

                    level.setImageResource(
                        FilterSelectionViewModel.getLevelOrRankIcon(card.type!!)
                    )

                    card.race?.let {
                        raceIcon.setImageResource(FilterSelectionViewModel.getRaceIcon(it))
                    }

                    card.attribute?.let {
                        attributeImage.setImageResource(
                            FilterSelectionViewModel.getAttributeIcon(it)
                        )
                    } ?: run { attributeImage.visibility = View.GONE }
                    descriptionText.text = card.desc

                    favouriteIcon.setOnClickListener {
                        if (card.isFavourite) viewModel.removeFromFavorites()
                        else viewModel.addToFavorites()
                    }

                    if (card.isFavourite) favouriteIcon.setImageResource(R.drawable.ic_favorite)
                    else favouriteIcon.setImageResource(R.drawable.ic_not_favorite)
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}