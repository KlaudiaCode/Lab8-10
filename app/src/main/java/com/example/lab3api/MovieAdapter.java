package com.example.lab3api;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    List data = new ArrayList();

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;

        public ViewHolder(View view) {
            super(view);
            textView = view.findViewById(R.id.movie_title);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieAdapter.ViewHolder holder, int position) {
        if (data.get(position) instanceof Movie) {
            Movie item = (Movie) data.get(position);
            holder.textView.setText(item.title);
        } else if (data.get(position) instanceof Actor) {
            Actor item = (Actor) data.get(position);
            holder.textView.setText(item.name);
        } else {
            TvShow item = (TvShow) data.get(position);
            holder.textView.setText(item.name);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List data){
        this.data = data;
        notifyDataSetChanged();
    }
}
