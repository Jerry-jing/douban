package com.example.douban.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.douban.R;
import com.example.douban.adapter.BookListAdapter;
import com.example.douban.bean.AbstractX;
import com.example.douban.bean.Book;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class BookFragment extends Fragment {

    private String id;
    private String title;
    private String img;
    private String score;
    private String people;
    private String time;
    private int order_num;
    private AbstractX abstractX;
    private RecyclerView recyclerView;
    private List<Book> bookList = new ArrayList<>();
    private String path = "http://39.105.38.10:8081/" + "book/top250?page=0";
    private SwipeRefreshLayout swipeRefreshLayout;
    public LinearLayoutManager layoutManager;
    public BookListAdapter adapter;


    @Override
    public void onStart() {
        super.onStart();
        requestData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book, container, false);
        swipeRefreshLayout = view.findViewById(R.id.book_swipe_refresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        recyclerView = view.findViewById(R.id.book_fragment_recycler);
        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestData();
            }
        });

        return view;
    }


    public void requestData() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url(path).build();
                try{
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    parseJson(responseData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void parseJson(String responseData) {
        if (responseData != null) {
            try {
                JSONObject jsonObject = new JSONObject(responseData);
                JSONObject jsonData = jsonObject.getJSONObject("data");
                JSONArray jsonSubject = jsonData.getJSONArray("subject");
                for (int i = 0; i < jsonSubject.length(); i++) {
                    JSONObject jsonObject_i = jsonSubject.getJSONObject(i);
                    id = jsonObject_i.getString("id");
                    title = jsonObject_i.getString("title");
                    img = jsonObject_i.getString("img");
                    score = jsonObject_i.getString("score");
                    people = jsonObject_i.getString("people");
                    order_num = jsonObject_i.getInt("order_num");
                    time = jsonObject_i.getString("time");
                    JSONArray jsonArray = jsonObject_i.getJSONArray("abstract");


                    AbstractX abstractX = new AbstractX();
                    abstractX.author = jsonArray.getString(0);
                    abstractX.press = jsonArray.getString(1);
                    if(jsonArray.length() > 2) {
                        abstractX.publicationYear = jsonArray.getString(2);
                    }

                    bookList.add(new Book(id, title, img, score, people, time, order_num, abstractX));
                }
                adapter = new BookListAdapter(bookList);

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                        recyclerView.setAdapter(adapter);
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}