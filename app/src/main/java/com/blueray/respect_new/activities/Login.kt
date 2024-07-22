package com.blueray.respect.activities

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.blueray.raihan.viewModel.AppViewModel
import com.blueray.respect_new.R
import com.blueray.respect_new.activities.MainActivity
import com.blueray.respect_new.databinding.ActivityLoginBinding
import com.blueray.respect_new.helpers.HelperUtils
import com.blueray.respect_new.helpers.HelperUtils.SHARED_PREF
import com.blueray.respect_new.model.LoginData
import com.blueray.respect_new.model.NetworkResults

class Login : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val viewModel by viewModels<AppViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        binding.loginText.setTextColor(resources.getColor(R.color.text_color))
        binding.loginButton.setOnClickListener {
            validateFields()

        }
        //observe data
        getLogin()
    }

    //validation of fields
    private fun validateFields() {
        if (binding.userNameEt.text.toString().isEmpty()) {
            binding.userNameEt.error = "الرجاء تعبئة الحقل"
        } else if (binding.password.text.toString().isEmpty()) {
            binding.password.error = "الرجاء تعبئة الحقل"
        } else {
            viewModel.retrieveLogin(
                binding.userNameEt.text.toString(),
                binding.password.text.toString()
            )
        }
    }

    private fun getLogin() {
        viewModel.getLogin().observe(this) { result ->
            when (result) {
                is NetworkResults.Success -> {
                    if (result.data.msg.status == 200) {
                        saveUserData(result.data.msg.data)
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, result.data.msg.message, Toast.LENGTH_SHORT).show()
                    }
                }

                is NetworkResults.Error -> {
                    Toast.makeText(
                        this,
                        result.exception.localizedMessage.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is NetworkResults.PartialSuccess -> {

                }
            }
        }
    }

    fun saveUserData(user: LoginData) {
        val sharedPreferences = getSharedPreferences(SHARED_PREF, MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("uid", user.id.toString())
        editor.putString("user_name", user.user_name)
        editor.putString("user_image", HelperUtils.BASE_URL + user.userImage)
        editor.putString("role", user.role)
        editor.apply()
    }
}