package com.example.bengaliaicallassistant;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends Activity {

    private EditText trustedNumberInput;
    private LinearLayout trustedListContainer;

    private android.content.SharedPreferences preferences;

    private static final String PREFS_NAME = "trusted_numbers";
    private static final String NUMBERS_KEY = "numbers";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferences = getSharedPreferences(
                PREFS_NAME,
                MODE_PRIVATE
        );

        createUI();
        refreshTrustedList();
    }

    private void createUI() {

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(25, 35, 25, 25);
        root.setBackgroundColor(Color.rgb(5, 10, 20));

        TextView title = new TextView(this);
        title.setText("Bengali AI Call Assistant");
        title.setTextColor(Color.WHITE);
        title.setTextSize(23);
        title.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        title.setGravity(Gravity.CENTER);

        root.addView(title);

        TextView trustedTitle = new TextView(this);
        trustedTitle.setText("\nTrusted Numbers");
        trustedTitle.setTextColor(Color.WHITE);
        trustedTitle.setTextSize(20);
        trustedTitle.setTypeface(Typeface.DEFAULT, Typeface.BOLD);

        root.addView(trustedTitle);

        TextView info = new TextView(this);
        info.setText(
                "Trusted number থেকে কল এলে AI screening-এর জন্য আলাদা করা হবে না।"
        );
        info.setTextColor(Color.LTGRAY);
        info.setTextSize(15);
        info.setPadding(0, 10, 0, 15);

        root.addView(info);

        trustedNumberInput = new EditText(this);
        trustedNumberInput.setHint("যেমন: +919876543210");
        trustedNumberInput.setHintTextColor(Color.GRAY);
        trustedNumberInput.setTextColor(Color.WHITE);
        trustedNumberInput.setInputType(
                InputType.TYPE_CLASS_PHONE
        );

        root.addView(trustedNumberInput);

        Button addButton = new Button(this);
        addButton.setText("ADD TRUSTED NUMBER");

        root.addView(addButton);

        TextView listTitle = new TextView(this);
        listTitle.setText("\nSaved Trusted Numbers:");
        listTitle.setTextColor(Color.WHITE);
        listTitle.setTextSize(17);
        listTitle.setTypeface(Typeface.DEFAULT, Typeface.BOLD);

        root.addView(listTitle);

        trustedListContainer = new LinearLayout(this);
        trustedListContainer.setOrientation(
                LinearLayout.VERTICAL
        );

        root.addView(trustedListContainer);

        addButton.setOnClickListener(v -> addTrustedNumber());

        setContentView(root);
    }

    private void addTrustedNumber() {

        String number = trustedNumberInput
                .getText()
                .toString()
                .trim();

        if (number.isEmpty()) {

            Toast.makeText(
                    this,
                    "একটি ফোন নম্বর দিন",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        number = normalizeNumber(number);

        Set<String> currentNumbers =
                new HashSet<>(
                        preferences.getStringSet(
                                NUMBERS_KEY,
                                new HashSet<>()
                        )
                );

        if (currentNumbers.contains(number)) {

            Toast.makeText(
                    this,
                    "এই নম্বরটি আগে থেকেই Trusted",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        currentNumbers.add(number);

        preferences.edit()
                .putStringSet(NUMBERS_KEY, currentNumbers)
                .apply();

        trustedNumberInput.setText("");

        refreshTrustedList();

        Toast.makeText(
                this,
                "Trusted number added",
                Toast.LENGTH_SHORT
        ).show();
    }

    private void refreshTrustedList() {

        trustedListContainer.removeAllViews();

        Set<String> saved =
                preferences.getStringSet(
                        NUMBERS_KEY,
                        new HashSet<>()
                );

        ArrayList<String> numbers =
                new ArrayList<>(saved);

        Collections.sort(numbers);

        if (numbers.isEmpty()) {

            TextView empty = new TextView(this);

            empty.setText("\nকোনো Trusted Number নেই।");
            empty.setTextColor(Color.LTGRAY);
            empty.setTextSize(15);

            trustedListContainer.addView(empty);

            return;
        }

        for (String number : numbers) {

            addNumberRow(number);
        }
    }

    private void addNumberRow(String number) {

        LinearLayout row = new LinearLayout(this);

        row.setOrientation(
                LinearLayout.HORIZONTAL
        );

        row.setGravity(Gravity.CENTER_VERTICAL);
        row.setPadding(0, 8, 0, 8);

        TextView numberText = new TextView(this);

        numberText.setText("✓ " + number);
        numberText.setTextColor(Color.WHITE);
        numberText.setTextSize(16);

        LinearLayout.LayoutParams textParams =
                new LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1
                );

        row.addView(
                numberText,
                textParams
        );

        Button deleteButton = new Button(this);

        deleteButton.setText("DELETE");

        deleteButton.setOnClickListener(
                v -> deleteTrustedNumber(number)
        );

        row.addView(deleteButton);

        trustedListContainer.addView(row);
    }

    private void deleteTrustedNumber(String number) {

        Set<String> currentNumbers =
                new HashSet<>(
                        preferences.getStringSet(
                                NUMBERS_KEY,
                                new HashSet<>()
                        )
                );

        currentNumbers.remove(number);

        preferences.edit()
                .putStringSet(NUMBERS_KEY, currentNumbers)
                .apply();

        refreshTrustedList();

        Toast.makeText(
                this,
                "Trusted number deleted",
                Toast.LENGTH_SHORT
        ).show();
    }

    private String normalizeNumber(String number) {

        return number
                .replace(" ", "")
                .replace("-", "")
                .replace("(", "")
                .replace(")", "");
    }
}
