package com.te6lim.ytcviewer.details

import android.os.Bundle
import android.view.MenuItem
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

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val card = intent.getParcelableExtra<Card>("card")!!

        val viewModel = ViewModelProvider(
            this, CardDetailsViewModelFactory(card)
        )[CardDetailsViewModel::class.java]

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

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