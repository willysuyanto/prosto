package com.ladokgi.apps;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

public class CustomSeekbar {

    int maxCount, textColor;
    Context mContext;
    LinearLayout mSeekLabelAbove, mSeekLabelBelow;
    SeekBar mSeekBar;
    String response1, response2;

    public CustomSeekbar(Context context, int maxCount, int textColor, String response1, String response2) {
        this.mContext = context;
        this.maxCount = maxCount;
        this.textColor = textColor;
        this.response1 = response1;
        this.response2 = response2;
    }

    public SeekBar getSeekBar() {
        return mSeekBar;
    }

    public int getValue() {
        return getSeekBar().getProgress() + 1;
    }

    public void addSeekBar(LinearLayout parent) {

        if (parent instanceof LinearLayout) {

            parent.setOrientation(LinearLayout.VERTICAL);
            mSeekBar = new SeekBar(mContext);
            mSeekBar.setThumb(parent.getResources().getDrawable(R.drawable.ic_round_arrow_circle_up_24));
            mSeekBar.getThumb().setTint(parent.getResources().getColor(R.color.black));
            mSeekBar.setMax(maxCount - 1);
            mSeekBar.setProgress((int) Math.ceil(maxCount/2));

            // Add LinearLayout for labels above SeekBar
            mSeekLabelAbove = new LinearLayout(mContext);
            mSeekLabelAbove.setOrientation(LinearLayout.HORIZONTAL);
            mSeekLabelAbove.setPadding(5, 0, 5, 0);

            // Add LinearLayout for labels below SeekBar
            mSeekLabelBelow = new LinearLayout(mContext);
            mSeekLabelBelow.setOrientation(LinearLayout.HORIZONTAL);
            mSeekLabelBelow.setPadding(5, 0, 5, 0);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(35, 25, 35, 10);
            params2.setMargins(35, 0, 35, 25);
            mSeekLabelAbove.setLayoutParams(params);
            mSeekLabelBelow.setLayoutParams(params2);


            addLabelsAboveSeekBar();
            addLabelsBelowSeekBar(response1,response2);
            parent.addView(mSeekLabelAbove);
            parent.addView(mSeekBar);
            parent.addView(mSeekLabelBelow);

        } else {

            Log.e("CustomSeekBar", " Parent is not a LinearLayout");

        }

    }

    private void addLabelsAboveSeekBar() {
        for (int count = 0; count < maxCount; count++) {
            TextView textView = new TextView(mContext);
            textView.setText(String.valueOf(count + 1));
            textView.setTextColor(textColor);
            textView.setGravity(Gravity.LEFT);
            mSeekLabelAbove.addView(textView);
            textView.setLayoutParams((count == maxCount - 1) ? getLayoutParams(0.0f) : getLayoutParams(1.0f));
        }
    }

    private void addLabelsBelowSeekBar(String text1, String text2){
        TextView textView1 = new TextView(mContext);
        TextView textView2 = new TextView(mContext);
        textView1.setText(text1);
        textView2.setText(text2);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0,ViewGroup.LayoutParams.WRAP_CONTENT,1.0f);
        textView1.setLayoutParams(params);
        textView2.setLayoutParams(params);
        textView2.setGravity(Gravity.RIGHT);
        mSeekLabelBelow.addView(textView1);
        mSeekLabelBelow.addView(textView2);
    }

    LinearLayout.LayoutParams getLayoutParams(float weight) {
        return new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, weight);
    }

}