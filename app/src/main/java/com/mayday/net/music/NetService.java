package com.mayday.net.music;

import android.app.IntentService;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.mayday.tool.localMusicManager.MusicmediaUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 *文件下载类
 */
public class NetService extends IntentService {

    public static final String GETURL = "URL";

    private String url = null;
    private String name = null;

    public NetService() {
        super("NetService");
    }

    @Override
    public void onStart(Intent intent, int startId) {
        if (GETURL.equals(intent.getAction())) {
            url = intent.getStringExtra("url");
            name = intent.getStringExtra("name");
        }
        super.onStart(intent, startId);
    }

    @Override
    protected void onHandleIntent(final Intent intent) {
        //这里本身就开启了一个子线程
        download(url, "Netdownloads");
    }

    private void download(String url, final String saveDir) {
        OkHttpClient client = new OkHttpClient();
        //uir=null??
        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
//                listener.onDownloadFailed();
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String savePath = isExistDir(saveDir);
                InputStream is;
                int len;
                FileOutputStream fos;
                byte[] buff = new byte[2048];
                if (response != null && response.isSuccessful()) {
                    is = response.body().byteStream();
                    //这里不用去更新UI，就不写进度事件了
//                    long length = response.body().contentLength();
                    File file = new File(savePath, name+".mp3");

                    fos = new FileOutputStream(file);
                    while ((len = is.read(buff)) != -1) {
                        fos.write(buff, 0, len);
                    }
                    fos.flush();
                    is.close();
                    fos.close();
                }
            }
        });
    }

    /**
     * 保存文件地址
     *默认保存在SD卡中
     * @param saveDir
     * @return
     * @throws IOException
     */
    @NonNull
    private String isExistDir(String saveDir) throws IOException {
        File file = new File(Environment.getExternalStorageDirectory(),saveDir);//"/mnt/shared"
        if (!file.mkdirs()) {
            file.createNewFile();
        }
        return file.getAbsolutePath();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(NetService.this,"下载成功",Toast.LENGTH_SHORT).show();
        //TODO 发送广播通知本地音乐界面更新UI(暂时就先调用下这个方法吧，先看下运行情况)
        //强制扫描SD卡
        MediaScannerConnection.scanFile(this, new String[] { Environment
                .getExternalStorageDirectory().getAbsolutePath()}, null, null);
    }

    public interface OnDownloadListener {
        /**
         * 下载成功
         */
        void onDownloadSuccess();

        /**
         * 下载失败
         */
        void onDownloadFailed();
    }

}
