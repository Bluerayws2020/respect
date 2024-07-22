package com.blueray.respect_new.helpers

import android.content.Context
import android.content.Intent

object HelperUtils {

    const val SHARED_PREF = "RESPECT_KEY"
    const val BASE_URL = "http://respect.com.dedi5536.your-server.de"
    fun getLang(mContext: Context?): String {
        val sharedPreferences = mContext?.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)
        return sharedPreferences?.getString("lang", "ar")!!
    }

    fun getUID(mContext: Context?): String {
        val sharedPreferences = mContext?.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)
        return sharedPreferences?.getString("uid", "0")!!
    }

    fun getUserName(mContext: Context?): String {
        val sharedPreferences = mContext?.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)
        return sharedPreferences?.getString("user_name", "0")!!
    }
    fun getUserRole(mContext: Context?): String {
        val sharedPreferences = mContext?.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)
        return sharedPreferences?.getString("role", "0")!!
    }

    fun getUserImage(mContext: Context?): String {
        val sharedPreferences = mContext?.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)
        return sharedPreferences?.getString("user_image", "")!!
    }
    fun setUserImage(mContext: Context?,img: String?){
        val sharedPreferences = mContext?.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)
        val editor = sharedPreferences?.edit()
        editor?.putString("user_image",img)
        editor.apply {  }
    }

    fun getToken(mContext: Context?): String {
        val sharedPreferences = mContext?.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)
        return sharedPreferences?.getString("token", "0")!!
    }

    fun isGuest(mContext: Context?): Boolean {
        val sharedPreferences = mContext?.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)
        val isUserIDEmpty = sharedPreferences?.getString("uid", "0") == "0"
//        if (isUserIDEmpty)
//            openLoginActivity(mContext)
        return isUserIDEmpty
    }

//    fun openLoginActivity(mContext: Context?) {
//       // Toast.makeText(mContext, mContext?.getString(R.string.guest_login), Toast.LENGTH_LONG).show()
//        val splashIntent = Intent(mContext, StartUpActivity::class.java)
//        mContext?.startActivity(splashIntent)
//    }
}