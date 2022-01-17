package br.com.biblioteca.presentation.features.books

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import br.com.biblioteca.databinding.ActivityBookHostBinding
import br.com.biblioteca.presentation.features.main.TOKEN
import br.com.biblioteca.presentation.features.main.USER_ID
import org.koin.androidx.viewmodel.ext.android.viewModel

private const val VIEW_CONTENT = 0
private const val VIEW_ERROR = 1

class BookHostActivity : AppCompatActivity() {

    private val viewModel : BookViewModel by viewModel()

    private lateinit var binding : ActivityBookHostBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookHostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpViewModel()
        viewModel.token = intent.getStringExtra(TOKEN).toString()
        viewModel.userId = intent.getLongExtra(USER_ID, 0)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun setUpViewModel(){
        viewModel.haveError.observe(this){ haveError ->
            binding.vfBooks.displayedChild = if (haveError) VIEW_ERROR else VIEW_CONTENT
        }
    }

}