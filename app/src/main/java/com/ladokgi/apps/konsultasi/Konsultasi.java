package com.ladokgi.apps.konsultasi;

public class Konsultasi {
    private String tanggal;
    private boolean hasilPerilaku;
    private boolean hasilNonPerilaku;
    private String status;

    public Konsultasi(String tanggal, boolean hasilPerilaku, boolean hasilNonPerilaku, String status) {
        this.tanggal = tanggal;
        this.hasilPerilaku = hasilPerilaku;
        this.hasilNonPerilaku = hasilNonPerilaku;
        this.status = status;
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
