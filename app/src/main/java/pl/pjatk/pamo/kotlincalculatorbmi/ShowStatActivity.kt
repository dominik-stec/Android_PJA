package pl.pjatk.pamo.kotlincalculatorbmi

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity


class ShowStatActivity : AppCompatActivity() {
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val webView = WebView(this)
        val webSettings = webView.settings
        webSettings.javaScriptEnabled = true
        webView.loadUrl("file:///android_asset/www/index.html")
        setContentView(webView)
    }
}

//
//import android.annotation.SuppressLint;
//import android.os.Bundle;
//import android.webkit.WebSettings;
//import android.webkit.WebView;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//public class ShowStatActivity extends AppCompatActivity {
//
//    @SuppressLint("SetJavaScriptEnabled")
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        WebView webView = new WebView(this);
//
//        WebSettings webSettings = webView.getSettings();
//        webSettings.setJavaScriptEnabled(true);
//
//        webView.loadUrl("file:///android_asset/www/index.html");
//
//        setContentView(webView);
//    }
//
//}
