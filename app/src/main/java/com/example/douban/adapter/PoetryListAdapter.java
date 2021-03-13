package com.example.douban.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.douban.R;
import com.example.douban.bean.Poetry;

import java.util.List;

public class PoetryListAdapter extends RecyclerView.Adapter<PoetryListAdapter.ViewHolder>{

    public List<Poetry> poetries;

    public PoetryListAdapter(List<Poetry> poetriesList) {
        poetries = poetriesList;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView content, source;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            content = itemView.findViewById(R.id.content);
            source = itemView.findViewById(R.id.source);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.poetry_item, null);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Poetry poetry = poetries.get(position);
        holder.content.setText(poetry.getContent().toString());
        holder.source.setText(poetry.getSource().toString());
    }

    @Override
    public int getItemCount() {
        return poetries.size();
    }


}
