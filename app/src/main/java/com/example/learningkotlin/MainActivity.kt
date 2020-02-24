package com.example.learningkotlin

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.learningkotlin.model.SetterGetter.ListUser
import com.example.learningkotlin.model.SetterGetter.ListUsers
import com.example.learningkotlin.network.RetrofitBuilder
import com.example.learningkotlin.network.ServiceApi
import com.example.learningkotlin.storage.sharedPreferences
import com.example.learningkotlin.utils.JWTUtils
import com.example.learningkotlin.utils.adapter.ListUserAdapter
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private var data =  ArrayList<ListUser>()
    private lateinit var getSharedPreferences: sharedPreferences
    private lateinit var recyclerView: RecyclerView
    private lateinit var adminName : TextView
    private lateinit var logout: Button
    lateinit var listView: RecyclerView
    lateinit var getToken: String
    lateinit var swipe : SwipeRefreshLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getSharedPreferences = sharedPreferences(this)
        recyclerView = findViewById(R.id.rv_view)
        adminName = findViewById(R.id.txtNameAdm)
        logout = findViewById(R.id.btnLogout)
        listView = findViewById(R.id.rv_view)
        swipe = findViewById(R.id.swipe_refresh_layout)

        swipe.setProgressBackgroundColorSchemeColor(ContextCompat.getColor(this,R.color.colorPrimary))
        swipe.setColorSchemeColors(Color.WHITE)
        swipe.setOnRefreshListener {
            fetching()
            swipe.isRefreshing=true
        }


        logout.setOnClickListener {
            getSharedPreferences.remove(getSharedPreferences._token)
            Toast.makeText(this, "Logout Success", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()

        }
        getToken = getSharedPreferences.getString(getSharedPreferences._token).toString()

        if (getToken == "empty") {
            startActivity(Intent(applicationContext, LoginActivity::class.java))
            finish()
        } else {
            var getUser = JWTUtils.decoded(getSharedPreferences.getString(getSharedPreferences._token).toString())
            adminName.text = getUser.getString("username")
        }


        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)


        fetching()
    }

    private fun fetching() {
        this?.let {
            val retrofit = RetrofitBuilder.Login()
            val callApi = retrofit.create(ServiceApi::class.java)
            getToken = getSharedPreferences.getString(getSharedPreferences._token).toString()

            callApi.getUser("Bearer " + getToken).enqueue(object :Callback<ListUsers> {
                override fun onFailure(call: Call<ListUsers>, t: Throwable) {
                    Log.e("FETCH FAIL", t.message.toString())
                    Toast.makeText(it,"Error Network timeout", Toast.LENGTH_LONG).show()
                }

                override fun onResponse(call: Call<ListUsers>, response: Response<ListUsers>) {
                    Log.i("FETCH SUCCESS", response.body().toString())
                    swipe.isRefreshing = false
                    if (response.isSuccessful) {
                        if (getToken == "empty") {
                            startActivity(Intent(applicationContext, LoginActivity::class.java))
                            finish()
                        } else {
                            val getData = response.body()?.data
                            rv_view.adapter = ListUserAdapter(applicationContext, getData!!)
                        }
                    } else {
                        getSharedPreferences.remove(getSharedPreferences._token)
                        Toast.makeText(it,"Token inValidate!", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(it, LoginActivity::class.java))
                        finish()
                    }
                }
            })
        }


    }
}
