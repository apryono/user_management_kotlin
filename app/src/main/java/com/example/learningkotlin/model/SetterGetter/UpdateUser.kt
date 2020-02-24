package com.example.learningkotlin.model.SetterGetter

import com.google.gson.annotations.SerializedName

class Update(status : Boolean, message: String, data:UPD) {

    @SerializedName("status")
    var status:Boolean? = status
    @SerializedName("message")
    var message:String? = message
    @SerializedName("data")
    var data:UPD? = data
}

data class UPD(
    var username : String,
    var email: String
)