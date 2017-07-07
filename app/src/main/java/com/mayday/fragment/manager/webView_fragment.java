package com.mayday.fragment.manager;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
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
 * Created by xy-pc on 2017/3/28.
 */

public class webView_fragment extends Fragment{
    private WebView wView;
    private ImageView web_back,iv_refluse;
    private TextView titles,tv_errors;

    private RelativeLayout webview_title;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.webmusic_fragment, container, false);
        webview_title= (RelativeLayout) view.findViewById(R.id.webview_title);
        wView= (WebView) view.findViewById(R.id.wView);
        web_back= (ImageView) view.findViewById(R.id.web_back);
        iv_refluse= (ImageView) view.findViewById(R.id.iv_refluse);
        titles= (TextView) view.findViewById(R.id.titles);
        tv_errors= (TextView) view.findViewById(R.id.tv_errors);

        initWebViewData();
        web_back.setOnClickListener(new MyClicke());
        iv_refluse.setOnClickListener(new MyClicke());
        return view;
    }

    class MyClicke implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            switch (view.getId()){
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
        webSettings.setBlockNetworkImage(true);// 把图片加载放在最后来加载渲染
//        wView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        // 支持多窗口
        webSettings.setSupportMultipleWindows(true);
        // 开启 DOM storage API 功能
        webSettings.setDomStorageEnabled(true);
        // 开启 Application Caches 功能
        webSettings.setAppCacheEnabled(true);

        /**
         * 辅助 WebView 处理 Javascript 的对话框,网站图标,网站标题等等。
         */
        wView.setWebChromeClient(new WebChromeClient(){

            //获得网页的加载进度并显示
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if(newProgress<100){
                    String progress=newProgress+"%";
                    Log.i("加载网页进度", "onProgressChanged: "+progress);
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
        wView.setWebViewClient(new WebViewClient(){

            //开始载入页面调用的，我们可以设定一个loading的页面，告诉用户程序在等待网络响应。
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return super.shouldOverrideUrlLoading(view, request);
            }

            //打开网页时不调用系统浏览器， 而是在本WebView中显示
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            //该方法用于显示错入信息(获取不到网页信息或者没有网络的状态下加载界面)
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                wView.setVisibility(View.GONE);
                tv_errors.setTextColor(Color.YELLOW);
                tv_errors.setText("404 error");
            }

            //在加载页面资源时会调用，每一个资源（比如图片）的加载都会调用一次。
            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
            }

            //处理https请求
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                super.onReceivedSslError(view, handler, error);
                handler.proceed();
            }

            //在页面加载结束时调用。我们可以关闭loading 条，切换程序动作。
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
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
    public void onResume() {
        wView.getSettings().setJavaScriptEnabled(false);
        super.onResume();
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
