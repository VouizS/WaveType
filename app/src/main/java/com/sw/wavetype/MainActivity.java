package com.sw.wavetype;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Gravity;
import android.view.inputmethod.InputMethodInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends Activity {
    private static final String PREFS_NAME = "wavetype_settings";
    private static final String KEY_HEIGHT = "height_mode";
    private static final String KEY_TRANSPARENCY = "transparency_mode";

    private static final String HEIGHT_LOW = "LOW";
    private static final String HEIGHT_MEDIUM = "MEDIUM";
    private static final String HEIGHT_HIGH = "HIGH";

    private static final String TRANS_LOW = "LOW";
    private static final String TRANS_MEDIUM = "MEDIUM";
    private static final String TRANS_HIGH = "HIGH";

    private TextView statusView;
    private TextView hintView;
    private TextView heightButton;
    private TextView transparencyButton;
    private SharedPreferences prefs;

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

    private TextView smallCard(String text) {
        TextView view = new TextView(this);
        view.setText(text);
        view.setTextColor(Color.WHITE);
        view.setTextSize(14);
        view.setGravity(Gravity.CENTER);
        view.setTypeface(Typeface.DEFAULT_BOLD);
        view.setBackground(rounded(0x251E88E5, 22, 0x33FFFFFF));
        view.setClickable(true);
        view.setPadding(dp(10), 0, dp(10), 0);
        return view;
    }

    private boolean isWaveTypeEnabled() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (imm == null) return false;

        List<InputMethodInfo> enabledMethods = imm.getEnabledInputMethodList();
        String packageName = getPackageName();

        for (InputMethodInfo info : enabledMethods) {
            if (info != null && packageName.equals(info.getPackageName())) {
                return true;
            }
        }

        return false;
    }

    private String heightLabel(String value) {
        if (HEIGHT_LOW.equals(value)) return "Altura: Baixo";
        if (HEIGHT_HIGH.equals(value)) return "Altura: Alto";
        return "Altura: Médio";
    }

    private String transparencyLabel(String value) {
        if (TRANS_LOW.equals(value)) return "Transparência: Baixa";
        if (TRANS_HIGH.equals(value)) return "Transparência: Alta";
        return "Transparência: Média";
    }

    private String nextHeight(String value) {
        if (HEIGHT_LOW.equals(value)) return HEIGHT_MEDIUM;
        if (HEIGHT_MEDIUM.equals(value)) return HEIGHT_HIGH;
        return HEIGHT_LOW;
    }

    private String nextTransparency(String value) {
        if (TRANS_LOW.equals(value)) return TRANS_MEDIUM;
        if (TRANS_MEDIUM.equals(value)) return TRANS_HIGH;
        return TRANS_LOW;
    }

    private void updateSettingsLabels() {
        String height = prefs.getString(KEY_HEIGHT, HEIGHT_MEDIUM);
        String transparency = prefs.getString(KEY_TRANSPARENCY, TRANS_MEDIUM);

        if (heightButton != null) {
            heightButton.setText(heightLabel(height));
        }

        if (transparencyButton != null) {
            transparencyButton.setText(transparencyLabel(transparency));
        }
    }

    private void updateStatus() {
        boolean enabled = isWaveTypeEnabled();

        if (statusView != null) {
            if (enabled) {
                statusView.setText("Status: WaveType ativado no sistema");
                statusView.setTextColor(0xFF8DE6C8);
            } else {
                statusView.setText("Status: WaveType ainda não ativado");
                statusView.setTextColor(0xFFFFD58A);
            }
        }

        if (hintView != null) {
            if (enabled) {
                hintView.setText("Personalize abaixo e toque em “Escolher teclado atual” para testar.");
            } else {
                hintView.setText("Passo 1: toque em “Ativar” e habilite o WaveType nas configurações do Android.");
            }
        }

        updateSettingsLabels();
    }

    private void openInputSettings() {
        Intent intent = new Intent(Settings.ACTION_INPUT_METHOD_SETTINGS);
        startActivity(intent);
    }

    private void openInputPickerOrSettings() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        if (!isWaveTypeEnabled()) {
            Toast.makeText(
                    this,
                    "Ative o WaveType primeiro nas configurações de teclado.",
                    Toast.LENGTH_LONG
            ).show();
            openInputSettings();
            return;
        }

        if (imm != null) {
            imm.showInputMethodPicker();
        } else {
            Toast.makeText(this, "Não foi possível abrir o seletor de teclado.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

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
        subtitle.setText("Liquid Glass Keyboard\nv0.1.3 Height & Transparency");
        subtitle.setTextColor(0xCCFFFFFF);
        subtitle.setTextSize(16);
        subtitle.setGravity(Gravity.CENTER);
        subtitle.setPadding(0, dp(12), 0, dp(20));

        statusView = new TextView(this);
        statusView.setTextSize(14);
        statusView.setTypeface(Typeface.DEFAULT_BOLD);
        statusView.setGravity(Gravity.CENTER);
        statusView.setPadding(dp(8), 0, dp(8), dp(14));

        TextView enable = cardButton("1. Ativar WaveType nos teclados", 0x332A7FFF);
        enable.setOnClickListener(v -> openInputSettings());

        TextView picker = cardButton("2. Escolher teclado atual", 0x3326C6DA);
        picker.setOnClickListener(v -> openInputPickerOrSettings());

        TextView section = new TextView(this);
        section.setText("Personalização");
        section.setTextColor(0xDDFFFFFF);
        section.setTextSize(16);
        section.setTypeface(Typeface.DEFAULT_BOLD);
        section.setGravity(Gravity.CENTER);
        section.setPadding(0, dp(22), 0, dp(8));

        heightButton = smallCard("Altura: Médio");
        heightButton.setOnClickListener(v -> {
            String current = prefs.getString(KEY_HEIGHT, HEIGHT_MEDIUM);
            String next = nextHeight(current);
            prefs.edit().putString(KEY_HEIGHT, next).apply();
            updateSettingsLabels();
            Toast.makeText(this, heightLabel(next) + " salva", Toast.LENGTH_SHORT).show();
        });

        transparencyButton = smallCard("Transparência: Média");
        transparencyButton.setOnClickListener(v -> {
            String current = prefs.getString(KEY_TRANSPARENCY, TRANS_MEDIUM);
            String next = nextTransparency(current);
            prefs.edit().putString(KEY_TRANSPARENCY, next).apply();
            updateSettingsLabels();
            Toast.makeText(this, transparencyLabel(next) + " salva", Toast.LENGTH_SHORT).show();
        });

        hintView = new TextView(this);
        hintView.setTextColor(0x99FFFFFF);
        hintView.setTextSize(13);
        hintView.setGravity(Gravity.CENTER);
        hintView.setPadding(dp(8), dp(18), dp(8), 0);

        TextView info = new TextView(this);
        info.setText("Base atual: ABC, números, símbolos, altura e transparência salvas.");
        info.setTextColor(0x77FFFFFF);
        info.setTextSize(12);
        info.setGravity(Gravity.CENTER);
        info.setPadding(dp(8), dp(12), dp(8), 0);

        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                dp(56)
        );
        buttonParams.setMargins(0, dp(8), 0, dp(8));

        LinearLayout.LayoutParams smallParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                dp(52)
        );
        smallParams.setMargins(0, dp(6), 0, dp(6));

        root.addView(title);
        root.addView(subtitle);
        root.addView(statusView);
        root.addView(enable, buttonParams);
        root.addView(picker, buttonParams);
        root.addView(section);
        root.addView(heightButton, smallParams);
        root.addView(transparencyButton, smallParams);
        root.addView(hintView);
        root.addView(info);

        setContentView(root);
        updateStatus();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateStatus();
    }
}
