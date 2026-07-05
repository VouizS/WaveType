package com.sw.wavetype;

import android.inputmethodservice.InputMethodService;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputConnection;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class WaveTypeKeyboardService extends InputMethodService {
    private boolean caps = false;

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

    @Override
    public View onCreateInputView() {
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(dp(8), dp(8), dp(8), dp(8));
        root.setBackground(rounded(0x6610151C, 26, 0x44FFFFFF));

        LinearLayout top = new LinearLayout(this);
        top.setOrientation(LinearLayout.HORIZONTAL);
        top.setGravity(Gravity.CENTER_VERTICAL);
        top.setPadding(dp(8), 0, dp(8), dp(8));

        TextView brand = new TextView(this);
        brand.setText("WaveType");
        brand.setTextColor(Color.WHITE);
        brand.setTextSize(15);
        brand.setTypeface(Typeface.DEFAULT_BOLD);

        LinearLayout.LayoutParams brandParams = new LinearLayout.LayoutParams(0, dp(42), 1f);
        top.addView(brand, brandParams);

        top.addView(toolButton("🎙"));
        top.addView(toolButton("▣"));
        top.addView(toolButton("⌕"));
        top.addView(toolButton("⚙"));

        root.addView(top);

        addLetterRow(root, "qwertyuiop");
        addLetterRow(root, "asdfghjkl");
        addThirdRow(root);
        addBottomRow(root);

        return root;
    }

    private Button toolButton(String label) {
        Button button = new Button(this);
        button.setText(label);
        button.setTextSize(16);
        button.setTextColor(Color.WHITE);
        button.setAllCaps(false);
        button.setBackground(rounded(0x221E88E5, 18, 0x33FFFFFF));
        button.setOnClickListener(v ->
                Toast.makeText(this, "Recurso preparado para próximas versões", Toast.LENGTH_SHORT).show()
        );

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(dp(44), dp(40));
        params.setMargins(dp(4), 0, dp(4), 0);
        button.setLayoutParams(params);
        return button;
    }

    private void addLetterRow(LinearLayout root, String letters) {
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setGravity(Gravity.CENTER);

        for (int i = 0; i < letters.length(); i++) {
            String key = String.valueOf(letters.charAt(i));
            row.addView(keyButton(key));
        }

        root.addView(row);
    }

    private void addThirdRow(LinearLayout root) {
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);

        Button shift = actionButton("⇧");
        shift.setOnClickListener(v -> {
            caps = !caps;
            Toast.makeText(this, caps ? "Maiúsculas ativadas" : "Maiúsculas desativadas", Toast.LENGTH_SHORT).show();
        });
        row.addView(shift);

        String letters = "zxcvbnm";
        for (int i = 0; i < letters.length(); i++) {
            row.addView(keyButton(String.valueOf(letters.charAt(i))));
        }

        Button backspace = actionButton("⌫");
        backspace.setOnClickListener(v -> {
            InputConnection ic = getCurrentInputConnection();
            if (ic != null) ic.deleteSurroundingText(1, 0);
        });
        row.addView(backspace);

        root.addView(row);
    }

    private void addBottomRow(LinearLayout root) {
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);

        Button symbols = actionButton("123");
        symbols.setOnClickListener(v -> Toast.makeText(this, "Símbolos entram na próxima versão", Toast.LENGTH_SHORT).show());
        row.addView(symbols);

        Button comma = keyButton(",");
        row.addView(comma);

        Button space = actionButton("Espaço");
        space.setOnClickListener(v -> commit(" "));
        LinearLayout.LayoutParams spaceParams = new LinearLayout.LayoutParams(0, dp(48), 4f);
        spaceParams.setMargins(dp(3), dp(4), dp(3), dp(4));
        space.setLayoutParams(spaceParams);
        row.addView(space);

        Button dot = keyButton(".");
        row.addView(dot);

        Button enter = actionButton("↵");
        enter.setOnClickListener(v -> {
            InputConnection ic = getCurrentInputConnection();
            if (ic != null) {
                ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
                ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_ENTER));
            }
        });
        row.addView(enter);

        root.addView(row);
    }

    private Button keyButton(String label) {
        Button button = new Button(this);
        button.setText(label);
        button.setTextSize(18);
        button.setTextColor(Color.WHITE);
        button.setAllCaps(false);
        button.setBackground(rounded(0x331A222D, 18, 0x33FFFFFF));
        button.setOnClickListener(v -> {
            String out = caps ? label.toUpperCase() : label;
            commit(out);
        });

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, dp(48), 1f);
        params.setMargins(dp(3), dp(4), dp(3), dp(4));
        button.setLayoutParams(params);
        return button;
    }

    private Button actionButton(String label) {
        Button button = new Button(this);
        button.setText(label);
        button.setTextSize(15);
        button.setTextColor(Color.WHITE);
        button.setAllCaps(false);
        button.setBackground(rounded(0x441E88E5, 18, 0x33FFFFFF));

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, dp(48), 1.35f);
        params.setMargins(dp(3), dp(4), dp(3), dp(4));
        button.setLayoutParams(params);
        return button;
    }

    private void commit(String text) {
        InputConnection ic = getCurrentInputConnection();
        if (ic != null) ic.commitText(text, 1);
    }
}
