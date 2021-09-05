package cn.mapotofu.everydaymvvm.app.services;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import androidx.annotation.Nullable;

/**
 * @author milk
 * @description 该服务只用来让APP重启，生命周期也仅仅是只是重启APP。重启完即自我杀死
 * @package cn.mapotofu.everydaymvvm.app.services
 * @date 2021/8/31
 */
public class KillSelfService extends Service {
    /**关闭应用后多久重新启动*/
    private static  long stopDelayed=2000;
    private Handler handler;
    private String PackageName;
    public KillSelfService() {
        handler=new Handler();
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        stopDelayed=intent.getLongExtra("Delayed",2000);
        PackageName=intent.getStringExtra("PackageName");
        handler.postDelayed(()->{
            Intent LaunchIntent = getPackageManager().getLaunchIntentForPackage(PackageName);
            startActivity(LaunchIntent);
            KillSelfService.this.stopSelf();
        },stopDelayed);
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
