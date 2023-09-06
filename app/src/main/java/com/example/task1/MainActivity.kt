package com.example.task1

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.task1.databinding.ActivityMainBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: SearchViewModel
    private lateinit var binding: ActivityMainBinding
    private val mainScope = MainScope()
    private var searchJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize the ViewModel
        viewModel = ViewModelProvider(this).get(SearchViewModel::class.java)

        // Set up AutoCompleteTextView adapter
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line)
        binding.autoCompleteTextView.setAdapter(adapter)

        // Listen for text changes in AutoCompleteTextView
        binding.autoCompleteTextView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Implement beforeTextChanged logic if needed
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString()
                if (query.length >= 2) {
                    // Cancel any previous search job
                    searchJob?.cancel()
                    // Delay the search to avoid frequent requests while typing
                    searchJob = mainScope.launch {
                        delay(200) // Adjust the delay as needed
                        val suggestions = viewModel.search(query) // Call within a coroutine
                        adapter.clear()
                        adapter.addAll(suggestions)
                        adapter.notifyDataSetChanged()
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // Implement afterTextChanged logic if needed
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        // Cancel any ongoing search job when the activity is destroyed
        searchJob?.cancel()
    }
}
