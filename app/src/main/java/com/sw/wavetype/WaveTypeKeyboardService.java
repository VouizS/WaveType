package com.sw.wavetype;

import android.inputmethodservice.InputMethodService;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.HapticFeedbackConstants;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputConnection;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class WaveTypeKeyboardService extends InputMethodService {
    private boolean caps = false;
    private boolean symbolsMode = false;
    private LinearLayout keyboardRoot;

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
        keyboardRoot = new LinearLayout(this);
        keyboardRoot.setOrientation(LinearLayout.VERTICAL);
        keyboardRoot.setPadding(dp(8), dp(7), dp(8), dp(8));
        keyboardRoot.setBackground(rounded(0x5A0B1018, 28, 0x55FFFFFF));

        renderKeyboard();
        return keyboardRoot;
    }

    private void renderKeyboard() {
        if (keyboardRoot == null) return;

        keyboardRoot.removeAllViews();
        addTopBar();

        if (symbolsMode) {
            addSymbolRow(new String[]{"1","2","3","4","5","6","7","8","9","0"}, 0);
            addSymbolRow(new String[]{"@","#","R$","&","*","(",")","-","+"}, 8);
            addSymbolRowWithActions();
            addSymbolBottomRow();
        } else {
            addLetterRow("qwertyuiop", 0);
            addLetterRow("asdfghjkl", 10);
            addThirdLetterRow();
            addLetterBottomRow();
        }
    }

    private void addTopBar() {
        LinearLayout top = new LinearLayout(this);
        top.setOrientation(LinearLayout.HORIZONTAL);
        top.setGravity(Gravity.CENTER_VERTICAL);
        top.setPadding(dp(6), 0, dp(6), dp(6));

        TextView brand = new TextView(this);
        brand.setText("WaveType");
        brand.setTextColor(Color.WHITE);
        brand.setTextSize(15);
        brand.setTypeface(Typeface.DEFAULT_BOLD);
        brand.setGravity(Gravity.CENTER_VERTICAL);

        LinearLayout.LayoutParams brandParams = new LinearLayout.LayoutParams(0, dp(38), 1f);
        top.addView(brand, brandParams);

        top.addView(toolKey("Mic"));
        top.addView(toolKey("Img"));
        top.addView(toolKey("Busca"));
        top.addView(toolKey("Tema"));

        keyboardRoot.addView(top);
    }

    private TextView toolKey(String label) {
        TextView key = baseKey(label, 12, 0x241E88E5, 0x33FFFFFF, true);
        key.setOnClickListener(v -> {
            v.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP);
            Toast.makeText(this, label + " entra nas próximas versões", Toast.LENGTH_SHORT).show();
        });

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(dp(54), dp(38));
        params.setMargins(dp(3), 0, dp(3), 0);
        key.setLayoutParams(params);
        return key;
    }

    private void addLetterRow(String letters, int sidePadding) {
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setGravity(Gravity.CENTER);
        row.setPadding(dp(sidePadding), 0, dp(sidePadding), 0);

        for (int i = 0; i < letters.length(); i++) {
            row.addView(letterKey(String.valueOf(letters.charAt(i))));
        }

        keyboardRoot.addView(row);
    }

    private void addThirdLetterRow() {
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setGravity(Gravity.CENTER);

        TextView shift = actionKey(caps ? "SHIFT" : "shift", 1.28f);
        shift.setOnClickListener(v -> {
            caps = !caps;
            v.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP);
            renderKeyboard();
        });
        row.addView(shift);

        String letters = "zxcvbnm";
        for (int i = 0; i < letters.length(); i++) {
            row.addView(letterKey(String.valueOf(letters.charAt(i))));
        }

        row.addView(backspaceKey(1.28f));
        keyboardRoot.addView(row);
    }

    private void addLetterBottomRow() {
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setGravity(Gravity.CENTER);

        TextView symbols = actionKey("123", 1.35f);
        symbols.setOnClickListener(v -> {
            symbolsMode = true;
            v.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP);
            renderKeyboard();
        });
        row.addView(symbols);

        row.addView(letterKey(","));

        TextView space = actionKey("Espaço", 4.25f);
        space.setTextSize(15);
        space.setOnClickListener(v -> {
            v.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP);
            commit(" ");
        });
        row.addView(space);

        row.addView(letterKey("."));

        row.addView(enterKey(1.35f, "Enter"));
        keyboardRoot.addView(row);
    }

    private void addSymbolRow(String[] labels, int sidePadding) {
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setGravity(Gravity.CENTER);
        row.setPadding(dp(sidePadding), 0, dp(sidePadding), 0);

        for (String label : labels) {
            row.addView(symbolKey(label));
        }

        keyboardRoot.addView(row);
    }

    private void addSymbolRowWithActions() {
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setGravity(Gravity.CENTER);

        TextView abc = actionKey("ABC", 1.28f);
        abc.setOnClickListener(v -> {
            symbolsMode = false;
            v.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP);
            renderKeyboard();
        });
        row.addView(abc);

        String[] labels = new String[]{"!","?",":",";","/","\\","'","\""};
        for (String label : labels) {
            row.addView(symbolKey(label));
        }

        row.addView(backspaceKey(1.28f));
        keyboardRoot.addView(row);
    }

    private void addSymbolBottomRow() {
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setGravity(Gravity.CENTER);

        String[] left = new String[]{"[","]","{","}"};
        for (String label : left) {
            row.addView(symbolKey(label));
        }

        TextView space = actionKey("Espaço", 3.2f);
        space.setTextSize(15);
        space.setOnClickListener(v -> {
            v.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP);
            commit(" ");
        });
        row.addView(space);

        String[] right = new String[]{"%","_","="};
        for (String label : right) {
            row.addView(symbolKey(label));
        }

        row.addView(enterKey(1.35f, "Enter"));
        keyboardRoot.addView(row);
    }

    private TextView letterKey(String label) {
        TextView key = baseKey(label, 18, 0x2F101821, 0x33FFFFFF, false);
        key.setOnClickListener(v -> {
            v.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP);
            String out = caps ? label.toUpperCase() : label;
            commit(out);
        });

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, dp(50), 1f);
        params.setMargins(dp(3), dp(4), dp(3), dp(4));
        key.setLayoutParams(params);
        return key;
    }

    private TextView symbolKey(String label) {
        TextView key = baseKey(label, label.length() > 1 ? 14 : 18, 0x2F101821, 0x33FFFFFF, false);
        key.setOnClickListener(v -> {
            v.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP);
            commit(label);
        });

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, dp(50), 1f);
        params.setMargins(dp(3), dp(4), dp(3), dp(4));
        key.setLayoutParams(params);
        return key;
    }

    private TextView actionKey(String label, float weight) {
        TextView key = baseKey(label, 14, 0x4A0D3A5A, 0x44FFFFFF, true);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, dp(50), weight);
        params.setMargins(dp(3), dp(4), dp(3), dp(4));
        key.setLayoutParams(params);
        return key;
    }

    private TextView backspaceKey(float weight) {
        TextView backspace = actionKey("⌫", weight);
        backspace.setTextSize(19);
        backspace.setOnClickListener(v -> {
            v.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP);
            InputConnection ic = getCurrentInputConnection();
            if (ic != null) ic.deleteSurroundingText(1, 0);
        });
        return backspace;
    }

    private TextView enterKey(float weight, String label) {
        TextView enter = actionKey(label, weight);
        enter.setTextSize(13);
        enter.setOnClickListener(v -> {
            v.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP);
            InputConnection ic = getCurrentInputConnection();
            if (ic != null) {
                ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
                ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_ENTER));
            }
        });
        return enter;
    }

    private TextView baseKey(String label, int textSize, int color, int stroke, boolean bold) {
        TextView key = new TextView(this);
        key.setText(label);
        key.setTextColor(Color.WHITE);
        key.setTextSize(textSize);
        key.setGravity(Gravity.CENTER);
        key.setSingleLine(true);
        key.setIncludeFontPadding(false);
        key.setBackground(rounded(color, 18, stroke));
        key.setClickable(true);
        key.setFocusable(true);
        key.setPadding(dp(2), 0, dp(2), 0);

        if (bold) {
            key.setTypeface(Typeface.DEFAULT_BOLD);
        }

        return key;
    }

    private void commit(String text) {
        InputConnection ic = getCurrentInputConnection();
        if (ic != null) ic.commitText(text, 1);
    }
}
