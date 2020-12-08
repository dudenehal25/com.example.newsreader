package com.example.newsreader;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    ArrayList<String> newsTitle  = new ArrayList<>();
    ArrayAdapter myAdapter;
    String result = "";
    static ArrayList<String> pageOpenUrl = new ArrayList<>();

    public class DownloadData extends AsyncTask<String , Void ,String> {
        @Override
        protected String doInBackground(String... urls) {
            String topArticles ="";
            URL url;
            HttpURLConnection urlConnection;

            try {
                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection)url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();

                while (data != -1) {
                    char current = (char) data;
                    topArticles += current;
                    data = reader.read();
                }

                //TO GET ID OF ARTICLE AND STORE IT INTO INT array
                int[] newsId  = new int[9];
                int no= 0;


                Pattern p = Pattern.compile(", (.*?),");
                Matcher m = p.matcher(topArticles);
                while (m.find()) {
                    newsId[no] = Integer.parseInt(m.group(1));
                    no++;
                    if (no>8)
                        break;
                }

                for (int i=0;i<newsId.length;i++){
                    Log.i("------------------------------",Integer.toString(newsId[i]));
                    try {
                        String articleInfo="";
                        url = new URL("https://hacker-news.firebaseio.com/v0/item/" + newsId[i] +".json?print=pretty");
                        urlConnection = (HttpURLConnection)url.openConnection();
                        in = urlConnection.getInputStream();
                        reader = new InputStreamReader(in);
                        data = reader.read();

                        while (data != -1) {
                            char current = (char) data;
                            articleInfo += current;
                            data = reader.read();
                        }

                        JSONObject jsonObject = new JSONObject(articleInfo);
                        String newstitle = jsonObject.getString("title");
                        pageOpenUrl.add(jsonObject.getString("url"));
                        Log.i("---------------TITLE--------" , newstitle);
                       // Log.i("---------------URL --------" , pageOpenUrl);
                        newsTitle.add(newstitle);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
                return topArticles;
            }
            catch (Exception e) {
                e.printStackTrace();
                return topArticles;
            }
        }

    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listViewNews = findViewById(R.id.ListViewNews);

        //setting adapter
        myAdapter = new ArrayAdapter<>(getApplicationContext() , android.R.layout.simple_expandable_list_item_1 ,newsTitle);
        listViewNews.setAdapter(myAdapter);

        DownloadData  data = new DownloadData();

        try {
            result = data.execute("https://hacker-news.firebaseio.com/v0/topstories.json?print=pretty").get();
            Log.i("-------------------------------", result);
        }
        catch (Exception e ){
            e.printStackTrace();
        }

        myAdapter.notifyDataSetChanged();

        listViewNews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent  intent = new Intent(getApplicationContext(), openPage.class);
                intent.putExtra("noteId" , i);
                startActivity(intent);
            }
        });





    }
}