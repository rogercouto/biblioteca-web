package br.com.biblioteca.presentation.features.auth

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import br.com.biblioteca.databinding.ActivityAuthBinding
import br.com.biblioteca.presentation.ext.visibleOrGone
import br.com.biblioteca.presentation.features.main.MainActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class AuthActivity : AppCompatActivity() {

    private val viewModel : AuthViewModel by viewModel()

    private lateinit var binding : ActivityAuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpViewModel()
        setUpActions()
        binding.btnEnter.isEnabled = false
    }

    private fun setUpViewModel(){
        viewModel.auth.observe(this) { auth ->
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_TASK_ON_HOME
            startActivity(intent)
        }
        viewModel.loading.observe( this ){ isLoading ->
            binding.viewLoading.root.visibleOrGone(isLoading)
            binding.btnEnter.isEnabled = !isLoading
        }
        viewModel.error.observe(this){ error ->
            if (error != null ){
                if (error.code == 400){
                    binding.tvError.visibleOrGone(true)
                }else{
                    binding.etEmail.visibleOrGone(false)
                    binding.etPassword.visibleOrGone(false)
                    binding.btnEnter.visibleOrGone(false)
                    binding.viewError.root.visibleOrGone(true)
                    binding.tvError.visibleOrGone(false)
                }
            }else{
                binding.tvError.visibleOrGone(false)
            }
        }
    }

    private fun setUpActions() {
        binding.etEmail.doAfterTextChanged {
            checkFields()
        }
        binding.etPassword.doAfterTextChanged {
            checkFields()
        }
        binding.btnEnter.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            viewModel.signIn(email, password)
        }
    }

    private fun checkFields(){
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()
        binding.btnEnter.isEnabled = !email.isEmpty() && !password.isEmpty()
    }


}