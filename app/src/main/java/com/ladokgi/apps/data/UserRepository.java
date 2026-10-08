package com.ladokgi.apps.data;

import android.util.Log;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.ladokgi.apps.security.PasswordHasher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * All reads/writes against the {@code users} collection live here.
 * Passwords are stored as salted PBKDF2 hashes; legacy plaintext documents
 * are transparently upgraded on the first successful login.
 */
public class UserRepository {

    public interface LoginCallback {
        void onLoginSuccess(DocumentSnapshot user);

        void onLoginFailure(String message);
    }

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void login(String username, String password, LoginCallback callback) {
        db.collection(FirestoreKeys.USERS).document(username).get()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful() || task.getResult() == null) {
                        callback.onLoginFailure("Tidak dapat terhubung ke server");
                        return;
                    }
                    DocumentSnapshot doc = task.getResult();
                    if (!doc.exists()) {
                        callback.onLoginFailure("Username tidak ditemukan");
                        return;
                    }
                    String salt = doc.getString(FirestoreKeys.PASSWORD_SALT);
                    String hash = doc.getString(FirestoreKeys.PASSWORD_HASH);
                    if (salt != null && hash != null) {
                        if (PasswordHasher.matches(password, salt, hash)) {
                            callback.onLoginSuccess(doc);
                        } else {
                            callback.onLoginFailure("Password Salah");
                        }
                        return;
                    }
                    verifyLegacyAndUpgrade(doc, password, callback);
                });
    }

    private void verifyLegacyAndUpgrade(DocumentSnapshot doc, String password, LoginCallback callback) {
        Object legacy = doc.get(FirestoreKeys.PASSWORD);
        if (legacy == null || !password.equals(String.valueOf(legacy))) {
            callback.onLoginFailure("Password Salah");
            return;
        }
        String salt = PasswordHasher.newSalt();
        Map<String, Object> update = new HashMap<>();
        update.put(FirestoreKeys.PASSWORD_SALT, salt);
        update.put(FirestoreKeys.PASSWORD_HASH, PasswordHasher.hash(password, salt));
        update.put(FirestoreKeys.PASSWORD, FieldValue.delete());
        doc.getReference().update(update)
                .addOnFailureListener(e -> {
                    // Migration is best effort: a working login must not fail
                    // just because the upgrade write was rejected.
                });
        callback.onLoginSuccess(doc);
    }

    public void register(Map<String, Object> user, String username, String rawPassword,
                         ApiCallback.SuccessError callback) {
        String salt = PasswordHasher.newSalt();
        user.put(FirestoreKeys.PASSWORD_SALT, salt);
        user.put(FirestoreKeys.PASSWORD_HASH, PasswordHasher.hash(rawPassword, salt));
        user.remove(FirestoreKeys.PASSWORD);

        db.collection(FirestoreKeys.USERS).document(username).get().addOnCompleteListener(task -> {
            if (!task.isSuccessful() || task.getResult() == null) {
                callback.onFailure(task.getException());
                return;
            }
            if (task.getResult().exists()) {
                callback.onFailure(new IllegalStateException("exists"));
                return;
            }
            db.collection(FirestoreKeys.USERS).document(username).set(user)
                    .addOnCompleteListener(write -> {
                        if (write.isSuccessful()) {
                            callback.onSuccess();
                        } else {
                            callback.onFailure(write.getException());
                        }
                    });
        });
    }

    public void get(String username, ApiCallback.Value<DocumentSnapshot> callback) {
        db.collection(FirestoreKeys.USERS).document(username).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        callback.onValue(task.getResult());
                    }
                });
    }

    public ListenerRegistration observe(String username,
                                        com.google.firebase.firestore.EventListener<DocumentSnapshot> listener) {
        return db.collection(FirestoreKeys.USERS).document(username).addSnapshotListener(listener);
    }

    public void updateProfile(String username, Map<String, Object> data,
                              ApiCallback.SuccessError callback) {
        db.collection(FirestoreKeys.USERS).document(username).update(data)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess();
                    } else {
                        callback.onFailure(task.getException());
                    }
                });
    }

    public void resetPassword(String userId, String rawPassword, ApiCallback.SuccessError callback) {
        String salt = PasswordHasher.newSalt();
        Map<String, Object> data = new HashMap<>();
        data.put(FirestoreKeys.PASSWORD_SALT, salt);
        data.put(FirestoreKeys.PASSWORD_HASH, PasswordHasher.hash(rawPassword, salt));
        data.put(FirestoreKeys.PASSWORD, FieldValue.delete());
        updateProfile(userId, data, callback);
    }

    public void delete(String userId, ApiCallback.SuccessError callback) {
        db.collection(FirestoreKeys.USERS).document(userId).delete()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess();
                    } else {
                        callback.onFailure(task.getException());
                    }
                });
    }

    /**
     * Loads every patient account (doctor view), sorted by name.
     * The sort is done on the client on purpose: combining {@code where role == pasien}
     * with {@code order by nama} would require a composite index that the existing
     * production database does not have, which silently returned an empty list.
     */
    public void listPatients(ApiCallback.Value<List<DocumentSnapshot>> callback) {
        db.collection(FirestoreKeys.USERS)
                .whereEqualTo(FirestoreKeys.ROLE, FirestoreKeys.ROLE_PASIEN)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        List<DocumentSnapshot> patients =
                                new ArrayList<>(task.getResult().getDocuments());
                        Collections.sort(patients, (a, b) -> nameOf(a).compareToIgnoreCase(nameOf(b)));
                        callback.onValue(patients);
                    } else {
                        Log.e("UserRepository", "listPatients failed", task.getException());
                        callback.onValue(new ArrayList<>());
                    }
                });
    }

    private static String nameOf(DocumentSnapshot doc) {
        String nama = doc.getString(FirestoreKeys.NAMA);
        return nama != null ? nama : doc.getId();
    }
}
