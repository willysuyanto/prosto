package com.ladokgi.apps;

import java.io.Serializable;

public class Pasien implements Serializable {
    String id;
    String nama;
    String alamat;
    String telepon;

    public Pasien(String nama, String alamat, String telepon, String id) {
        this.nama = nama;
        this.alamat = alamat;
        this.telepon = telepon;
        this.id = id;
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
