package com.sw.wavetype;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Gravity;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {
    private int dp(float value) {
        return (int) (value * getResources().getDisplayMetrics().density);
    }

    private GradientDrawable rounded(int color, float radius) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(color);
        drawable.setCornerRadius(dp(radius));
        drawable.setStroke(dp(1), 0x33FFFFFF);
        return drawable;
    }

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setGravity(Gravity.CENTER);
        root.setPadding(dp(24), dp(24), dp(24), dp(24));
        root.setBackgroundColor(Color.rgb(11, 15, 20));

        TextView title = new TextView(this);
        title.setText("WaveType");
        title.setTextColor(Color.WHITE);
        title.setTextSize(34);
        title.setGravity(Gravity.CENTER);

        TextView subtitle = new TextView(this);
        subtitle.setText("Liquid Glass Keyboard\nv0.1 Foundation");
        subtitle.setTextColor(0xCCFFFFFF);
        subtitle.setTextSize(16);
        subtitle.setGravity(Gravity.CENTER);
        subtitle.setPadding(0, dp(12), 0, dp(28));

        Button enable = new Button(this);
        enable.setText("Ativar WaveType nos teclados");
        enable.setTextColor(Color.WHITE);
        enable.setBackground(rounded(0x331E88E5, 24));
        enable.setAllCaps(false);
        enable.setOnClickListener(v -> {
            Intent intent = new Intent(Settings.ACTION_INPUT_METHOD_SETTINGS);
            startActivity(intent);
        });

        Button picker = new Button(this);
        picker.setText("Escolher teclado atual");
        picker.setTextColor(Color.WHITE);
        picker.setBackground(rounded(0x3326C6DA, 24));
        picker.setAllCaps(false);
        picker.setOnClickListener(v -> {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            if (imm != null) imm.showInputMethodPicker();
        });

        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                dp(56)
        );
        buttonParams.setMargins(0, dp(8), 0, dp(8));

        root.addView(title);
        root.addView(subtitle);
        root.addView(enable, buttonParams);
        root.addView(picker, buttonParams);

        setContentView(root);
    }
}
