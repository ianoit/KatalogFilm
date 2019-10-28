package com.syamhad.katalogfilm.model;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.syamhad.katalogfilm.R;

import org.json.JSONObject;

public class Film implements Parcelable {
    private int id, idmoviedb;
    private float voteavg;
    private String judul, gambar, banner, keterangan, rilisdate, language, jenis;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public String getJenis() {
        return jenis;
    }

    public void setJenis(String jenis) {
        this.jenis = jenis;
    }

    public int getIdmoviedb() {
        return idmoviedb;
    }

    public void setIdmoviedb(int idmoviedb) {
        this.idmoviedb = idmoviedb;
    }

    public float getVoteavg() {
        return voteavg;
    }

    public void setVoteavg(float voteavg) {
        this.voteavg = voteavg;
    }

    public String getRilisdate() {
        return rilisdate;
    }

    public void setRilisdate(String rilisdate) {
        this.rilisdate = rilisdate;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.idmoviedb);
        dest.writeFloat(this.voteavg);
        dest.writeString(this.judul);
        dest.writeString(this.gambar);
        dest.writeString(this.banner);
        dest.writeString(this.keterangan);
        dest.writeString(this.rilisdate);
        dest.writeString(this.language);
        dest.writeString(this.jenis);
    }

    public Film() {
    }

    protected Film(Parcel in) {
        this.id = in.readInt();
        this.idmoviedb = in.readInt();
        this.voteavg = in.readFloat();
        this.judul = in.readString();
        this.gambar = in.readString();
        this.banner = in.readString();
        this.keterangan = in.readString();
        this.rilisdate = in.readString();
        this.language = in.readString();
        this.jenis = in.readString();
    }

    public static final Parcelable.Creator<Film> CREATOR = new Parcelable.Creator<Film>() {
        @Override
        public Film createFromParcel(Parcel source) {
            return new Film(source);
        }

        @Override
        public Film[] newArray(int size) {
            return new Film[size];
        }
    };

    Film(JSONObject object, String kategori) {
        try {
            String title = "", desc = "", img = "", bdrop = "", rilis = "", lang = "", jenis = "";
            int idmovie;
            double vote;
            if(kategori.equalsIgnoreCase("tv")){
                title = object.getString("original_name");
                rilis = object.getString("first_air_date");
                jenis = "tv";
            }
            else{
                title = object.getString("original_title");
                rilis = object.getString("release_date");
                jenis = "movie";
            }

            idmovie = object.getInt("id");
            vote = object.getDouble("vote_average");
            desc = object.getString("overview");
            img = object.getString("poster_path");
            bdrop = object.getString("backdrop_path");
            lang = object.getString("original_language");

            this.idmoviedb = idmovie;
            this.voteavg = (float) vote;
            this.judul = title;
            this.keterangan = desc;
            this.gambar = img;
            this.banner = bdrop;
            this.rilisdate = rilis;
            this.language = lang;
            this.jenis = jenis;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
