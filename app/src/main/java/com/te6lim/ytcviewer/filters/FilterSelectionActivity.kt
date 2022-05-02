package com.te6lim.ytcviewer.filters

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.te6lim.ytcviewer.R
import com.te6lim.ytcviewer.databinding.ActivitySelectionFilterBinding

class FilterSelectionActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySelectionFilterBinding

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_selection_filter)

        setSupportActionBar(binding.toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }
}