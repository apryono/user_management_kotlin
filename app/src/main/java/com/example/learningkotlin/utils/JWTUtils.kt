package com.example.learningkotlin.utils

import android.util.Base64
import org.json.JSONObject
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset


class JWTUtils {
    companion object {
        fun decoded(JWTEncoded: String): JSONObject {
            var result:JSONObject

            try {
                val split = JWTEncoded.split(".").toTypedArray()
                result = JSONObject(getJson(split[1]))
            } catch (e: UnsupportedEncodingException) {
                result = JSONObject()
            }
            return result
        }

        private fun getJson(strEncoded: String): String {
            val decodedBytes: ByteArray = Base64.decode(strEncoded, Base64.URL_SAFE)
            return String(decodedBytes, Charset.forName("UTF-8"))
        }
    }
}