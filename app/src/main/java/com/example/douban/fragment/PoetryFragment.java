package com.example.douban.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.douban.R;
import com.example.douban.adapter.PoetryListAdapter;
import com.example.douban.bean.Poetry;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PoetryFragment extends Fragment {

    public String content;
    public String source;
    private RecyclerView recyclerView;
    private String responseData;
    public SwipeRefreshLayout swipeRefreshLayout;
    public ProgressBar progressBar;
    public TextView messageText;
    public List<Poetry> poetries = new ArrayList<>();
    public LinearLayoutManager layoutManager;
    public PoetryListAdapter adapter;


    public static PoetryFragment newInstance() {
        Bundle args = new Bundle();
    PoetryFragment fragment = new PoetryFragment();
        fragment.setArguments(args);
        return fragment;
}

    @Override
    public void onStart() {
        super.onStart();
        okhttpData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_poetry, container, false);
        recyclerView = view.findViewById(R.id.poetry_fragment_recycler);
        swipeRefreshLayout = view.findViewById(R.id.poetry_swipe_refresh);
        swipeRefreshLayout.setRefreshing(false);
//        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        progressBar = view.findViewById(R.id.fragment_progress);
        messageText = view.findViewById(R.id.fragment_message);
        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                    okhttpData();
            }
        });
        return view;
    }

    private void okhttpData() {
        progressBar.setVisibility(View.VISIBLE);
        messageText.setVisibility(View.VISIBLE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url("http://yijuzhan.com/api/word.php?m=json").build();
                try {
                    Response response = client.newCall(request).execute();
                    responseData = response.body().string();
                    jsonJXDate(responseData);
                } catch (IOException e) {
                    e.printStackTrace();
                }



            }
        }).start();
    }

    private void jsonJXDate(String responseData) {
        if(responseData != null) {
            try {

                JSONObject jsonObject = new JSONObject(responseData);
                content = jsonObject.getString("content");
                source = jsonObject.getString("source");
                poetries.clear();
                poetries.add(new Poetry(content, source));
                adapter = new PoetryListAdapter(poetries);

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.setAdapter(adapter);
                        swipeRefreshLayout.setRefreshing(false);
                        progressBar.setVisibility(View.GONE);
                        messageText.setVisibility(View.GONE);
                    }
                });


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}