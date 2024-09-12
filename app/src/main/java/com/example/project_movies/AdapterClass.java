package com.example.project_movies;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterClass extends RecyclerView.Adapter<AdapterClass.MyViewHolder> {
    private ArrayList movie_id, movie_title, movie_description, movie_image, movie_releaseDate, movie_bookmarked;
    int position;
    private Context context;

    public AdapterClass(Context context, ArrayList movie_id, ArrayList movie_title,
                        ArrayList movie_description, ArrayList movie_image, ArrayList movie_releaseDate, ArrayList movie_bookmarked){
        this.context = context;
        this.movie_id = movie_id;
        this.movie_title = movie_title;
        this.movie_description = movie_description;
        this.movie_image = movie_image;
        this.movie_releaseDate = movie_releaseDate;
        this.movie_bookmarked = movie_bookmarked;

    }

    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.number_list_item, viewGroup, false);
        return new MyViewHolder(view);
    }

    public int getItemCount() {
        return movie_id.size();
    }


    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.tv_movie_id.setText(String.valueOf(movie_id.get(position)));
        holder.tv_movie_title.setText(String.valueOf(movie_title.get(position)));
        holder.tv_movie_description.setText(String.valueOf(movie_description.get(position)));
        holder.tv_selectedDate.setText(String.valueOf(movie_releaseDate.get(position)));
        holder.mainLayout.setOnClickListener(v -> {
            Intent intent = new Intent(context, EditActivity.class);
            intent.putExtra("id", String.valueOf(movie_id.get(position)));
            intent.putExtra("title", String.valueOf(movie_title.get(position)));
            intent.putExtra("description", String.valueOf(movie_description.get(position)));
            intent.putExtra("releaseDate", String.valueOf(movie_releaseDate.get(position)));
            intent.putExtra("bookmarked", String.valueOf(movie_bookmarked.get(position)));
            context.startActivity(intent);
        });
    }
    static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tv_movie_id, tv_movie_title, tv_movie_description, tv_selectedDate;
        LinearLayout mainLayout;
        public MyViewHolder(View itemView) {
            super(itemView);
            tv_movie_id = itemView.findViewById(R.id.tv_movie_id);
            tv_movie_title = itemView.findViewById(R.id.tv_movie_title);
            tv_movie_description = itemView.findViewById(R.id.tv_movie_description);
            tv_selectedDate = itemView.findViewById(R.id.tv_movie_release);
            mainLayout = itemView.findViewById(R.id.mainLayout);
        }
    }

}
