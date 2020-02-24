package com.example.learningkotlin.model.SetterGetter

import com.google.gson.annotations.SerializedName


class DefaultResponse(status: Boolean, message: String ) {

    @SerializedName("status")
    var status:Boolean? = status
    @SerializedName("message")
    var message:String? = message
}