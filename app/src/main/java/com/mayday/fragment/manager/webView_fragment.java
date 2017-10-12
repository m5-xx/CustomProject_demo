package com.mayday.fragment.manager;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mayday.xy.customproject.R;

/**
 * Created by xy-pc on 2017/7/10.
 */

public class webView_fragment extends Fragment {
    private WebView wView;
    private ImageView web_back, iv_refluse;
    private TextView titles, tv_errors;

    private RelativeLayout webview_title;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.webmusic_fragment, container, false);
        webview_title = (RelativeLayout) view.findViewById(R.id.webview_title);
        wView = (WebView) view.findViewById(R.id.wView);
        web_back = (ImageView) view.findViewById(R.id.web_back);
        iv_refluse = (ImageView) view.findViewById(R.id.iv_refluse);
        titles = (TextView) view.findViewById(R.id.titles);
        tv_errors = (TextView) view.findViewById(R.id.tv_errors);

        initWebViewData();
        web_back.setOnClickListener(new MyClicke());
        iv_refluse.setOnClickListener(new MyClicke());
        return view;
    }

    class MyClicke implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.iv_refluse:
                    wView.reload();
                    break;

                case R.id.web_back:
                    wView.goBack();
                    break;
            }
        }
    }

    private void initWebViewData() {
        webview_title.setVisibility(View.GONE);
        wView.loadUrl("http://www.xiami.com/");

        WebSettings webSettings = wView.getSettings();
        //网页中有JS的话，就要添加该行代码来支持它
        webSettings.setJavaScriptEnabled(true);
        // 设置 缓存模式
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
//        webSettings.setBlockNetworkImage(true);// 把图片加载放在最后来加载渲染

        // 开启 Application Caches 功能
        webSettings.setAppCacheEnabled(true);

        /**
         * 辅助 WebView 处理 Javascript 的对话框,网站图标,网站标题等等。
         */
        wView.setWebChromeClient(new WebChromeClient() {

            //获得网页的加载进度并显示
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress < 100) {
                    String progress = newProgress + "%";
                    Log.i("加载网页进度", "onProgressChanged: " + progress);
                }
            }

            //获取Web页中的标题
            @Override
            public void onReceivedTitle(WebView view, String title) {
                titles.setText(title);
                super.onReceivedTitle(view, title);
            }
        });

        /**
         * 处理各种通知 & 请求事件
         */
        wView.setWebViewClient(new WebViewClient() {
            //打开网页时不调用系统浏览器， 而是在本WebView中显示
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            //该方法用于显示错入信息(获取不到网页信息或者没有网络的状态下加载界面)
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                wView.setVisibility(View.GONE);
                tv_errors.setTextColor(Color.YELLOW);
                tv_errors.setText("404 error");
            }

        });

    }

    /**
     * 若加载的html里面JS在执行动画等操作，会造成资源浪费(CPU、电量)
     * 下面onResume()方法也是同样的道理
     */
    @Override
    public void onStop() {
        wView.getSettings().setJavaScriptEnabled(false);
        super.onStop();
    }

    @Override
    public void onDestroy() {
        //防止内存泄露
        wView.removeAllViews();
        wView.destroy();
        //清除访问历史记录
        wView.clearHistory();
        super.onDestroy();
    }
}
