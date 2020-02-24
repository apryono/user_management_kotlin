package com.example.learningkotlin

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.learningkotlin.model.SetterGetter.DefaultResponse
import com.example.learningkotlin.model.SetterGetter.UserById
import com.example.learningkotlin.network.RetrofitBuilder
import com.example.learningkotlin.network.ServiceApi
import com.example.learningkotlin.storage.sharedPreferences
import com.example.learningkotlin.utils.JWTUtils
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailActivity : AppCompatActivity() {

    lateinit var getSharedPreferences: sharedPreferences
    lateinit var username: TextView
    lateinit var emails: TextView
    private lateinit var data: JSONObject
    lateinit var bt_edit : Button
    lateinit var bt_delete : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detail_layout)

        username = findViewById(R.id.nameUser)
        emails = findViewById(R.id.emailUser)
        bt_edit = findViewById(R.id.btnEdit)
        bt_delete = findViewById(R.id.btnDelete)


        bt_delete.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Confirmation")
                .setMessage("Do you want to delete ?")
                .setCancelable(false)
                .setPositiveButton("Delete",DialogInterface.OnClickListener { dialog, _ ->
                    delete()
                    dialog.dismiss()
                })
                .setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, _ ->
                    dialog.dismiss()
                })
                .show()

        }

        getSharedPreferences = sharedPreferences(this)

        data = JWTUtils.decoded(getSharedPreferences.getString(getSharedPreferences._token).toString())

        bt_edit.setOnClickListener {
            startActivity(Intent(this, UpdateActivity::class.java).apply {
                putExtra("user_id", intent.getIntExtra("user_id",0))
                putExtra("username", username.text)
                putExtra("email", emails.text)
                Log.d("IDD", intent.getIntExtra("user_id",0).toString())
                putExtra("token", getSharedPreferences.getString(getSharedPreferences._token).toString())
                Log.d("TOKEEN", getSharedPreferences.getString(getSharedPreferences._token).toString())
            })
        }

        val retrofit =  RetrofitBuilder.Login()
        val callApi = retrofit.create(ServiceApi::class.java)
        val token = "Bearer " + getSharedPreferences.getString(getSharedPreferences._token).toString()

        callApi.getUserById(token, this.intent.getIntExtra("user_id",0)).enqueue(object : Callback<UserById> {
            override fun onFailure(call: Call<UserById>, t: Throwable) {
                Log.e("FETCH FAIL", t.message.toString())
                Toast.makeText(applicationContext, "Failed Connect to server!", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<UserById>, response: Response<UserById>) {
                if (response.isSuccessful) {
                   username.text = response.body()?.data?.username
                    emails.text = response.body()?.data?.email
                } else {
                    getSharedPreferences.remove(getSharedPreferences._token)
                    Toast.makeText(applicationContext,"Token inValidate!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(applicationContext, LoginActivity::class.java))
                    finish()
                }
            }
        })


    }

    private fun delete() {
        data = JWTUtils.decoded(getSharedPreferences.getString(getSharedPreferences._token).toString())
        val retrofit = RetrofitBuilder.Login()
        val callApi = retrofit.create(ServiceApi::class.java)
        val token =
            "Bearer " + getSharedPreferences.getString(getSharedPreferences._token).toString()

        callApi.delete(token, this.intent.getIntExtra("user_id", 0))
            .enqueue(object : Callback<DefaultResponse> {
                override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                    Log.e("FETCH FAIL", t.message.toString())
                    Toast.makeText(applicationContext, "Error Network timeout", Toast.LENGTH_LONG)
                        .show()
                }

                override fun onResponse(
                    call: Call<DefaultResponse>,
                    response: Response<DefaultResponse>
                ) {
                    if (response.isSuccessful) {
                        Toast.makeText(applicationContext, "Delete User Success", Toast.LENGTH_LONG)
                        onBackPressed()
                    } else {
                        getSharedPreferences.remove(getSharedPreferences._token)
                        Toast.makeText(applicationContext, "Token inValidate!", Toast.LENGTH_SHORT)
                            .show()
                        startActivity(Intent(applicationContext, LoginActivity::class.java))
                        finish()
                    }
                }
            })
    }

}
