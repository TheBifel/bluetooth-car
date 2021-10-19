package dev.bifel.bluetoothcar

import android.content.Context

class Cache(context: Context) {

    private val sp = context.getSharedPreferences("cache", Context.MODE_PRIVATE)

    var lastConnection: String?
        get() = sp.getString(LAST_CONNECTION, null)
        set(value) = sp.edit().putString("LAST_CONNECTION", value).apply()

    companion object {
        private const val LAST_CONNECTION = "LAST_CONNECTION"
    }
}
