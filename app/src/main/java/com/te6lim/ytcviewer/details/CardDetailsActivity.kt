package com.te6lim.ytcviewer.details

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.te6lim.ytcviewer.R
import com.te6lim.ytcviewer.database.Card
import com.te6lim.ytcviewer.databinding.ActivityDetailsCardBinding
import com.te6lim.ytcviewer.filters.FilterSelectionViewModel

class CardDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsCardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_details_card)
        setSupportActionBar(binding.toolbar as Toolbar)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            elevation = this@CardDetailsActivity.resources.getDimension(R.dimen.small_spacing)
        }

        val card = intent.getParcelableExtra<Card>("card")!!

        val viewModel = ViewModelProvider(
            this, CardDetailsViewModelFactory(card)
        )[CardDetailsViewModel::class.java]

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        with(binding) {
            cardSetList.adapter = CardSetListAdapter().apply { submitList(card.cardSets) }
            cardTypeText.text = viewModel.card.type
            atkText.text = viewModel.card.atk.toString()
            defText.text = viewModel.card.def.toString()
            levelText.text = getString(
                R.string.level, viewModel.card.level.toString()
            )
            raceText.text = getString(R.string.race, viewModel.card.race)
            attributeArchetypeText.text =
                getString(R.string.race, viewModel.card.attribute)
            if (viewModel.card.isNonMonsterCard())
                attributeArchetypeTitleText.text = getString(R.string.archetype_title)
            monsterProperties.visibility =
                if (viewModel.card.isNonMonsterCard()) View.GONE else View.VISIBLE

            level.setImageResource(
                FilterSelectionViewModel.getLevelOrRankIcon(viewModel.card.type!!)
            )
            raceIcon.setImageResource(
                FilterSelectionViewModel.getRaceIconResource()[viewModel.card.race] ?: R.drawable.poker
            )
            viewModel.card.attribute?.let {
                attributeImage.setImageResource(
                    FilterSelectionViewModel.getAttributeIconResource()[it]!!
                )
            } ?: run { attributeImage.visibility = View.GONE }
            descriptionText.text = viewModel.card.desc
        }

        supportActionBar?.title = card.name
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