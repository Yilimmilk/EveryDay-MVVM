package cn.mapotofu.everydaymvvm.ui.activity;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.progressindicator.BaseProgressIndicator;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.beta.UpgradeInfo;
import com.tencent.bugly.beta.download.DownloadListener;
import com.tencent.bugly.beta.download.DownloadTask;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.List;

import cn.mapotofu.everydaymvvm.R;
import cn.mapotofu.everydaymvvm.app.App;
import cn.mapotofu.everydaymvvm.app.util.OthersUtil;

/**
 * @author milk
 * @description
 * @package cn.mapotofu.everydaymvvm.ui.activity
 * @date 2021/9/27
 */
public class UpdateActivity extends AppCompatActivity {
    private TextView title, version, size, time, type, downloadProgress, content;
    private Button start, cancel;
    private LinearProgressIndicator progressDownload;
    private String upgradeTypeText;
    private UpgradeInfo upgradeInfo;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_upgrade);
        setStatusBarTransparent(getWindow());

        title = findViewById(R.id.textviewTitle);
        version = findViewById(R.id.textviewVersion);
        size = findViewById(R.id.textviewSize);
        time = findViewById(R.id.textviewTime);
        type = findViewById(R.id.textviewUpdateType);
        downloadProgress = findViewById(R.id.textviewDownloadProgress);
        progressDownload = findViewById(R.id.progressDownload);
        content = findViewById(R.id.textviewContent);
        start = findViewById(R.id.buttonStart);
        cancel = findViewById(R.id.buttonCancel);

        upgradeInfo = Beta.getUpgradeInfo();
        initUpgradeInfo(upgradeInfo);

        /*获取下载任务，初始化界面信息*/
        updateBtn(Beta.getStrategyTask());

        /*为下载按钮设置监听*/
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownloadTask task = Beta.startDownload();
                updateBtn(task);
                progressDownload.setVisibility(View.VISIBLE);
                downloadProgress.setVisibility(View.VISIBLE);
//                if (task.getStatus() == DownloadTask.DOWNLOADING) {
//                    finish();
//                }
            }
        });

        /*为取消按钮设置监听*/
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Beta.cancelDownload();
                finish();
            }
        });

        /*注册下载监听，监听下载事件*/
        Beta.registerDownloadListener(new DownloadListener() {
            @Override
            public void onReceive(DownloadTask task) {
                updateBtn(task);
                float progressPercent = ((float) task.getSavedLength() / (float) upgradeInfo.fileSize) * (float) 100;
                downloadProgress.setText("下载进度: " +  String.format("%.2f", progressPercent) + "%");
                progressDownload.setProgress(Long.valueOf(task.getSavedLength()).intValue());
            }

            @Override
            public void onCompleted(DownloadTask task) {
                updateBtn(task);
                downloadProgress.setText("下载进度: 下载完成");
                progressDownload.setProgress(Long.valueOf(task.getSavedLength()).intValue());
                progressDownload.setIndicatorColor(Color.GREEN);
            }

            @Override
            public void onFailed(DownloadTask task, int code, String extMsg) {
                updateBtn(task);
                downloadProgress.setText("下载进度: 下载失败" );
                progressDownload.setProgress(Long.valueOf(task.getSavedLength()).intValue());
                progressDownload.setIndicatorColor(Color.RED);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /*注销下载监听*/
        Beta.unregisterDownloadListener();
    }

    @Override
    public void onBackPressed() {
        if (upgradeInfo.upgradeType==2) {
            OthersUtil.INSTANCE.exitAPP(App.context);
//            Intent intent = new Intent(Intent.ACTION_MAIN);
//            intent.addCategory(Intent.CATEGORY_HOME);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intent);
//            finish();
        }else {
            super.onBackPressed();
        }
    }


    public void updateBtn(DownloadTask task) {
        /*根据下载任务状态设置按钮*/
        switch (task.getStatus()) {
            case DownloadTask.INIT:
            case DownloadTask.DELETED:
            case DownloadTask.FAILED: {
                start.setText("开始下载");
            }
            break;
            case DownloadTask.COMPLETE: {
                start.setText("安装");
            }
            break;
            case DownloadTask.DOWNLOADING: {
                start.setText("暂停");
            }
            break;
            case DownloadTask.PAUSED: {
                start.setText("继续下载");
            }
            break;
        }
    }

    @SuppressLint("SetTextI18n")
    private void initUpgradeInfo(UpgradeInfo upgradeInfo) {
        if (upgradeInfo == null) return;

        switch (upgradeInfo.upgradeType) {
            case 1:
                upgradeTypeText = "建议更新";
                break;
            case 2:
                upgradeTypeText = "强制更新";
                break;
            case 3:
                upgradeTypeText = "手动更新";
                break;
            default:
                upgradeTypeText = "其他更新";
                break;
        }

        title.setText("更新至\n " + upgradeInfo.title);
        version.setText("目标版本: " + upgradeInfo.versionName);
        size.setText("大小: " + sizeFormatString(upgradeInfo.fileSize));
        time.setText("时间: " + timeStampToDate(upgradeInfo.publishTime));
        //记录一个莫名BUG，若元素无法显示出来，添加换行符，盲猜被挤了
        type.setText("更新类型: " + upgradeTypeText);
        progressDownload.setShowAnimationBehavior(BaseProgressIndicator.SHOW_OUTWARD);
        progressDownload.setMax(Long.valueOf(upgradeInfo.fileSize).intValue());
        if (Beta.getStrategyTask().getSavedLength() != 0) {
            downloadProgress.setVisibility(View.VISIBLE);
            progressDownload.setVisibility(View.VISIBLE);
            long progress = Beta.getStrategyTask().getSavedLength();
            float progressPercent = ((float) progress / (float) upgradeInfo.fileSize) * (float) 100;
            downloadProgress.setText("下载进度: " +  String.format("%.2f", progressPercent) + "%");
            progressDownload.setProgress(Long.valueOf(progress).intValue());
            if (Beta.getStrategyTask().getStatus() == DownloadTask.COMPLETE) {
                downloadProgress.setText("下载进度: 下载完成");
                progressDownload.setProgress(Long.valueOf(Beta.getStrategyTask().getSavedLength()).intValue());
                progressDownload.setIndicatorColor(Color.GREEN);
            }
        }else {
            downloadProgress.setVisibility(View.GONE);
            progressDownload.setVisibility(View.GONE);
        }
        content.setText(upgradeInfo.newFeature);
        content.setMovementMethod(new ScrollingMovementMethod());

        if (upgradeInfo.upgradeType == 2) cancel.setVisibility(View.GONE);

        StringBuilder info = new StringBuilder();
        info.append("id: ").append(upgradeInfo.id).append("\n");
        info.append("标题: ").append(upgradeInfo.title).append("\n");
        info.append("升级说明: ").append(upgradeInfo.newFeature).append("\n");
        info.append("versionCode: ").append(upgradeInfo.versionCode).append("\n");
        info.append("versionName: ").append(upgradeInfo.versionName).append("\n");
        info.append("发布时间: ").append(upgradeInfo.publishTime).append("\n");
        info.append("安装包Md5: ").append(upgradeInfo.apkMd5).append("\n");
        info.append("安装包下载地址: ").append(upgradeInfo.apkUrl).append("\n");
        info.append("安装包大小: ").append(upgradeInfo.fileSize).append("\n");
        info.append("弹窗间隔（ms）: ").append(upgradeInfo.popInterval).append("\n");
        info.append("弹窗次数: ").append(upgradeInfo.popTimes).append("\n");
        info.append("发布类型（0:测试 1:正式）: ").append(upgradeInfo.publishType).append("\n");
        info.append("弹窗类型（1:建议 2:强制 3:手工）: ").append(upgradeInfo.upgradeType).append("\n");
        info.append("图片地址：").append(upgradeInfo.imageUrl);
        Log.d("更新信息", info.toString());
    }

    private static void setStatusBarTransparent(Window window) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    @SuppressLint("DefaultLocale")
    private static String sizeFormatString(long size) {
        double pers = 1048576; //1024*1024
        String s = "";
        if (size > 1024 * 1024)
            s = String.format("%.2f", (double) size / pers) + "M";
        else
            s = String.format("%.2f", (double) size / (1024)) + "KB";
        return s;
    }

    @SuppressLint("SimpleDateFormat")
    private static String timeStampToDate(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
        String sd = sdf.format(time);
        return sd;
    }
}
