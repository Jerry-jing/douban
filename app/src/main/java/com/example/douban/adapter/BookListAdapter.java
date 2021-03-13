package com.example.douban.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.douban.R;
import com.example.douban.bean.Book;

import java.util.List;


public class BookListAdapter extends RecyclerView.Adapter<BookListAdapter.ViewHolder> {

    private List<Book> mBooks;
    private Context mContext;

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView title;
        TextView score;
        TextView abstractX;
        TextView time;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.book_image);
            title = itemView.findViewById(R.id.book_title);
            score = itemView.findViewById(R.id.score);
            abstractX = itemView.findViewById(R.id.information);
            time = itemView.findViewById(R.id.time);
        }
    }

    public BookListAdapter(List<Book> bookList) {
        mBooks = bookList;
    }

    @NonNull
    @Override
    public BookListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        mContext = parent.getContext();
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Book book = mBooks.get(position);
        holder.title.setText(book.getTitle());
        holder.score.setText(book.getScore());
        holder.abstractX.setText(book.abstractX.getAuthor() + book.abstractX.getPress() + book.abstractX.getPublicationYear());
        //holder.abstractX.setText((CharSequence) book.getAbstractX());
        holder.time.setText(book.getTime());
        Glide.with(mContext).load(book.getImg()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return mBooks.size();
    }

}
