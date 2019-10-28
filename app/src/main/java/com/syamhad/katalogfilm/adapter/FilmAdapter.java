package com.syamhad.katalogfilm.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.syamhad.katalogfilm.R;
import com.syamhad.katalogfilm.model.Film;

import java.util.ArrayList;

public class FilmAdapter extends RecyclerView.Adapter<FilmAdapter.CardViewViewHolder>{
    private ArrayList<Film> listFilm = new ArrayList<>();
    private OnItemClickCallback onItemClickCallback;

    public ArrayList<Film> getListFilm() {
        return listFilm;
    }

    public void setData(ArrayList<Film> items) {
        listFilm.clear();
        listFilm.addAll(items);
        notifyDataSetChanged();
    }

    public void addItem(final Film item) {
        listFilm.add(item);
        notifyDataSetChanged();
    }

    public void clearData() {
        listFilm.clear();
    }

    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    @NonNull
    @Override
    public CardViewViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_row_film, viewGroup, false);
        return new CardViewViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull CardViewViewHolder holder, final int position) {
        holder.bind(listFilm.get(position));
    }
    @Override
    public int getItemCount() {
        return listFilm.size();
    }

    public class CardViewViewHolder extends RecyclerView.ViewHolder {
        ImageView imgGambar;
        TextView tvJudul, tvKeterangan;
        CardViewViewHolder(View itemView) {
            super(itemView);
            imgGambar = itemView.findViewById(R.id.img_item_gambar);
            tvJudul = itemView.findViewById(R.id.tv_item_judul);
            tvKeterangan = itemView.findViewById(R.id.tv_item_ket);
        }
        void bind(Film item){
            tvJudul.setText(item.getJudul());
            tvKeterangan.setText(item.getKeterangan());
            Glide.with(itemView.getContext())
                    .load("https://image.tmdb.org/t/p/w185"+item.getGambar())
                    .apply(new RequestOptions().placeholder(R.drawable.noimage).error(R.drawable.noimage))
                    .into(imgGambar);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickCallback.onItemClicked(listFilm.get(getAdapterPosition()));
                }
            });
        }
    }

    public interface OnItemClickCallback {
        void onItemClicked(Film data);
    }
}
