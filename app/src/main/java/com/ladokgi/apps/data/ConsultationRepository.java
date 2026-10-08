package com.ladokgi.apps.data;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.ladokgi.apps.konsultasi.DataKonsultasi;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

/** Reads/writes for consultations and notifications. */
public class ConsultationRepository {

    public interface ListCallback {
        void onItems(List<DataKonsultasi> items);

        void onError(Exception e);
    }

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final UserRepository users = new UserRepository();

    public CollectionReference consultationsOf(String username) {
        return db.collection(FirestoreKeys.USERS)
                .document(username)
                .collection(FirestoreKeys.KONSULTASI);
    }

    public CollectionReference notificationsOf(String username) {
        return db.collection(FirestoreKeys.USERS)
                .document(username)
                .collection(FirestoreKeys.NOTIFIKASI);
    }

    public void createConsultation(String username, String docId, Map<String, Object> data,
                                   ApiCallback.SuccessError callback) {
        consultationsOf(username).document(docId).set(data)
                .addOnCompleteListener(task -> complete(task.isSuccessful(), task.getException(), callback));
    }

    public void mergeConsultation(String username, String docId, Map<String, Object> data,
                                  ApiCallback.SuccessError callback) {
        consultationsOf(username).document(docId).set(data, SetOptions.merge())
                .addOnCompleteListener(task -> complete(task.isSuccessful(), task.getException(), callback));
    }

    public void updateConsultation(String username, String docId, Map<String, Object> data,
                                   ApiCallback.SuccessError callback) {
        consultationsOf(username).document(docId).update(data)
                .addOnCompleteListener(task -> complete(task.isSuccessful(), task.getException(), callback));
    }

    public Task<DocumentSnapshot> getConsultation(String username, String docId) {
        return consultationsOf(username).document(docId).get();
    }

    /** Marks a consultation as confirmed and notifies the patient. */
    public void confirmConsultation(String username, String docId, ApiCallback.SuccessError callback) {
        Map<String, Object> update = new HashMap<>();
        update.put(FirestoreKeys.STATUS, FirestoreKeys.STATUS_CONFIRMED);
        consultationsOf(username).document(docId).update(update)
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        callback.onFailure(task.getException());
                        return;
                    }
                    notify(username, "Pemberitahuan",
                            "Konsultasi anda telah selesai ditinjau oleh Dokter. Silahkan cek hasil konsultasi "
                                    + "anda pada Dashboard aplikasi.");
                    callback.onSuccess();
                });
    }

    public void notify(String username, String title, String message) {
        Map<String, Object> data = new HashMap<>();
        data.put(FirestoreKeys.NOTIF_TITLE, title);
        data.put(FirestoreKeys.NOTIF_MESSAGE, message);
        String date = new SimpleDateFormat("dd MMMM yyyy HH.mm", Locale.getDefault()).format(new Date());
        data.put(FirestoreKeys.NOTIF_DATE, date);
        notificationsOf(username).document(UUID.randomUUID().toString()).set(data);
    }

    public Task<QuerySnapshot> patientConsultations(String username) {
        return consultationsOf(username)
                .orderBy(FirestoreKeys.TANGGAL_KONSULTASI, Query.Direction.DESCENDING)
                .get();
    }

    public Task<QuerySnapshot> notifications(String username) {
        return notificationsOf(username)
                .orderBy(FirestoreKeys.NOTIF_DATE, Query.Direction.DESCENDING)
                .get();
    }

    /**
     * Aggregates consultations of every patient, filtered by status
     * ({@code pending} or {@code confirmed}) — used by the doctor tabs.
     */
    public void doctorConsultations(String status, ListCallback callback) {
        users.listPatients(patients -> {
            if (patients.isEmpty()) {
                callback.onItems(new ArrayList<>());
                return;
            }
            List<DataKonsultasi> aggregated = new ArrayList<>();
            final int[] remaining = {patients.size()};
            final boolean[] failed = {false};
            for (DocumentSnapshot patient : patients) {
                String username = patient.getString(FirestoreKeys.USERNAME);
                if (username == null) {
                    username = patient.getId();
                }
                final String patientName = patient.getString(FirestoreKeys.NAMA);
                final String patientUsername = username;
                consultationsOf(username)
                        .orderBy(FirestoreKeys.TANGGAL_KONSULTASI, Query.Direction.DESCENDING)
                        .get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful() && task.getResult() != null) {
                                for (DocumentSnapshot snap : task.getResult()) {
                                    String snapStatus = snap.getString(FirestoreKeys.STATUS);
                                    if (!status.equals(snapStatus)) {
                                        continue;
                                    }
                                    DataKonsultasi item = toDataKonsultasi(patientName, patientUsername, snap);
                                    aggregated.add(item);
                                }
                            } else {
                                failed[0] = true;
                            }
                            remaining[0]--;
                            if (remaining[0] == 0) {
                                if (failed[0]) {
                                    callback.onError(new IllegalStateException("Gagal memuat data"));
                                } else {
                                    callback.onItems(aggregated);
                                }
                            }
                        });
            }
        });
    }

    private DataKonsultasi toDataKonsultasi(String patientName, String username, DocumentSnapshot snap) {
        boolean hasilP = Boolean.TRUE.equals(snap.getBoolean(FirestoreKeys.ASPEK_PERILAKU + "." + FirestoreKeys.HASIL));
        boolean hasilNP = Boolean.TRUE.equals(snap.getBoolean(FirestoreKeys.ASPEK_NON_PERILAKU + "." + FirestoreKeys.HASIL));
        DataKonsultasi item = new DataKonsultasi(patientName,
                snap.getString(FirestoreKeys.TANGGAL_KONSULTASI),
                hasilP, hasilNP,
                snap.getString(FirestoreKeys.STATUS),
                username, snap.getId());
        Integer bobotP = snap.get(FirestoreKeys.ASPEK_PERILAKU + "." + FirestoreKeys.TOTAL_BOBOT, Integer.class);
        Integer bobotNP = snap.get(FirestoreKeys.ASPEK_NON_PERILAKU + "." + FirestoreKeys.TOTAL_BOBOT, Integer.class);
        if (bobotP != null) {
            item.setTotalBobotPerilaku(String.valueOf(bobotP));
        }
        if (bobotNP != null) {
            item.setTotalBobotNonPerilaku(String.valueOf(bobotNP));
        }
        return item;
    }

    private void complete(boolean ok, Exception e, ApiCallback.SuccessError callback) {
        if (ok) {
            callback.onSuccess();
        } else {
            callback.onFailure(e);
        }
    }
}
