package com.te6lim.ytcviewer.details

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.te6lim.ytcviewer.R
import com.te6lim.ytcviewer.databinding.ActivityDetailsCardBinding
import com.te6lim.ytcviewer.domain.Card

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

        with(binding.cardProperties) {
            findViewById<TextView>(R.id.cardType_text).text = viewModel.card.type
            findViewById<TextView>(R.id.atk_text).text = viewModel.card.atk.toString()
            findViewById<TextView>(R.id.def_text).text = viewModel.card.def.toString()
            findViewById<TextView>(R.id.level_text).text = getString(
                R.string.level, viewModel.card.level.toString()
            )
            findViewById<TextView>(R.id.race_text).text = getString(R.string.race, viewModel.card.race)
            findViewById<TextView>(R.id.attribute_archetype_text).text =
                getString(R.string.race, viewModel.card.attribute)
            if (viewModel.card.isNonMonsterCard())
                findViewById<TextView>(R.id.attribute_archetype_title_text).text =
                    context.getString(R.string.archetype_title)
            findViewById<LinearLayout>(R.id.monster_properties).visibility =
                if (viewModel.card.isNonMonsterCard()) View.GONE else View.VISIBLE
        }

        with(binding.descriptionAndCardSets) {
            findViewById<TextView>(R.id.description_text).text = viewModel.card.desc
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