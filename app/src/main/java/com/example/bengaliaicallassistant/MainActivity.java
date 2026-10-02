package com.example.bengaliaicallassistant;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends Activity {

    EditText trustedNumberInput;
    LinearLayout trustedListContainer;
    TextView trustedCountText;
    TextView trustedStatCard;
    TextView trustedPill;
    TextView totalCallsCard;
    SharedPreferences preferences;

    static final String PREFS_NAME = "trusted_numbers";
    static final String NUMBERS_KEY = "numbers";
    static final String TOTAL_CALLS_KEY = "total_calls";

    static final int white = Color.WHITE;
    static final int lightText = Color.rgb(200, 210, 235);
    static final int dimText = Color.rgb(140, 155, 195);
    static final int blue = Color.rgb(60, 170, 255);
    static final int purple = Color.rgb(180, 80, 255);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        setContentView(buildUI());
        refreshTrustedList();
        refreshTotalCalls();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshTotalCalls();
    }

    void refreshTotalCalls() {
        if (totalCallsCard == null) return;
        int total = preferences.getInt(TOTAL_CALLS_KEY, 0);
        totalCallsCard.setText("📞\nTotal Calls\nScreened\n" + total);
    }

    View buildUI() {
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(Color.rgb(3, 5, 22));

        ScrollView sv = new ScrollView(this);
        sv.setFillViewport(true);
        LinearLayout content = new LinearLayout(this);
        content.setOrientation(LinearLayout.VERTICAL);
        content.setPadding(dp(20), dp(30), dp(20), dp(30));
        sv.addView(content);
        root.addView(sv, new LinearLayout.LayoutParams(-1, 0, 1));

        LinearLayout headerRow = new LinearLayout(this);
        headerRow.setOrientation(LinearLayout.HORIZONTAL);
        headerRow.setGravity(Gravity.CENTER_VERTICAL);
        headerRow.setPadding(dp(4), 0, dp(4), 0);

        TextView headerIcon = new TextView(this);
        headerIcon.setText("🤖");
        headerIcon.setTextSize(30);
        LinearLayout.LayoutParams hiP = new LinearLayout.LayoutParams(-2, -2);
        hiP.setMargins(0, 0, dp(12), 0);
        headerRow.addView(headerIcon, hiP);

        TextView title = new TextView(this);
        title.setText("Bengali AI Call Assistant");
        title.setTextColor(white);
        title.setTextSize(20);
        title.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        headerRow.addView(title, new LinearLayout.LayoutParams(0, -2, 1));

        TextView gear = new TextView(this);
        gear.setText("⚙");
        gear.setTextSize(24);
        gear.setTextColor(Color.rgb(120, 150, 220));
        headerRow.addView(gear);
        content.addView(headerRow);

        title.post(() -> {
            Shader sh = new LinearGradient(0, 0, title.getWidth(), 0,
                    new int[]{Color.rgb(80, 180, 255),
                            Color.rgb(180, 100, 255),
                            Color.rgb(255, 120, 200)},
                    null, Shader.TileMode.CLAMP);
            title.getPaint().setShader(sh);
            title.invalidate();
        });

        TextView subtitle = new TextView(this);
        subtitle.setText("অজানা কল, এখন আর চিন্তার কারণ নয়!");
        subtitle.setTextColor(dimText);
        subtitle.setTextSize(13);
        subtitle.setGravity(Gravity.CENTER);
        subtitle.setPadding(0, dp(6), 0, dp(22));
        content.addView(subtitle);

        LinearLayout heroCard = createCard(Color.rgb(15, 22, 70), purple);
        content.addView(heroCard);

        FrameLayout robotWrap = new FrameLayout(this);
        View robotGlow = new View(this);
        robotGlow.setBackground(makeGlowBg(
                new int[]{Color.rgb(70, 130, 255), Color.rgb(180, 80, 255)},
                Color.rgb(140, 100, 255), 60));
        robotWrap.addView(robotGlow,
                new FrameLayout.LayoutParams(dp(120), dp(120), Gravity.CENTER));
        TextView robotEmoji = new TextView(this);
        robotEmoji.setText("🤖");
        robotEmoji.setTextSize(58);
        robotEmoji.setGravity(Gravity.CENTER);
        robotWrap.addView(robotEmoji,
                new FrameLayout.LayoutParams(-2, -2, Gravity.CENTER));
        LinearLayout.LayoutParams rwP = new LinearLayout.LayoutParams(-2, -2);
        rwP.gravity = Gravity.CENTER_HORIZONTAL;
        rwP.setMargins(0, 0, 0, dp(8));
        heroCard.addView(robotWrap, rwP);

        TextView heroTitle = new TextView(this);
        heroTitle.setText("AI Call Assistant");
        heroTitle.setTextColor(white);
        heroTitle.setTextSize(23);
        heroTitle.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        heroTitle.setGravity(Gravity.CENTER);
        heroCard.addView(heroTitle);

        TextView statusPill = new TextView(this);
        statusPill.setText("✓   Call Screening ON");
        statusPill.setTextColor(white);
        statusPill.setTextSize(14);
        statusPill.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        statusPill.setGravity(Gravity.CENTER);
        statusPill.setBackground(makeGlowBg(
                new int[]{Color.rgb(20, 190, 110), Color.rgb(20, 150, 190)},
                Color.rgb(40, 230, 150), 24));
        statusPill.setPadding(dp(18), dp(9), dp(18), dp(9));
        LinearLayout.LayoutParams pP = new LinearLayout.LayoutParams(-2, -2);
        pP.gravity = Gravity.CENTER_HORIZONTAL;
        pP.setMargins(0, dp(12), 0, dp(12));
        heroCard.addView(statusPill, pP);

        TextView heroInfo = new TextView(this);
        heroInfo.setText("অজানা নম্বর থেকে আসা কলগুলো\n" +
                "আমি স্ক্রিন করে আপনার জন্য\n" +
                "সঠিক তথ্য জানাবো।");
        heroInfo.setTextColor(lightText);
        heroInfo.setTextSize(14);
        heroInfo.setGravity(Gravity.CENTER);
        heroInfo.setPadding(0, dp(4), 0, dp(4));
        heroCard.addView(heroInfo);

        LinearLayout stats = new LinearLayout(this);
        stats.setOrientation(LinearLayout.HORIZONTAL);
        stats.setGravity(Gravity.CENTER);
        stats.setPadding(0, dp(18), 0, dp(18));

        totalCallsCard = createStatCard("📞", "Total Calls\nScreened", "0",
                Color.rgb(30, 200, 130));
        trustedStatCard = createStatCard("🛡", "Trusted\nNumbers", "0",
                Color.rgb(60, 150, 255));
        TextView blockedCard = createStatCard("⛔", "Blocked\n(If Any)", "0",
                Color.rgb(170, 90, 255));

        LinearLayout.LayoutParams s1 = new LinearLayout.LayoutParams(0, -2, 1);
        s1.setMargins(0, 0, dp(6), 0);
        LinearLayout.LayoutParams s2 = new LinearLayout.LayoutParams(0, -2, 1);
        s2.setMargins(dp(3), 0, dp(3), 0);
        LinearLayout.LayoutParams s3 = new LinearLayout.LayoutParams(0, -2, 1);
        s3.setMargins(dp(6), 0, 0, 0);
        stats.addView(totalCallsCard, s1);
        stats.addView(trustedStatCard, s2);
        stats.addView(blockedCard, s3);
        content.addView(stats);

        LinearLayout trustedBox = createCard(Color.rgb(12, 20, 60), blue);
        content.addView(trustedBox);

        LinearLayout tHead = new LinearLayout(this);
        tHead.setOrientation(LinearLayout.HORIZONTAL);
        tHead.setGravity(Gravity.CENTER_VERTICAL);
        TextView tTitle = new TextView(this);
        tTitle.setText("👥   Trusted Numbers");
        tTitle.setTextColor(white);
        tTitle.setTextSize(19);
        tTitle.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tHead.addView(tTitle, new LinearLayout.LayoutParams(0, -2, 1));

        trustedPill = new TextView(this);
        trustedPill.setText("0 Trusted");
        trustedPill.setTextColor(white);
        trustedPill.setTextSize(12);
        trustedPill.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        trustedPill.setGravity(Gravity.CENTER);
        trustedPill.setBackground(makeGlowBg(
                new int[]{Color.rgb(20, 190, 110), Color.rgb(20, 150, 190)},
                Color.rgb(40, 230, 150), 20));
        trustedPill.setPadding(dp(14), dp(6), dp(14), dp(6));
        tHead.addView(trustedPill);
        trustedBox.addView(tHead);

        TextView tInfo = new TextView(this);
        tInfo.setText("বিশ্বস্ত নম্বরগুলো এখানে দিন");
        tInfo.setTextColor(dimText);
        tInfo.setTextSize(13);
        tInfo.setPadding(0, dp(6), 0, dp(14));
        trustedBox.addView(tInfo);

        trustedNumberInput = new EditText(this);
        trustedNumberInput.setHint("+919876543210");
        trustedNumberInput.setHintTextColor(Color.GRAY);
        trustedNumberInput.setTextColor(white);
        trustedNumberInput.setTextSize(15);
        trustedNumberInput.setInputType(InputType.TYPE_CLASS_PHONE);
        GradientDrawable inputBg = new GradientDrawable();
        inputBg.setColor(Color.rgb(8, 12, 38));
        inputBg.setCornerRadius(dp(22));
        inputBg.setStroke(dp(2), blue);
        trustedNumberInput.setBackground(inputBg);
        trustedNumberInput.setPadding(dp(18), dp(10), dp(18), dp(10));
        trustedBox.addView(trustedNumberInput,
                new LinearLayout.LayoutParams(-1, dp(56)));

        TextView addButton = new TextView(this);
        addButton.setText("＋   ADD TRUSTED NUMBER");
        addButton.setTextColor(white);
        addButton.setTextSize(14);
        addButton.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        addButton.setGravity(Gravity.CENTER);
        addButton.setClickable(true);
        addButton.setFocusable(true);
        addButton.setBackground(makeGlowBg(
                new int[]{Color.rgb(0, 130, 255), Color.rgb(190, 40, 255)},
                Color.rgb(140, 80, 255), 28));
        LinearLayout.LayoutParams addP = new LinearLayout.LayoutParams(-1, dp(72));
        addP.setMargins(0, dp(14), 0, dp(4));
        trustedBox.addView(addButton, addP);
        addButton.setOnClickListener(v -> addTrustedNumber());

        LinearLayout savedHead = new LinearLayout(this);
        savedHead.setOrientation(LinearLayout.HORIZONTAL);
        savedHead.setGravity(Gravity.CENTER_VERTICAL);
        savedHead.setPadding(dp(6), dp(22), dp(6), dp(10));
        TextView savedTitle = new TextView(this);
        savedTitle.setText("⭐   Saved Trusted Numbers");
        savedTitle.setTextColor(white);
        savedTitle.setTextSize(18);
        savedTitle.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        savedHead.addView(savedTitle, new LinearLayout.LayoutParams(0, -2, 1));
        content.addView(savedHead);

        trustedCountText = new TextView(this);
        trustedCountText.setTextColor(dimText);
        trustedCountText.setTextSize(13);
        trustedCountText.setPadding(dp(6), 0, dp(6), dp(8));
        content.addView(trustedCountText);

        trustedListContainer = new LinearLayout(this);
        trustedListContainer.setOrientation(LinearLayout.VERTICAL);
        content.addView(trustedListContainer);

        TextView footer = new TextView(this);
        footer.setText("\n🎙   AI Call Assistant\nবাংলায় স্মার্ট কল সহায়তা");
        footer.setTextColor(dimText);
        footer.setTextSize(13);
        footer.setGravity(Gravity.CENTER);
        footer.setPadding(0, dp(24), 0, dp(14));
        content.addView(footer);

        LinearLayout bottomNav = new LinearLayout(this);
        bottomNav.setOrientation(LinearLayout.HORIZONTAL);
        bottomNav.setGravity(Gravity.CENTER_VERTICAL);
        bottomNav.setPadding(dp(10), dp(10), dp(10), dp(14));
        GradientDrawable navBg = new GradientDrawable();
        navBg.setColor(Color.rgb(8, 10, 30));
        navBg.setStroke(dp(2), purple);
        navBg.setCornerRadii(new float[]{
                dp(30), dp(30), dp(30), dp(30), 0, 0, 0, 0});
        bottomNav.setBackground(navBg);

        LinearLayout homeWrap = new LinearLayout(this);
        homeWrap.setOrientation(LinearLayout.VERTICAL);
        homeWrap.setGravity(Gravity.CENTER);
        TextView homeIcon = new TextView(this);
        homeIcon.setText("🏠");
        homeIcon.setTextSize(22);
        homeIcon.setGravity(Gravity.CENTER);
        TextView homeLbl = new TextView(this);
        homeLbl.setText("Home");
        homeLbl.setTextColor(purple);
        homeLbl.setTextSize(12);
        homeLbl.setGravity(Gravity.CENTER);
        homeLbl.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        homeWrap.addView(homeIcon);
        homeWrap.addView(homeLbl);
        bottomNav.addView(homeWrap, new LinearLayout.LayoutParams(0, -2, 1));

        FrameLayout micWrap = new FrameLayout(this);
        micWrap.setPadding(dp(6), 0, dp(6), 0);
        View micGlow = new View(this);
        micGlow.setBackground(makeGlowBg(
                new int[]{Color.rgb(120, 60, 255), Color.rgb(200, 40, 200)},
                Color.rgb(180, 80, 255), 40));
        micWrap.addView(micGlow,
                new FrameLayout.LayoutParams(dp(68), dp(68), Gravity.CENTER));
        TextView micIcon = new TextView(this);
        micIcon.setText("🎤");
        micIcon.setTextSize(26);
        micIcon.setGravity(Gravity.CENTER);
        micWrap.addView(micIcon,
                new FrameLayout.LayoutParams(-2, -2, Gravity.CENTER));
        bottomNav.addView(micWrap, new LinearLayout.LayoutParams(-2, -2));

        LinearLayout hWrap = new LinearLayout(this);
        hWrap.setOrientation(LinearLayout.VERTICAL);
        hWrap.setGravity(Gravity.CENTER);
        TextView hIcon = new TextView(this);
        hIcon.setText("⇄");
        hIcon.setTextSize(24);
        hIcon.setTextColor(white);
        hIcon.setGravity(Gravity.CENTER);
        TextView hLbl = new TextView(this);
        hLbl.setText("Handover");
        hLbl.setTextColor(dimText);
        hLbl.setTextSize(12);
        hLbl.setGravity(Gravity.CENTER);
        hWrap.addView(hIcon);
        hWrap.addView(hLbl);
        bottomNav.addView(hWrap, new LinearLayout.LayoutParams(0, -2, 1));

        LinearLayout.LayoutParams navP = new LinearLayout.LayoutParams(-1, -2);
        navP.setMargins(dp(10), 0, dp(10), dp(10));
        root.addView(bottomNav, navP);

        return root;
    }

    void addTrustedNumber() {
        String number = trustedNumberInput.getText().toString().trim();
        if (number.isEmpty()) {
            Toast.makeText(this, "একটি ফোন নম্বর দিন", Toast.LENGTH_SHORT).show();
            return;
        }
        number = normalizeNumber(number);
        Set<String> currentNumbers = new HashSet<>(
                preferences.getStringSet(NUMBERS_KEY, new HashSet<>()));
        if (currentNumbers.contains(number)) {
            Toast.makeText(this, "এই নম্বরটি আগে থেকেই Trusted", Toast.LENGTH_SHORT).show();
            return;
        }
        currentNumbers.add(number);
        preferences.edit().putStringSet(NUMBERS_KEY, currentNumbers).apply();
        trustedNumberInput.setText("");
        refreshTrustedList();
        Toast.makeText(this, "✓ Trusted number added", Toast.LENGTH_SHORT).show();
    }

    void refreshTrustedList() {
        trustedListContainer.removeAllViews();
        Set<String> saved = preferences.getStringSet(NUMBERS_KEY, new HashSet<>());
        ArrayList<String> numbers = new ArrayList<>(saved);
        Collections.sort(numbers);
        trustedCountText.setText(numbers.size() + " Trusted number saved");
        if (trustedStatCard != null) {
            trustedStatCard.setText("🛡\nTrusted\nNumbers\n" + numbers.size());
        }
        if (trustedPill != null) {
            trustedPill.setText(numbers.size() + " Trusted");
        }
        if (numbers.isEmpty()) {
            TextView empty = new TextView(this);
            empty.setText("এখনও কোনো Trusted Number যোগ করা হয়নি।");
            empty.setTextColor(dimText);
            empty.setTextSize(14);
            empty.setPadding(dp(8), dp(10), dp(8), dp(10));
            trustedListContainer.addView(empty);
            return;
        }
        for (String number : numbers) {
            addNumberRow(number);
        }
    }

    void addNumberRow(String number) {
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setGravity(Gravity.CENTER_VERTICAL);
        row.setPadding(dp(14), dp(10), dp(10), dp(10));
        GradientDrawable rowBg = new GradientDrawable();
        rowBg.setColor(Color.rgb(10, 16, 48));
        rowBg.setCornerRadius(dp(22));
        rowBg.setStroke(dp(2), purple);
        row.setBackground(rowBg);

        TextView numText = new TextView(this);
        numText.setText("👤  " + number);
        numText.setTextColor(white);
        numText.setTextSize(15);
        row.addView(numText, new LinearLayout.LayoutParams(0, -2, 1));

        TextView delBtn = new TextView(this);
        delBtn.setText("🗑");
        delBtn.setTextSize(18);
        delBtn.setTextColor(white);
        delBtn.setGravity(Gravity.CENTER);
        delBtn.setClickable(true);
        delBtn.setFocusable(true);
        delBtn.setBackground(makeGlowBg(
                new int[]{Color.rgb(235, 45, 110), Color.rgb(160, 20, 90)},
                Color.rgb(255, 60, 120), 18));
        delBtn.setOnClickListener(v -> deleteTrustedNumber(number));

        LinearLayout.LayoutParams dP = new LinearLayout.LayoutParams(dp(52), dp(52));
        dP.setMargins(dp(6), 0, 0, 0);
        row.addView(delBtn, dP);

        LinearLayout.LayoutParams rP = new LinearLayout.LayoutParams(-1, -2);
        rP.setMargins(0, dp(6), 0, dp(6));
        trustedListContainer.addView(row, rP);
    }

    void deleteTrustedNumber(String number) {
        Set<String> currentNumbers = new HashSet<>(
                preferences.getStringSet(NUMBERS_KEY, new HashSet<>()));
        currentNumbers.remove(number);
        preferences.edit().putStringSet(NUMBERS_KEY, currentNumbers).apply();
        refreshTrustedList();
        Toast.makeText(this, "Trusted number deleted", Toast.LENGTH_SHORT).show();
    }

    Drawable makeGlowBg(int[] gradientColors, int glowColor, float radiusDp) {
        float radius = radiusDp * getResources().getDisplayMetrics().density;
        int[] glowAlphas = { 25, 55, 100 };
        Drawable[] layers = new Drawable[4];
        for (int i = 0; i < 3; i++) {
            GradientDrawable g = new GradientDrawable();
            g.setShape(GradientDrawable.RECTANGLE);
            g.setCornerRadius(radius);
            g.setColor((glowColor & 0x00FFFFFF) | (glowAlphas[i] << 24));
            layers[i] = g;
        }
        GradientDrawable main = new GradientDrawable(
                GradientDrawable.Orientation.LEFT_RIGHT, gradientColors);
        main.setShape(GradientDrawable.RECTANGLE);
        main.setCornerRadius(radius);
        layers[3] = main;
        LayerDrawable ld = new LayerDrawable(layers);
        int step = dp(2);
        ld.setLayerInset(1, step, step, step, step);
        ld.setLayerInset(2, step * 2, step * 2, step * 2, step * 2);
        ld.setLayerInset(3, step * 3, step * 3, step * 3, step * 3);
        return ld;
    }

    int dp(int v) {
        return (int) (v * getResources().getDisplayMetrics().density);
    }

    LinearLayout cr
        LinearLayout createCard(int background, int stroke1) {
        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setGravity(Gravity.CENTER_HORIZONTAL);
        card.setPadding(dp(18), dp(22), dp(18), dp(22));
        GradientDrawable bg = new GradientDrawable();
        bg.setColor(background);
        bg.setCornerRadius(dp(28));
        bg.setStroke(dp(2), stroke1);
        card.setBackground(bg);
        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(-1,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, dp(8), 0, dp(8));
        card.setLayoutParams(params);
        return card;
    }

    TextView createStatCard(String icon, String label,
                            String value, int accentColor) {
        TextView text = new TextView(this);
        text.setText(icon + "\n" + label + "\n" + value);
        text.setTextColor(white);
        text.setTextSize(13);
        text.setGravity(Gravity.CENTER);
        text.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        text.setPadding(dp(6), dp(14), dp(6), dp(14));
        text.setLineSpacing(dp(2), 1f);
        GradientDrawable bg = new GradientDrawable();
        bg.setColor(Color.rgb(10, 16, 48));
        bg.setCornerRadius(dp(22));
        bg.setStroke(dp(2), accentColor);
        text.setBackground(bg);
        return text;
    }

    private String normalizeNumber(String number) {
        return number.replace(" ", "").replace("-", "")
                .replace("(", "").replace(")", "");
    }
}
