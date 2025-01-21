package com.anime.application.others;

import android.text.TextUtils
import android.view.View

import java.util.regex.Matcher
import java.util.regex.Pattern


class MyUtils {

    companion object {

        fun isValidPassword(password: String?): Boolean {
            if (password != null && password.length < 7) {
                return false
            }
            val pattern: Pattern
            val matcher: Matcher
            val PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$"
            pattern = Pattern.compile(PASSWORD_PATTERN)
            matcher = pattern.matcher(password)
            return matcher.matches()
        }

        fun viewGone(view: View?) {
            if (view != null) {
                view.visibility = View.GONE

            }
        }

        fun viewVisible(view: View?) {
            if (view != null && (view.visibility == View.INVISIBLE || view.visibility == View.GONE)) {
                view.visibility = View.VISIBLE

            }
        }

        fun isEmptyString(value: String?): Boolean {
            return TextUtils.isEmpty(value) || TextUtils.isEmpty(value?.trim())
        }



        fun contains(value: String?, match: String?): Boolean {
            return !isEmptyString(value) && !isEmptyString(match) && value!!.contains(match!!)
        }


    }


}
