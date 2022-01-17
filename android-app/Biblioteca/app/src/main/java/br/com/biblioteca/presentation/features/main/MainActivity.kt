package br.com.biblioteca.presentation.features.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import br.com.biblioteca.databinding.ActivityMainBinding
import br.com.biblioteca.domain.entity.Auth
import br.com.biblioteca.presentation.ext.visibleOrGone
import br.com.biblioteca.presentation.features.auth.AuthActivity
import br.com.biblioteca.presentation.features.books.BookHostActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

const val TOKEN = "TOKEN"
const val USER_ID = "USER_ID"

class MainActivity : AppCompatActivity() {

    private val viewModel : MainViewModel by viewModel()

    private lateinit var binding : ActivityMainBinding

    private var _auth : Auth? = null
    private var _token : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpViewModel()
        setUpActions()
        super.onCreate(savedInstanceState)
    }

    private fun setUpViewModel(){
        viewModel.auth.observe(this) { auth ->
            _auth = auth
            _token = auth?.token
            if (_token != null){
                binding.btnLogin.visibleOrGone(false)
                binding.btnLogout.visibleOrGone(true)
            }
        }
        viewModel.trySignInWithCurrentUser()
    }

    private fun setUpActions(){
        binding.btnBooks.setOnClickListener {
            val intent = Intent(this, BookHostActivity::class.java)
            intent.putExtra(TOKEN, _token)
            intent.putExtra(USER_ID, _auth?.user?.id)
            startActivity(intent)
        }
        binding.btnLogin.setOnClickListener {
            val intent = Intent(this, AuthActivity::class.java)
            startActivity(intent)
        }
        binding.btnLogout.setOnClickListener {
            viewModel.logout()
            _auth = null
            _token = null
            binding.btnLogin.visibleOrGone(true)
            binding.btnLogout.visibleOrGone(false)
        }
    }

}