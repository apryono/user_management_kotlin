package com.example.learningkotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import com.example.learningkotlin.model.SetterGetter.Login
import com.example.learningkotlin.network.RetrofitBuilder
import com.example.learningkotlin.network.ServiceApi
import com.example.learningkotlin.storage.sharedPreferences
import com.example.learningkotlin.utils.Email
import com.google.android.material.snackbar.Snackbar
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    lateinit var sharedPreference: sharedPreferences
    lateinit var loading: LinearLayout

    lateinit var isEmail: Email
    lateinit var email : EditText
    lateinit var password : EditText
    lateinit var btnLogin : Button
    lateinit var register : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_layout)

        email = findViewById(R.id.et_email)
        password = findViewById(R.id.et_password)
        btnLogin = findViewById(R.id.btnSingIn)
        register = findViewById(R.id.register)

        loading = findViewById(R.id.loading)
        isEmail = Email()
        sharedPreference = sharedPreferences(this)

        register.setOnClickListener {
            startActivity(Intent(Intent(this,
                RegisterActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            ))
            finish()
        }

        btnLogin.setOnClickListener {
            if (email.text.length <= 4 ) {
                Snackbar.make(email, "Email must be longest 5 character", Snackbar.LENGTH_SHORT).show()
                if (email.text.isEmpty()) {

                }
            } else if(!isEmail.isValidEmail(email.text.toString())){
                Snackbar.make(
                    email,
                    "Email invalid!",
                    Snackbar.LENGTH_SHORT
                ).show()
            }else if (password.text.length <= 4) {
                Snackbar.make(
                    password,
                    "Password must be longest 5 character",
                    Snackbar.LENGTH_SHORT
                ).show()
            } else {
                Login()
            }


        }


    }

    private fun Login() {
        val data = JSONObject()
        data.put("email", email.text)
        data.put("password", password.text)

        showLoading()

        val requestBody: RequestBody = RequestBody.create(MediaType.parse("application/json"), data.toString())
        val retrofit = RetrofitBuilder.Login()
        val callApi = retrofit.create(ServiceApi::class.java)


        callApi.login(requestBody).enqueue(object : Callback<Login> {
            override fun onFailure(call: Call<Login>, t: Throwable) {
                Log.d("FAILED FETCH", t.toString())
                dismissLoading()
                Snackbar.make(email,"Error when connecting to server", Snackbar.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<Login>, response: Response<Login>) {
                Log.d("FETCH SUCCESS", response.body().toString())
                if (response.isSuccessful) {
                    dismissLoading()
                    if (response.body()?.status == true) {
                        sharedPreference.saveString(sharedPreference._token, response.body()?.data?.token.toString())
                        Snackbar.make(email, "success", Snackbar.LENGTH_SHORT).show()
                        startActivity(Intent(applicationContext, MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                        finish()
                    } else {
                        dismissLoading()
                        Snackbar.make(email, response.body()?.message.toString(), Snackbar.LENGTH_SHORT).show()
                    }
                } else {
                    dismissLoading()
                    Snackbar.make(email, "Email or Password is wrong", Snackbar.LENGTH_SHORT).show()
                }
            }
        })
    }


    fun showLoading() {
        btnLogin.visibility = View.GONE
        loading.visibility = View.VISIBLE
    }

    fun dismissLoading(){
        btnLogin.visibility = View.VISIBLE
        loading.visibility = View.GONE
    }
}
