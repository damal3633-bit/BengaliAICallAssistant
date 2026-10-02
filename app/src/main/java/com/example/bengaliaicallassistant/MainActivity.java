package com.example.bengaliaicallassistant;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends Activity {

    private EditText trustedNumberInput;
    private LinearLayout trustedListContainer;
    private TextView trustedCountText;
    private TextView trustedStatCard;

    private android.content.SharedPreferences preferences;

    private static final String PREFS_NAME = "trusted_numbers";
    private static final String NUMBERS_KEY = "numbers";

    private int white = Color.WHITE;
    private int lightText = Color.rgb(210, 215, 235);
    private int blue = Color.rgb(40, 150, 255);
    private int purple = Color.rgb(180, 60, 255);
    private int green = Color.rgb(40, 230, 150);

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

        ScrollView scrollView = new ScrollView(this);
        scrollView.setBackgroundColor(Color.rgb(3, 5, 20));

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(20, 25, 20, 35);

        scrollView.addView(root);

        // =========================
        // HEADER
        // =========================

        TextView title = new TextView(this);
        title.setText("Bengali AI Call Assistant");
        title.setTextColor(white);
        title.setTextSize(27);
        title.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        title.setGravity(Gravity.CENTER);
        title.setPadding(0, 10, 0, 5);

        root.addView(title);

        TextView subtitle = new TextView(this);
        subtitle.setText("আপনার কল • আপনার নিয়ন্ত্রণ • AI সহায়তা");
        subtitle.setTextColor(Color.rgb(180, 190, 220));
        subtitle.setTextSize(14);
        subtitle.setGravity(Gravity.CENTER);
        subtitle.setPadding(0, 0, 0, 20);

        root.addView(subtitle);

        // =========================
        // AI MAIN CARD
        // =========================

        LinearLayout aiCard = createCard(
                Color.rgb(12, 20, 65),
                purple,
                blue
        );

        TextView aiIcon = new TextView(this);
        aiIcon.setText("🤖");
        aiIcon.setTextSize(55);
        aiIcon.setGravity(Gravity.CENTER);

        aiCard.addView(aiIcon);

        TextView aiTitle = new TextView(this);
        aiTitle.setText("AI Call Assistant");
        aiTitle.setTextColor(white);
        aiTitle.setTextSize(24);
        aiTitle.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        aiTitle.setGravity(Gravity.CENTER);

        aiCard.addView(aiTitle);

        TextView status = new TextView(this);
        status.setText("●  CALL SCREENING ON");
        status.setTextColor(green);
        status.setTextSize(16);
        status.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        status.setGravity(Gravity.CENTER);
        status.setPadding(0, 12, 0, 10);

        aiCard.addView(status);

        TextView aiInfo = new TextView(this);
        aiInfo.setText(
                "অপরিচিত কল শনাক্ত করে\n" +
                "AI handling system-এর জন্য প্রস্তুত করা হবে।"
        );
        aiInfo.setTextColor(lightText);
        aiInfo.setTextSize(15);
        aiInfo.setGravity(Gravity.CENTER);
        aiInfo.setPadding(0, 5, 0, 10);

        aiCard.addView(aiInfo);

        root.addView(aiCard);

        // =========================
        // STATISTICS
        // =========================

        LinearLayout stats = new LinearLayout(this);
        stats.setOrientation(LinearLayout.HORIZONTAL);
        stats.setGravity(Gravity.CENTER);
        stats.setPadding(0, 18, 0, 18);

        TextView totalCard = createStatCard(
                "📞",
                "Total Calls",
                "0"
        );

        trustedStatCard = createStatCard(
                "🛡",
                "Trusted",
                "0"
        );

        stats.addView(
                totalCard,
                new LinearLayout.LayoutParams(
                        0,
                        130,
                        1
                )
        );

        stats.addView(
                trustedStatCard,
                new LinearLayout.LayoutParams(
                        0,
                        130,
                        1
                )
        );

        root.addView(stats);

        // =========================
        // TRUSTED NUMBER CARD
        // =========================

        LinearLayout trustedCardBox = createCard(
                Color.rgb(10, 18, 55),
                blue,
                purple
        );

        TextView trustedTitle = new TextView(this);
        trustedTitle.setText("👥  Trusted Numbers");
        trustedTitle.setTextColor(white);
        trustedTitle.setTextSize(22);
        trustedTitle.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        trustedCardBox.addView(trustedTitle);

        TextView trustedInfo = new TextView(this);
        trustedInfo.setText(
                "যে নম্বরগুলো Trusted করা হবে,\n" +
                "সেগুলো AI screening-এর জন্য আলাদা করা হবে না।"
        );
        trustedInfo.setTextColor(lightText);
        trustedInfo.setTextSize(14);
        trustedInfo.setPadding(0, 8, 0, 15);

        trustedCardBox.addView(trustedInfo);

        trustedNumberInput = new EditText(this);
        trustedNumberInput.setHint("+919876543210");
        trustedNumberInput.setHintTextColor(Color.GRAY);
        trustedNumberInput.setTextColor(white);
        trustedNumberInput.setTextSize(16);
        trustedNumberInput.setInputType(
                InputType.TYPE_CLASS_PHONE
        );

        GradientDrawable inputBg = new GradientDrawable();
        inputBg.setColor(Color.rgb(8, 12, 35));
        inputBg.setCornerRadius(25);
        inputBg.setStroke(2, blue);

        trustedNumberInput.setBackground(inputBg);
        trustedNumberInput.setPadding(
                20,
                10,
                20,
                10
        );

        trustedCardBox.addView(
                trustedNumberInput,
                new LinearLayout.LayoutParams(
                        -1,
                        60
                )
        );

        Button addButton = new Button(this);
        addButton.setText("＋  ADD TRUSTED NUMBER");
        addButton.setTextColor(white);
        addButton.setTextSize(15);
        addButton.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        GradientDrawable addBg = new GradientDrawable(
                GradientDrawable.Orientation.LEFT_RIGHT,
                new int[]{
                        Color.rgb(0, 130, 255),
                        Color.rgb(190, 40, 255)
                }
        );

        addBg.setCornerRadius(30);
        addButton.setBackground(addBg);

        LinearLayout.LayoutParams addParams =
                new LinearLayout.LayoutParams(
                        -1,
                        58
                );

        addParams.setMargins(0, 15, 0, 5);

        trustedCardBox.addView(
                addButton,
                addParams
        );

        root.addView(trustedCardBox);

        // =========================
        // SAVED NUMBERS
        // =========================

        TextView savedTitle = new TextView(this);
        savedTitle.setText("⭐  Saved Trusted Numbers");
        savedTitle.setTextColor(white);
        savedTitle.setTextSize(21);
        savedTitle.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );
        savedTitle.setPadding(5, 25, 5, 12);

        root.addView(savedTitle);

        trustedCountText = new TextView(this);
        trustedCountText.setTextColor(
                Color.rgb(140, 160, 200)
        );
        trustedCountText.setTextSize(14);
        trustedCountText.setPadding(5, 0, 5, 8);

        root.addView(trustedCountText);

        trustedListContainer = new LinearLayout(this);
        trustedListContainer.setOrientation(
                LinearLayout.VERTICAL
        );

        root.addView(trustedListContainer);

        // =========================
        // FOOTER
        // =========================

        TextView footer = new TextView(this);

        footer.setText(
                "\n🎙  AI Call Assistant\n" +
                "বাংলায় স্মার্ট কল সহায়তা"
        );

        footer.setTextColor(
                Color.rgb(170, 180, 220)
        );

        footer.setTextSize(14);
        footer.setGravity(Gravity.CENTER);
        footer.setPadding(0, 25, 0, 10);

        root.addView(footer);

        addButton.setOnClickListener(
                v -> addTrustedNumber()
        );

        setContentView(scrollView);
    }

    // =========================
    // CARD
    // =========================

    private LinearLayout createCard(
            int background,
            int stroke1,
            int stroke2
    ) {

        LinearLayout card =
                new LinearLayout(this);

        card.setOrientation(
                LinearLayout.VERTICAL
        );

        card.setGravity(Gravity.CENTER);

        card.setPadding(
                18,
                20,
                18,
                20
        );

        GradientDrawable bg =
                new GradientDrawable();

        bg.setColor(background);
        bg.setCornerRadius(35);
        bg.setStroke(3, stroke1);

        card.setBackground(bg);

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(
                        -1,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        params.setMargins(0, 8, 0, 8);

        card.setLayoutParams(params);

        return card;
    }

    // =========================
    // STAT CARD
    // =========================

    private TextView createStatCard(
            String icon,
            String label,
            String value
    ) {

        TextView text = new TextView(this);

        text.setText(
                icon + "\n" +
                label + "\n" +
                value
        );

        text.setTextColor(white);
        text.setTextSize(15);
        text.setGravity(Gravity.CENTER);
        text.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        GradientDrawable bg =
                new GradientDrawable();

        bg.setColor(Color.rgb(10, 18, 50));
        bg.setCornerRadius(28);
        bg.setStroke(2, blue);

        text.setBackground(bg);

        return text;
    }

    // =========================
    // ADD NUMBER
    // =========================

    private void addTrustedNumber() {

        String number =
                trustedNumberInput
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
                .putStringSet(
                        NUMBERS_KEY,
                        currentNumbers
                )
                .apply();

        trustedNumberInput.setText("");

        refreshTrustedList();

        Toast.makeText(
                this,
                "✓ Trusted number added",
                Toast.LENGTH_SHORT
        ).show();
    }

    // =========================
    // REFRESH LIST
    // =========================

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

        trustedCountText.setText(
                numbers.size() +
                " Trusted number saved"
        );

        if (trustedStatCard != null) {
            trustedStatCard.setText(
                    "🛡\nTrusted\n" + numbers.size()
            );
        }

        if (numbers.isEmpty()) {

            TextView empty =
                    new TextView(this);

            empty.setText(
                    "এখনও কোনো Trusted Number যোগ করা হয়নি।"
            );

            empty.setTextColor(
                    Color.rgb(150, 160, 190)
            );

            empty.setTextSize(15);
            empty.setPadding(8, 10, 8, 10);

            trustedListContainer.addView(empty);

            return;
        }

        for (String number : numbers) {

            addNumberRow(number);
        }
    }

    // =========================
    // NUMBER ROW
    // =========================

    private void addNumberRow(
            String number
    ) {

        LinearLayout row =
                new LinearLayout(this);

        row.setOrientation(
                LinearLayout.HORIZONTAL
        );

        row.setGravity(
                Gravity.CENTER_VERTICAL
        );

        row.setPadding(
                15,
                12,
                10,
                12
        );

        GradientDrawable rowBg =
                new GradientDrawable();

        rowBg.setColor(
                Color.rgb(9, 15, 42)
        );

        rowBg.setCornerRadius(25);
        rowBg.setStroke(2, purple);

        row.setBackground(rowBg);

        TextView numberText =
                new TextView(this);

        numberText.setText(
                "🛡  " + number
        );

        numberText.setTextColor(white);
        numberText.setTextSize(16);

        LinearLayout.LayoutParams textParams =
                new LinearLayout.LayoutParams(
                        0,
                        -2,
                        1
                );

        row.addView(
                numberText,
                textParams
        );

        Button deleteButton =
                new Button(this);

        deleteButton.setText("DELETE");
        deleteButton.setTextSize(11);
        deleteButton.setTextColor(white);

        GradientDrawable deleteBg =
                new GradientDrawable();

        deleteBg.setColor(
                Color.rgb(210, 35, 100)
        );

        deleteBg.setCornerRadius(25);

        deleteButton.setBackground(deleteBg);

        deleteButton.setOnClickListener(
                v -> deleteTrustedNumber(number)
        );

        row.addView(
                deleteButton,
                new LinearLayout.LayoutParams(
                        105,
                        50
                )
        );

        LinearLayout.LayoutParams rowParams =
                new LinearLayout.LayoutParams(
                        -1,
                        -2
                );

        rowParams.setMargins(
                0,
                6,
                0,
                6
        );

        trustedListContainer.addView(
                row,
                rowParams
        );
    }

    // =========================
    // DELETE
    // =========================

    private void deleteTrustedNumber(
            String number
    ) {

        Set<String> currentNumbers =
                new HashSet<>(
                        preferences.getStringSet(
                                NUMBERS_KEY,
                                new HashSet<>()
                        )
                );

        currentNumbers.remove(number);

        preferences.edit()
                .putStringSet(
                        NUMBERS_KEY,
                        currentNumbers
                )
                .apply();

        refreshTrustedList();

        Toast.makeText(
                this,
                "Trusted number deleted",
                Toast.LENGTH_SHORT
        ).show();
    }

    // =========================
    // NORMALIZE
    // =========================

    private String normalizeNumber(
            String number
    ) {

        return number
                .replace(" ", "")
                .replace("-", "")
                .replace("(", "")
                .replace(")", "");
    }
}
