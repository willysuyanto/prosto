package com.ladokgi.apps;

import android.provider.ContactsContract;

import java.util.List;

public class DataKonsultasi {
    private String namaPasien;
    private String tanggal;
    private boolean hasilPerilaku;
    private boolean hasilNonPerilaku;
    private String status;
    private String userName;
    private String idKonsultasi;

    public DataKonsultasi(String namaPasien, String tanggal, boolean hasilPerilaku, boolean hasilNonPerilaku, String status, String userName, String idKonsultasi) {
        this.namaPasien = namaPasien;
        this.tanggal = tanggal;
        this.hasilPerilaku = hasilPerilaku;
        this.hasilNonPerilaku = hasilNonPerilaku;
        this.status = status;
        this.userName = userName;
        this.idKonsultasi = idKonsultasi;
    }

    public String getIdKonsultasi() {
        return idKonsultasi;
    }

    public void setIdKonsultasi(String idKonsultasi) {
        this.idKonsultasi = idKonsultasi;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public DataKonsultasi(){}

    public String getNamaPasien() {
        return namaPasien;
    }

    public void setNamaPasien(String namaPasien) {
        this.namaPasien = namaPasien;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public boolean isHasilPerilaku() {
        return hasilPerilaku;
    }

    public void setHasilPerilaku(boolean hasilPerilaku) {
        this.hasilPerilaku = hasilPerilaku;
    }

    public boolean isHasilNonPerilaku() {
        return hasilNonPerilaku;
    }

    public void setHasilNonPerilaku(boolean hasilNonPerilaku) {
        this.hasilNonPerilaku = hasilNonPerilaku;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
