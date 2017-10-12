package com.mayday.fragment.manager;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.mayday.net.music.NetMusicAdapter;
import com.mayday.tool.localMusicManager.MusicInfo;
import com.mayday.xy.customproject.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 野接口下载链接，由于他并没有提供在线播放的url地址，所以只要下载下来后才能够在本地播放。
 * Created by xy-pc on 2017/6/28.
 */

public class netMusic_fragment extends Fragment implements View.OnClickListener {
    //用户需要传入关键字
    private static final String BAIDUMUSICURI = "http://tingapi.ting.baidu.com/v1/restserver/ting?from=webapp_music&method=baidu.ting.search.catalogSug&format=json&callback=&query=";

    private ProgressDialog dialog;

    private ListView list_webMusic;
    private EditText select_text;
    private Button bt_select;
    private OkHttpClient client = new OkHttpClient();

    private String getUrlPath = null;

    private ArrayList<MusicInfo> list = null;

    private NetMusicAdapter adapter;

    public String songLink = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.netmusic_fragment, container, false);
        list_webMusic = (ListView) view.findViewById(R.id.list_webMusic);
        select_text = (EditText) view.findViewById(R.id.select_text);
        bt_select = (Button) view.findViewById(R.id.bt_select);
        dialog = new ProgressDialog(getActivity());

        //搜索监听事件
        bt_select.setOnClickListener(this);
        return view;
    }

    public String getAdress(final String songid) {
//        String str = null;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpURLConnection connection;
                    //URL url = new URL("http://api.5288z.com/weixin/musicapi.php?q="+finalTitle);
                    URL url = new URL("http://ting.baidu.com/data/music/links?songIds=" + songid);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setConnectTimeout(60 * 1000);
                    connection.setReadTimeout(60 * 1000);
                    connection.connect();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String s;
                    if ((s = reader.readLine()) != null) {
                        s = s.replace("\\", "");//去掉\\
                        try {
                            JSONObject object = new JSONObject(s);
                            JSONObject object1 = object.getJSONObject("data");
                            JSONArray array = object1.getJSONArray("songList");
                            JSONObject object2 = array.getJSONObject(0);
                            songLink = object2.getString("songLink");
//                            songLength = object2.getLong("size");
//                            str[1]=String.valueOf(songLength);
//                            Log.v("tagadress", songLink);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return songLink;
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0x1:
                    list = (ArrayList<MusicInfo>) msg.obj;
                    adapter = new NetMusicAdapter(getActivity(), list);
                    list_webMusic.setAdapter(adapter);
                    dialog.dismiss();
                    break;
            }
        }
    };


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_select:
                //隐藏软键盘
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                String inputData = select_text.getText().toString();
                try {
                    inputData = URLEncoder.encode(inputData, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                //得到网页源码
                getUrlPath = BAIDUMUSICURI + inputData;
                Log.i("mayday--------->>", getUrlPath);
                if (inputData == null || inputData.equals("")) {
                    Toast.makeText(getActivity(), "关键字不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    dialog.show();
                    // 这里需要将下载过的音乐的url地址存入数据库中，这样我们再次下载的相同的音乐的时候就会提示该歌曲已下载的消息框(就不做这方面的处理了)。
                    //这个检查网路状态的代码好像也有问题
                    //TODO 检查网路状态的代码有误
                    if (isNetworkAvailable(getActivity())) {
                        Log.i("mayday", "boolean --->>" + isNetworkAvailable(getActivity()));
                        query2(getUrlPath);
                    } else {
                        dialog.dismiss();
                        Toast.makeText(getActivity(), "请检查网路连接状态", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    private void query2(final String url) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String urladd = getUrladd(url);
                System.out.println(urladd);
                System.out.println(urladd.length());
                doJson(urladd);
            }
        }).start();
    }

    /**
     * 处理Strig类型的参数不能转换为jsonObject格式的情况(这里为啥会出现不能转换的问题呢？因为在我们的json数据的头部有一个()的符号，导致其无法解析，我们通过.replace也没有过滤掉，
     * 因此我们使用substring(1)来把头符号过滤掉。这样子就可以正常的去解析我们的json数据了
     *
     * @param jsonData
     * @return
     */
    private void doJson(String jsonData) {
        MusicInfo musicinfo = null;
        JSONObject jsonObject;
        final ArrayList<MusicInfo> lists = new ArrayList<>();
        try {
//            jsonData.replace("(", "");
//            jsonData.replace(")", "");
            String substring = jsonData.substring(1);
            System.out.println(substring);
            jsonObject = new JSONObject(substring);
            JSONArray array;
            array = jsonObject.getJSONArray("song");
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                String songname = object.getString("songname");
                String artistname = object.getString("artistname");
                String songid = object.getString("songid");
                //将songid传入到相应的函数里进行处理(每一个对象体),返回一个音乐的url地址
                String adress = getAdress(songid);

                musicinfo = new MusicInfo();
                musicinfo.setUrl(adress);
//                String time = MusicmediaUtils.formatTime(Long.valueOf(adress[1]));
//                musicinfo.setSize();
                musicinfo.setTitle(songname);
                musicinfo.setSinger(artistname);
                lists.add(musicinfo);
                Log.i("xiaoxiao", lists.get(i).getTitle() + "\t");
            }

            Message message = handler.obtainMessage();
            message.what = 0x1;
            message.obj = lists;
            handler.sendMessage(message);

        } catch (JSONException e) {
            e.printStackTrace();
        }
//        return musicinfo;
    }

    // 判断网络是否连接状态(是否连接，连接是否可用一直判断不了，就不做判断了)
    /*public static boolean isNetWork(Context context) {
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connManager.getActiveNetworkInfo();

        //判断网络是否连接
        if (networkInfo == null || !networkInfo.isAvailable()) {
            // 当前没有网络连接
            Toast.makeText(context.getApplicationContext(), "请先连接Internet！",
                    Toast.LENGTH_SHORT).show();
            return false;
            //判断网咯连接是否可用
        }else {
            return true;
        }
    }*/

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected())
            {
                // 当前网络是连接的
                if (info.getState() == NetworkInfo.State.CONNECTED)
                {
                    // 当前所连接的网络可用
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 不知道为啥，通过okhttp来请求访问，获取不到数据。
     *
     * @param url
     */
    public void dataWebDataMusic(final String url) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final ArrayList<MusicInfo> list = new ArrayList<>();

                Request request = new Request.Builder().url(url).build();
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        //返回403
                        if (response != null && response.isSuccessful()) {
                            String webPath = response.body().string();
                            Log.i("mayday------>>", webPath);
                            try {
//                                webPath.replace("(", "");
//                                webPath.replace(")", "");

                                //获取头标签
                                JSONArray jsonArray = new JSONObject(webPath).getJSONArray("song");
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    MusicInfo musicInfo = new MusicInfo();
                                    musicInfo.setId(jsonObject.getLong("songid"));
                                    musicInfo.setTitle(jsonObject.getString("songname"));
                                    musicInfo.setSinger(jsonObject.getString("artistname"));

                                    list.add(musicInfo);
                                    Log.i("ashin", list.get(i).getTitle() + "\n" + list.get(i).getSinger());
                                }

//                                Message message = new myHandler().obtainMessage();
//                                message.what = 0x1;
//                                message.obj = list;
//                                new myHandler().sendMessage(message);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }
        }).start();

    }


    public static String getUrladd(String url) {
        StringBuilder sb = new StringBuilder();
        BufferedReader buffered = null;
        try {
            URL geturl = new URL(url);
            buffered = new BufferedReader(new InputStreamReader(geturl.openStream(), Charset.forName("UTF-8")));
            String team;
            while ((team = buffered.readLine()) != null) {
                sb.append(team);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (buffered != null) {
                    buffered.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}
