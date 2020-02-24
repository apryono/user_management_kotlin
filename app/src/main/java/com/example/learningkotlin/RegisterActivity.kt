package com.example.learningkotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.example.learningkotlin.model.SetterGetter.Register
import com.example.learningkotlin.network.RetrofitBuilder
import com.example.learningkotlin.network.ServiceApi
import com.example.learningkotlin.utils.Email
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.register_layout.*
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    lateinit var backToLogin : Button
    lateinit var isMail : Email
    lateinit var username : EditText
    lateinit var email : EditText
    lateinit var password : EditText
    lateinit var verifyPassword : EditText
    lateinit var register : Button
    lateinit var progressBar: LinearLayout



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_layout)
        isMail = Email()

        username = findViewById(R.id.et_username)
        email = findViewById(R.id.et_email)
        password = findViewById(R.id.et_password)
        verifyPassword = findViewById(R.id.et_verify)

        progressBar = findViewById(R.id.loading)
        register = findViewById(R.id.btnSingUp)

        register.setOnClickListener {
            if (username.text.length <= 4) {
                Snackbar.make(username, "Username must longest 5 character", Snackbar.LENGTH_SHORT)
                    .show()
                username.error
            } else if (email.text.length <= 4) {
                Snackbar.make(email, "Email must longest 5 character", Snackbar.LENGTH_SHORT)
                    .show()
            } else if(!isMail.isValidEmail(email.text.toString())){
                Snackbar.make(email, "Email invalid!", Snackbar.LENGTH_SHORT)
                    .show()
            }else if (password.text.length <= 4) {
                Snackbar.make(password, "Password must longest 5 character", Snackbar.LENGTH_SHORT)
                    .show()
            } else if (verifyPassword.text.length <= 4) {
                Snackbar.make(verifyPassword, "Username must longest 5 character", Snackbar.LENGTH_SHORT)
                    .show()
            } else if (password.text.toString() != verifyPassword.text.toString()) {
                password.setError("try again")
                verifyPassword.setError("try again")
                Snackbar.make(password, "Password do not match", Snackbar.LENGTH_SHORT).show()
            } else {
                val data = JSONObject()
                data.put("username", username.text.toString())
                data.put("email", email.text.toString())
                data.put("password", password.text.toString())

                showLoading()

                val retrofit = RetrofitBuilder.Login()
                val callApi = retrofit.create(ServiceApi::class.java)
                val requestBody: RequestBody = RequestBody.create(MediaType.parse("application/json"),data.toString())

                callApi.register(requestBody).enqueue(object : Callback<Register> {
                    override fun onFailure(call: Call<Register>, t: Throwable) {
                        Log.e("FETCH FAIL", t.message.toString())
                        dismissLoading()
                        Snackbar.make(email, "Error when try connecting to server", Snackbar.LENGTH_LONG)
                            .show()
                    }

                    override fun onResponse(call: Call<Register>, response: Response<Register>) {
                        if (response.isSuccessful) {
                            dismissLoading()
                            if (response.body()?.status == true) {
                                Toast.makeText(
                                    applicationContext,
                                    "Register successfully..",
                                    Toast.LENGTH_LONG
                                ).show()

                                var newIntent = Intent(applicationContext, LoginActivity::class.java)
                                startActivity(newIntent)
                                finish()
                            } else {
                                dismissLoading()
                                Snackbar.make(email, response.body()?.message.toString(), Snackbar.LENGTH_SHORT).show()
                            }
                        } else {
                            dismissLoading()
                            Snackbar.make(email, response.body()?.message.toString(), Snackbar.LENGTH_SHORT).show()
                        }
                    }
                })
            }
        }

        backToLogin = findViewById(R.id.btLogin)
        backToLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
            finish()
        }
    }

    fun showLoading() {
        btnSingUp.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
    }

    fun dismissLoading() {
        btnSingUp.visibility = View.VISIBLE
        progressBar.visibility = View.GONE
    }

}
