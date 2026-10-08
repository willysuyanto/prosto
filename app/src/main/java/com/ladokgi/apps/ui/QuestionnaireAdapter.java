package com.ladokgi.apps.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ladokgi.apps.CustomSeekbar;
import com.ladokgi.apps.R;
import com.ladokgi.apps.databinding.ItemQuestionnaireGroupBinding;
import com.ladokgi.apps.model.QuestionnaireData;

import java.util.List;

/** Binds one questionnaire group (question text + its seekbars) per row. */
public class QuestionnaireAdapter extends RecyclerView.Adapter<QuestionnaireAdapter.Holder> {

    private final List<QuestionnaireData.Group> groups;

    public QuestionnaireAdapter(List<QuestionnaireData.Group> groups) {
        this.groups = groups;
        setHasStableIds(true);
    }

    @Override
    public long getItemId(int position) {
        return groups.get(position).titleNumber;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemQuestionnaireGroupBinding binding = ItemQuestionnaireGroupBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new Holder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.bind(groups.get(position));
    }

    @Override
    public int getItemCount() {
        return groups.size();
    }

    static class Holder extends RecyclerView.ViewHolder {
        private final ItemQuestionnaireGroupBinding binding;

        Holder(ItemQuestionnaireGroupBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(QuestionnaireData.Group group) {
            binding.tvGroupTitle.setText(binding.getRoot().getContext()
                    .getString(R.string.format_question, group.titleNumber));
            binding.tvSubq.setText(group.subQuestionRes);
            binding.slidersContainer.removeAllViews();

            for (int i = 0; i < group.sliders.size(); i++) {
                final int sliderIndex = i;
                QuestionnaireData.SliderDef def = group.sliders.get(i);
                CustomSeekbar seekbar = new CustomSeekbar(binding.getRoot().getContext(),
                        QuestionnaireData.SCALE_STEPS, R.color.slider_label, def.left, def.right);
                seekbar.addSeekBar(binding.slidersContainer);
                SeekBar bar = seekbar.getSeekBar();
                if (bar != null) {
                    bar.setProgress(group.values[i] - 1);
                    bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                        @Override
                        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                            group.values[sliderIndex] = progress + 1;
                        }

                        @Override
                        public void onStartTrackingTouch(SeekBar seekBar) {
                        }

                        @Override
                        public void onStopTrackingTouch(SeekBar seekBar) {
                        }
                    });
                }
            }
        }
    }
}
