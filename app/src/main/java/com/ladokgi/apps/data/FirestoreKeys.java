package com.ladokgi.apps.data;

/** Firestore collection / document field names used across the app. */
public final class FirestoreKeys {

    private FirestoreKeys() {
    }

    public static final String USERS = "users";
    public static final String KONSULTASI = "konsultasi";
    public static final String NOTIFIKASI = "notifikasi";

    public static final String ROLE = "role";
    public static final String ROLE_PASIEN = "pasien";
    public static final String ROLE_DOKTER = "dokter";

    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String PASSWORD_HASH = "passwordHash";
    public static final String PASSWORD_SALT = "passwordSalt";

    public static final String NAMA = "nama";
    public static final String ALAMAT = "alamat";
    public static final String TELEPON = "telepon";
    public static final String PENDIDIKAN = "pendidikan";
    public static final String LAMA_MENGGUNAKAN = "lama-menggunakan";
    public static final String JENIS_KELAMIN = "jenis-kelamin";

    public static final String TANGGAL_KONSULTASI = "tanggal-konsultasi";
    public static final String STATUS = "status";
    public static final String STATUS_PENDING = "pending";
    public static final String STATUS_CONFIRMED = "confirmed";
    public static final String STATUS_UNFINISHED = "unfinished";

    public static final String ASPEK_PERILAKU = "perilaku";
    public static final String ASPEK_NON_PERILAKU = "non-perilaku";
    public static final String HASIL = "hasil";
    public static final String TOTAL_BOBOT = "total-bobot";
    public static final String GAMBAR_GIGI = "gambar_gigi";

    public static final String NOTIF_TITLE = "title";
    public static final String NOTIF_MESSAGE = "message";
    public static final String NOTIF_DATE = "date";
}
