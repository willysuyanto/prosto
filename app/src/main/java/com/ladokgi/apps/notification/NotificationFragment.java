package com.ladokgi.apps.notification;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.ladokgi.apps.data.SessionManager;
import com.ladokgi.apps.databinding.FragmentNotificationBinding;

import java.util.ArrayList;
import java.util.List;

public class NotificationFragment extends Fragment {

    private FragmentNotificationBinding binding;
    private final List<Notification> list = new ArrayList<>();

    public NotificationFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentNotificationBinding.inflate(inflater, container, false);
        SessionManager.init(requireContext());

        NotificationAdapter adapter = new NotificationAdapter(list, getContext());
        binding.rvNotification.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rvNotification.setAdapter(adapter);

        FirebaseFirestore.getInstance()
                .collection("users")
                .document(SessionManager.getUsername())
                .collection("notifikasi")
                .orderBy("date", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful() || task.getResult() == null || binding == null) {
                        return;
                    }
                    for (com.google.firebase.firestore.DocumentSnapshot ds : task.getResult()) {
                        String title = ds.getString("title");
                        String message = ds.getString("message");
                        String date = ds.getString("date");
                        if (title != null && message != null && date != null) {
                            list.add(new Notification(title, message, date));
                        }
                    }
                    if (!list.isEmpty()) {
                        binding.rvNotification.setVisibility(View.VISIBLE);
                        binding.noData.setVisibility(View.GONE);
                        adapter.notifyDataSetChanged();
                    }
                });

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        binding = null;
        super.onDestroyView();
    }
}
