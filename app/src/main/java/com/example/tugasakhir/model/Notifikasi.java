package com.example.tugasakhir.model;

public class Notifikasi {
    public Integer id;
    public String gerbong;
    public String isi;
    public Boolean status;


    public Notifikasi(Integer id, String gerbong, String isi, Boolean status) {
        this.id = id;
        this.gerbong = gerbong;
        this.isi = isi;
        this.status = status;
    }

    public Notifikasi(String name, String email, String nik) {
    }
}
