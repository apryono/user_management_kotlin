package com.example.learningkotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.example.learningkotlin.model.SetterGetter.Update
import com.example.learningkotlin.network.RetrofitBuilder
import com.example.learningkotlin.network.ServiceApi
import com.example.learningkotlin.storage.sharedPreferences
import com.example.learningkotlin.utils.Email
import com.example.learningkotlin.utils.JWTUtils
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.update_layout.*
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpdateActivity : AppCompatActivity() {

    lateinit var getSharedPreferences: sharedPreferences
    lateinit var user_name : EditText
    lateinit var emails : EditText
    lateinit var update: Button
    lateinit var isMail : Email
    lateinit var progressBar: LinearLayout
    lateinit var back: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.update_layout)

        user_name = findViewById(R.id.updateUser)
        emails = findViewById(R.id.updateEmail)
        update = findViewById(R.id.btnUpdate)
        back = findViewById(R.id.btnBack)
        getSharedPreferences = sharedPreferences(this)



        isMail = Email()
        progressBar = findViewById(R.id.loadingPro)

        back.setOnClickListener {
            onBackPressed()
        }




        update.setOnClickListener {
            if (user_name.text.isEmpty()) {
                user_name.error = "Username is required"
                user_name.requestFocus()
                return@setOnClickListener
            } else if (!isMail.isValidEmail(emails.text.toString())) {
                emails.error = "Email is required"
                emails.requestFocus()
                return@setOnClickListener
            } else {
                updateData()
            }

        }



    }

    private fun updateData() {
        val data = JSONObject()
        data.put("username", user_name.text.toString())
        data.put("email", emails.text.toString())

        showLoading()

        val retrofit = RetrofitBuilder.Login()
        val callApi = retrofit.create(ServiceApi::class.java)
        val requestBody: RequestBody = RequestBody.create(MediaType.parse("application/json"), data.toString())
        val getToken = intent.getStringExtra("token")

        callApi.updateUser("Bearer " + getToken, requestBody, intent.getIntExtra("user_id",0))
            .enqueue(object : Callback<Update> {
                override fun onFailure(call: Call<Update>, t: Throwable) {
                    Log.e("FETCH FAIL", t.message.toString())
                    dismissLoading()
                    Snackbar.make(
                        emails,
                        "Error when try connecting to server",
                        Snackbar.LENGTH_LONG
                    )
                        .show()
                }

                override fun onResponse(call: Call<Update>, response: Response<Update>) {
                    if (response.isSuccessful) {
                        dismissLoading()
                        if (response.body()?.status == true) {
                            Toast.makeText(
                                applicationContext,
                                "Register successfully..",
                                Toast.LENGTH_LONG
                            ).show()
                            onBackPressed()
                        } else {
                            dismissLoading()
                            Snackbar.make(
                                emails,
                                response.body()?.message.toString(),
                                Snackbar.LENGTH_SHORT
                            ).show()
                        }

                    } else {
                        dismissLoading()
                        Snackbar.make(
                            emails,
                            response.body()?.message.toString(),
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                }
            })
    }

    fun showLoading() {
        btnUpdate.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
    }

    fun dismissLoading() {
        btnUpdate.visibility = View.VISIBLE
        progressBar.visibility = View.GONE
    }

    override fun onStart() {
        super.onStart()

        user_name.setText(intent.getStringExtra("username")?.toString())
        emails.setText(intent.getStringExtra("email")?.toString())
    }

}
