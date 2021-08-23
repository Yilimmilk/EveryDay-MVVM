package cn.mapotofu.everydaymvvm.app.widget

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import cn.mapotofu.everydaymvvm.app.App
import cn.mapotofu.everydaymvvm.app.widget.BoardCastSender.send

class KeepService : Service() {
    override fun onBind(intent: Intent): IBinder? {
        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun onCreate() {
        Log.d("朝暮-小组件","onCreate KeepService")
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent, i: Int, i2: Int): Int {
        Log.d("朝暮-小组件","onStartCommand KeepService")
        val stringExtra = intent.getStringExtra(ACTION_NAME)
        val sb = StringBuilder()
        sb.append("启动辅助服务！")
        sb.append(stringExtra)
        Toast.makeText(App.context, sb.toString(), Toast.LENGTH_SHORT).show()
        val intent2 = Intent(stringExtra)
        val str = EXTRA_STR
        intent2.putExtra(str, intent.getStringExtra(str))
        send(this, intent2)
        return START_STICKY
    }

    override fun onDestroy() {
        Log.d("朝暮-小组件","onDestroy KeepService")
        super.onDestroy()
    }

    companion object {
        const val ACTION_NAME = "ACTION_NAME"
        const val EXTRA_STR = "EXTRA_STR"
    }
}