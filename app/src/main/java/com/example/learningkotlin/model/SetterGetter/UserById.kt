package com.example.learningkotlin.model.SetterGetter

import com.google.gson.annotations.SerializedName

class UserById(status: Boolean, message: String, data: Data) {
    @SerializedName("status")
    var status:Boolean? = status
    @SerializedName("message")
    var message:String? = message
    @SerializedName("data")
    var data:Data? = data

}

data class Data(
    var user_id: Int,
    var username: String,
    var email: String
)