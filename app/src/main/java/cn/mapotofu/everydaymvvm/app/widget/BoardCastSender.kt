package cn.mapotofu.everydaymvvm.app.widget

import android.content.Context
import android.content.Intent

object BoardCastSender {
    fun send(context: Context, intent: Intent?) {
        context.sendBroadcast(intent)
    }

    fun notifyWidget(context: Context) {
        context.sendBroadcast(Intent(Actions.UPDATE))
    }
}