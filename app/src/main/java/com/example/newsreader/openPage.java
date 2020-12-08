package com.example.newsreader;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import static com.example.newsreader.MainActivity.pageOpenUrl;


public class openPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        Intent intent = getIntent();
        final int noteId = intent.getIntExtra("noteId" , -1);
        Log.i("!!!!!!!!11111111111111111111111!!!!!!!!", String.valueOf(noteId));
        if (noteId != -1){
            WebView webView = findViewById(R.id.webView);

            webView.getSettings().setJavaScriptEnabled(true);
            webView.setWebViewClient(new WebViewClient());

            webView.loadUrl(pageOpenUrl.get(noteId));
        }


    }
}