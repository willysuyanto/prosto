package com.ladokgi.apps.daftarpasien;

import java.io.Serializable;

public class Pasien implements Serializable {
    String id;
    String nama;
    String alamat;
    String telepon;
    String jenisK;
    String pendidikan;
    String lama;

    public Pasien(String id, String nama, String alamat, String telepon, String jenisK, String pendidikan, String lama) {
        this.id = id;
        this.nama = nama;
        this.alamat = alamat;
        this.telepon = telepon;
        this.jenisK = jenisK;
        this.pendidikan = pendidikan;
        this.lama = lama;
    }

    public String getJenisK() {
        return jenisK;
    }

    public void setJenisK(String jenisK) {
        this.jenisK = jenisK;
    }

    public String getPendidikan() {
        return pendidikan;
    }

    public void setPendidikan(String pendidikan) {
        this.pendidikan = pendidikan;
    }

    public String getLama() {
        return lama;
    }

    public void setLama(String lama) {
        this.lama = lama;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getTelepon() {
        return telepon;
    }

    public void setTelepon(String telepon) {
        this.telepon = telepon;
    }
}
