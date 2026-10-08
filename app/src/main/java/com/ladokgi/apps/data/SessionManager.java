package com.ladokgi.apps.data;

import android.content.Context;

import com.orhanobut.hawk.Hawk;

/** Thin wrapper around the local session store (Hawk preferences). */
public final class SessionManager {

    private static final String KEY_USERNAME = "username";
    private static final String KEY_ROLE = "role";

    private SessionManager() {
    }

    public static void init(Context context) {
        if (!Hawk.isBuilt()) {
            Hawk.init(context.getApplicationContext()).build();
        }
    }

    public static void setSession(String username, String role) {
        Hawk.put(KEY_USERNAME, username);
        Hawk.put(KEY_ROLE, role);
    }

    public static void setUser(com.google.firebase.firestore.DocumentSnapshot doc) {
        if (doc == null) return;
        String username = doc.getString(FirestoreKeys.USERNAME);
        if (username == null || username.isEmpty()) {
            username = doc.getId();
        }
        String role = doc.getString(FirestoreKeys.ROLE);
        setSession(username, role);
    }

    public static String getUsername() {
        return Hawk.get(KEY_USERNAME);
    }

    public static String getRole() {
        return Hawk.get(KEY_ROLE);
    }

    public static boolean isLoggedIn() {
        return getRole() != null;
    }

    public static boolean isDokter() {
        return "dokter".equals(getRole());
    }

    public static void clear() {
        Hawk.deleteAll();
    }
}
