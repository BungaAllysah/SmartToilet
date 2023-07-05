package com.example.tugasakhir.model;

public class LaporanPengaduan {
    public Integer id;
    public String judul;
    public String tanggal;
    public String kereta;
    public String gerbong;
    public String kursi;
    public String isi;
    public String keterangan;
    public Boolean Status;

    public LaporanPengaduan(Integer id, String judul, String tanggal, String kereta, String gerbong, String kursi, String isi, String keterangan, Boolean status) {
        this.id = id;
        this.judul = judul;
        this.tanggal = tanggal;
        this.kereta = kereta;
        this.gerbong = gerbong;
        this.kursi = kursi;
        this.isi = isi;
        this.keterangan = keterangan;
        Status = status;
    }
}
