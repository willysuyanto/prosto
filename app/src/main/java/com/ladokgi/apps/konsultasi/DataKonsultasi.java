package com.ladokgi.apps.konsultasi;

public class DataKonsultasi {
    private String namaPasien;
    private String tanggal;
    private boolean hasilPerilaku;
    private boolean hasilNonPerilaku;
    private String status;
    private String userName;
    private String idKonsultasi;
    private String totalBobotPerilaku, totalBobotNonPerilaku;

    public String getTotalBobotPerilaku() {
        return totalBobotPerilaku;
    }

    public void setTotalBobotPerilaku(String totalBobotPerilaku) {
        this.totalBobotPerilaku = totalBobotPerilaku;
    }

    public String getTotalBobotNonPerilaku() {
        return totalBobotNonPerilaku;
    }

    public void setTotalBobotNonPerilaku(String totalBobotNonPerilaku) {
        this.totalBobotNonPerilaku = totalBobotNonPerilaku;
    }


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
