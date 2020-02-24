package com.example.learningkotlin.model.SetterGetter

import com.google.gson.annotations.SerializedName

class Register(status : Boolean, message: String, data:REG) {

    @SerializedName("status")
    var status:Boolean? = status
    @SerializedName("message")
    var message:String? = message
    @SerializedName("data")
    var data:REG? = data
}

data class REG(
    var username : String,
    var email: String,
    var password: String
)

