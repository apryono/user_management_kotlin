package com.example.learningkotlin.model.SetterGetter

import com.google.gson.annotations.SerializedName

class ListUsers(status: Boolean, message: String, data: ArrayList<ListUser>) {
    @SerializedName("status")
    var status:Boolean? = status
    @SerializedName("message")
    var message:String? = message
    @SerializedName("data")
    var data:ArrayList<ListUser>? = data

}

data class ListUser(
    var user_id: Int,
    var username: String,
    var email: String
)