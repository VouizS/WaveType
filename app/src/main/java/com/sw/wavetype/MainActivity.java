package com.sw.wavetype;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Gravity;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {
    private int dp(float value) {
        return (int) (value * getResources().getDisplayMetrics().density);
    }

    private GradientDrawable rounded(int color, float radius, int strokeColor) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(color);
        drawable.setCornerRadius(dp(radius));
        drawable.setStroke(dp(1), strokeColor);
        return drawable;
    }

    private TextView cardButton(String text, int color) {
        TextView view = new TextView(this);
        view.setText(text);
        view.setTextColor(Color.WHITE);
        view.setTextSize(15);
        view.setGravity(Gravity.CENTER);
        view.setTypeface(Typeface.DEFAULT_BOLD);
        view.setBackground(rounded(color, 24, 0x33FFFFFF));
        view.setClickable(true);
        view.setPadding(dp(12), 0, dp(12), 0);
        return view;
    }

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setGravity(Gravity.CENTER);
        root.setPadding(dp(24), dp(24), dp(24), dp(24));
        root.setBackgroundColor(Color.rgb(8, 11, 16));

        TextView title = new TextView(this);
        title.setText("WaveType");
        title.setTextColor(Color.WHITE);
        title.setTextSize(36);
        title.setTypeface(Typeface.DEFAULT_BOLD);
        title.setGravity(Gravity.CENTER);

        TextView subtitle = new TextView(this);
        subtitle.setText("Liquid Glass Keyboard\nv0.1.1 First Polish");
        subtitle.setTextColor(0xCCFFFFFF);
        subtitle.setTextSize(16);
        subtitle.setGravity(Gravity.CENTER);
        subtitle.setPadding(0, dp(12), 0, dp(28));

        TextView enable = cardButton("Ativar WaveType nos teclados", 0x332A7FFF);
        enable.setOnClickListener(v -> {
            Intent intent = new Intent(Settings.ACTION_INPUT_METHOD_SETTINGS);
            startActivity(intent);
        });

        TextView picker = cardButton("Escolher teclado atual", 0x3326C6DA);
        picker.setOnClickListener(v -> {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            if (imm != null) imm.showInputMethodPicker();
        });

        TextView info = new TextView(this);
        info.setText("Base aprovada: teclado funcional, glass escuro, teclas arredondadas e fluxo APK automático.");
        info.setTextColor(0x99FFFFFF);
        info.setTextSize(13);
        info.setGravity(Gravity.CENTER);
        info.setPadding(dp(8), dp(22), dp(8), 0);

        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                dp(56)
        );
        buttonParams.setMargins(0, dp(8), 0, dp(8));

        root.addView(title);
        root.addView(subtitle);
        root.addView(enable, buttonParams);
        root.addView(picker, buttonParams);
        root.addView(info);

        setContentView(root);
    }
}
