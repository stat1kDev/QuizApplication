package stat1kDev.quizapp.web;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import stat1kDev.quizapp.constants.AppConstants;
import stat1kDev.quizapp.listeners.WebListener;

public class WebEngine {

    private WebView webView;
    private Activity activity;
    private Context context;

    private static final String GOOGLE_DOCS_VIEWER = "https://docs.google.com/viewerng/viewer?url=";

    private WebListener webListener;

    public WebEngine(WebView webView, Activity activity) {
        this.webView = webView;
        this.activity = activity;
        this.context = activity.getApplicationContext();
    }

    public void initWebView() {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setAppCacheMaxSize(AppConstants.SITE_CASHE_SIZE);
        webView.getSettings().setAppCachePath(context.getCacheDir().getAbsolutePath());
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setDefaultTextEncodingName("utf-8");
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);

        if (!isNetworkAvailable(context)) {
            webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        }
    }

    public void initListeners(final WebListener webListener) {
        this.webListener = webListener;

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                webListener.onProgress(newProgress);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                webListener.onPageTitle(webView.getTitle());
            }
        });

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String webUrl) {

                loadPage(webUrl);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                webListener.onStart();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                webListener.onLoaded();
            }
        });
    }


    public void loadPage(String webUrl) {
        if (isNetworkAvailable(context)) {
            if (webUrl.startsWith("tel:") ||
                    webUrl.startsWith("sms:") ||
                    webUrl.startsWith("mms:") ||
                    webUrl.startsWith("smsto:") ||
                    webUrl.startsWith("mmsto:") ||
                    webUrl.startsWith("mailto:") ||
                    webUrl.contains("geo:")) {
                invokeNativeApp(webUrl);
            } else if (webUrl.contains("?target=blank")) {
                invokeNativeApp(webUrl.replace("?target=blank", ""));
            } else if (webUrl.endsWith(".doc") ||
                    webUrl.endsWith(".docx") ||
                    webUrl.endsWith(".xls") ||
                    webUrl.endsWith(".xlsx") ||
                    webUrl.endsWith(".pptx") ||
                    webUrl.endsWith(".pdf")) {
                webView.loadUrl(GOOGLE_DOCS_VIEWER + webUrl);
                webView.getSettings().setBuiltInZoomControls(true);
            } else {
                webView.loadUrl(webUrl);
            }

        } else {
            webListener.onNetworkError();
        }
    }

    private boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    private void invokeNativeApp(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        activity.startActivity(intent);
    }
}
