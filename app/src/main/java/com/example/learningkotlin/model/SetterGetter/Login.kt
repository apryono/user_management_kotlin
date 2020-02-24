package com.example.learningkotlin.model.SetterGetter

import com.google.gson.annotations.SerializedName

class Login(status: Boolean , message: String , data: JWT) {

    @SerializedName("status")
    var status:Boolean? = status
    @SerializedName("message")
    var message:String? = message
    @SerializedName("data")
    var data: JWT? = data
}

data class JWT(var token: String)