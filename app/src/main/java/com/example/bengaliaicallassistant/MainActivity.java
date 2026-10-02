package com.example.bengaliaicallassistant;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
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

    private EditText trustedNumberInput;
    private LinearLayout trustedListContainer;
    private TextView trustedCountText;
    private TextView trustedStatCard;
    private TextView trustedPill;
    private TextView totalCallsCard;

    private SharedPreferences preferences;

    private static final String PREFS_NAME = "trusted_numbers";
    private static final String NUMBERS_KEY = "numbers";
    private static final String TOTAL_CALLS_KEY = "total_calls";

    private int white = Color.WHITE;
    private int lightText = Color.rgb(200, 210, 235);
    private int dimText = Color.rgb(140, 155, 195);
    private int blue = Color.rgb(60, 170, 255);
    private int purple = Color.rgb(180, 80, 255);
    private int green = Color.rgb(40, 230, 150);
    private int darkBg = Color.rgb(3, 5, 22);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferences = getSharedPreferences(
                PREFS_NAME,
                MODE_PRIVATE
        );

        createUI();
        refreshTrustedList();
        refreshTotalCalls();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshTotalCalls();
    }

    private void refreshTotalCalls() {

        if (totalCallsCard == null) {
            return;
        }

        int total = preferences.getInt(
                TOTAL_CALLS_KEY,
                0
        );

        totalCallsCard.setText(
                "📞\nTotal Calls\nScreened\n" + total
        );
    }

    private void createUI() {

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(darkBg);

        ScrollView scrollView = new ScrollView(this);
        scrollView.setFillViewport(true);

        LinearLayout content = new LinearLayout(this);
        content.setOrientation(LinearLayout.VERTICAL);
        content.setPadding(dp(20), dp(30), dp(20), dp(30));

        scrollView.addView(content);

        root.addView(
                scrollView,
                new LinearLayout.LayoutParams(-1, 0, 1)
        );

        // ===== HEADER =====

        LinearLayout headerRow = new LinearLayout(this);
        headerRow.setOrientation(LinearLayout.HORIZONTAL);
        headerRow.setGravity(Gravity.CENTER_VERTICAL);
        headerRow.setPadding(dp(4), 0, dp(4), 0);

        TextView headerIcon = new TextView(this);
        headerIcon.setText("🤖");
        headerIcon.setTextSize(30);

        LinearLayout.LayoutParams hiParams =
                new LinearLayout.LayoutParams(-2, -2);
        hiParams.setMargins(0, 0, dp(12), 0);
        headerRow.addView(headerIcon, hiParams);

        TextView title = new TextView(this);
        title.setText("Bengali AI Call Assistant");
        title.setTextColor(white);
        title.setTextSize(20);
        title.setTypeface(Typeface.DEFAULT, Typeface.BOLD);

        LinearLayout.LayoutParams titleParams =
                new LinearLayout.LayoutParams(0, -2, 1);
        headerRow.addView(title, titleParams);

        TextView gear = new TextView(this);
        gear.setText("⚙");
        gear.setTextSize(24);
        gear.setTextColor(Color.rgb(120, 150, 220));

        headerRow.addView(gear);

        content.addView(headerRow);

        title.post(() -> {
            Shader shader = new LinearGradient(
                    0, 0, title.getWidth(), 0,
                    new int[]{
                            Color.rgb(80, 180, 255),
                            Color.rgb(180, 100, 255),
                            Color.rgb(255, 120, 200)
                    },
                    null,
                    Shader.TileMode.CLAMP
            );
            title.getPaint().setShader(shader);
            title.invalidate();
        });

        TextView subtitle = new TextView(this);
        subtitle.setText("অজানা কল, এখন আর চিন্তার কারণ নয়!");
        subtitle.setTextColor(dimText);
        subtitle.setTextSize(13);
        subtitle.setGravity(Gravity.CENTER);
        subtitle.setPadding(0, dp(6), 0, dp(22));

        content.addView(subtitle);

        // ===== HERO CARD =====

        LinearLayout heroCard = createCard(
                Color.rgb(15, 22, 70),
                purple
        );

        FrameLayout robotWrap = new FrameLayout(this);

        View robotGlow = new View(this);
        robotGlow.setBackground(makeGlowBg(
                new int[]{
                        Color.rgb(70, 130, 255),
                        Color.rgb(180, 80, 255)
                },
                Color.rgb(140, 100, 255),
                60
        ));

        FrameLayout.LayoutParams glowParams =
                new FrameLayout.LayoutParams(
                        dp(120), dp(120),
                        Gravity.CENTER
                );
        robotWrap.addView(robotGlow, glowParams);

        TextView robotEmoji = new TextView(this);
        robotEmoji.setText("🤖");
        robotEmoji.setTextSize(58);
        robotEmoji.setGravity(Gravity.CENTER);

        FrameLayout.LayoutParams emojiParams =
                new FrameLayout.LayoutParams(
                        -2, -2,
                        Gravity.CENTER
                );
        robotWrap.addView(robotEmoji, emojiParams);

        content.addView(heroCard);

        LinearLayout.LayoutParams rwParams =
                new LinearLayout.LayoutParams(-2, -2);
        rwParams.gravity = Gravity.CENTER_HORIZONTAL;
        rwParams.setMargins(0, 0, 0, dp(8));
        heroCard.addView(robotWrap, rwParams);

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
                new int[]{
                        Color.rgb(20, 190, 110),
                        Color.rgb(20, 150, 190)
                },
                Color.rgb(40, 230, 150),
                24
        ));
        statusPill.setPadding(dp(18), dp(9), dp(18), dp(9));

        LinearLayout.LayoutParams pillParams =
                new LinearLayout.LayoutParams(-2, -2);
        pillParams.gravity = Gravity.CENTER_HORIZONTAL;
        pillParams.setMargins(0, dp(12), 0, dp(12));

        heroCard.addView(statusPill, pillParams);

        TextView heroInfo = new TextView(this);
        heroInfo.setText(
                "অজানা নম্বর থেকে আসা কলগুলো\n" +
                "আমি স্ক্রিন করে আপনার জন্য\n" +
                "সঠিক তথ্য জানাবো।"
        );
        heroInfo.setTextColor(lightText);
        heroInfo.setTextSize(14);
        heroInfo.setGravity(Gravity.CENTER);
        heroInfo.setPadding(0, dp(4), 0, dp(4));

        heroCard.addView(heroInfo);

        // ===== STATS ROW =====

        LinearLayout stats = new LinearLayout(this);
        stats.setOrientation(LinearLayout.HORIZONTAL);
        stats.setGravity(Gravity.CENTER);
        stats.setPadding(0, dp(18), 0, dp(18));

        totalCallsCard = createStatCard(
                "📞",
                "Total Calls\nScreened",
                "0",
                Color.rgb(30, 200, 130)
        );

        trustedStatCard = createStatCard(
                "🛡",
                "Trusted\nNumbers",
                "0",
                Color.rgb(60, 150, 255)
        );

        TextView blockedCard = createStatCard(
                "⛔",
                "Blocked\n(If Any)",
                "0",
                Color.rgb(170, 90, 255)
        );

        LinearLayout.LayoutParams statLp1 =
                new LinearLayout.LayoutParams(0, -2, 1);
        statLp1.setMargins(0, 0, dp(6), 0);

        LinearLayout.LayoutParams statLp2 =
                new LinearLayout.LayoutParams(0, -2, 1);
        statLp2.setMargins(dp(3), 0, dp(3), 0);

        LinearLayout.LayoutParams statLp3 =
                new LinearLayout.LayoutParams(0, -2, 1);
        statLp3.setMargins(dp(6), 0, 0, 0);

        stats.addView(totalCallsCard, statLp1);
        stats.addView(trustedStatCard, statLp2);
        stats.addView(blockedCard, statLp3);

        content.addView(stats);

        // ===== TRUSTED NUMBERS CARD =====

        LinearLayout trustedBox = createCard(
                Color.rgb(12, 20, 60),
                blue
        );

        LinearLayout tHead = new LinearLayout(this);
        tHead.setOrientation(LinearLayout.HORIZONTAL);
        tHead.setGravity(Gravity.CENTER_VERTICAL);

        TextView tTitle = new TextView(this);
        tTitle.setText("👥   Trusted Numbers");
        tTitle.setTextColor(white);
        tTitle.setTextSize(19);
        tTitle.setTypeface(Typeface.DEFAULT, Typeface.BOLD);

        LinearLayout.LayoutParams tTitleLp =
                new LinearLayout.LayoutParams(0, -2, 1);
        tHead.addView(tTitle, tTitleLp);

        trustedPill = new TextView(this);
        trustedPill.setText("0 Trusted");
        trustedPill.setTextColor(white);
        trustedPill.setTextSize(12);
        trustedPill.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        trustedPill.setGravity(Gravity.CENTER);
        trustedPill.setBackground(makeGlowBg(
                new int[]{
                        Color.rgb(20, 190, 110),
                        Color.rgb(20, 150, 190)
                },
                Color.rgb(40, 230, 150),
                20
        ));
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
        trustedNumberInput.setPadding(
                dp(18), dp(10), dp(18), dp(10)
        );

        LinearLayout.LayoutParams inLp =
                new LinearLayout.LayoutParams(-1, dp(56));
        trustedBox.addView(trustedNumberInput, inLp);

        TextView addButton = new TextView(this);
        addButton.setText("＋   ADD TRUSTED NUMBER");
        addButton.setTextColor(white);
        addButton.setTextSize(14);
        addButton.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        addButton.setGravity(Gravity.CENTER);
        addButton.setClickable(true);
        addButton.setFocusable(true);
        addButton.setBackground(makeGlowBg(
                new int[]{
                        Color.rgb(0, 130, 255),
                        Color.rgb(190, 40, 255)
                },
                Color.rgb(140, 80, 255),
                28
        ));

        LinearLayout.LayoutParams addLp =
                new LinearLayout.LayoutParams(-1, dp(72));
        addLp.setMargins(0, dp(14), 0, dp(4));

        trustedBox.addView(addButton, addLp);

        addButton.setOnClickListener(
                v -> addTrustedNumber()
        );

        content.addView(trustedBox);

        // ===== SAVED NUMBERS HEADER =====

        LinearLayout savedHead = new LinearLayout(this);
        savedHead.setOrientation(LinearLayout.HORIZONTAL);
        savedHead.setGravity(Gravity.CENTER_VERTICAL);
        savedHead.setPadding(dp(6), dp(22), dp(6), dp(10));

        TextView savedTitle = new TextView(this);
        savedTitle.setText("⭐   Saved Trusted Numbers");
        savedTitle.setTextColor(white);
        savedTitle.setTextSize(18);
        savedTitle.setTypeface(Typeface.DEFAULT, Typeface.BOLD);

        LinearLayout.LayoutParams stLp =
                new LinearLayout.LayoutParams(0, -2, 1);
        savedHead.addView(savedTitle, stLp);

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
        footer.setText(
                "\n🎙   AI Call Assistant\n" +
                "বাংলায় স্মার্ট কল সহায়তা"
        );
        footer.setTextColor(dimText);
        footer.setTextSize(13);
        footer.setGravity(Gravity.CENTER);
        footer.setPadding(0, dp(24), 0, dp(14));

        content.addView(footer);

        // ===== BOTTOM NAV =====

        LinearLayout bottomNav = new LinearLayout(this);
        bottomNav.setOrientation(LinearLayout.HORIZONTAL);
        bottomNav.setGravity(Gravity.CENTER_VERTICAL);
        bottomNav.setPadding(dp(10), dp(10), dp(10), dp(14));

        GradientDrawable navBg = new GradientDrawable();
        navBg.setColor(Color.rgb(8, 10, 30));
        navBg.setStroke(dp(2), purple);
        navBg.setCornerRadii(new float[]{
                dp(30), dp(30),
                dp(30), dp(30),
                0, 0,
                0, 0
        });
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

        LinearLayout.LayoutParams hwLp =
                new LinearLayout.LayoutParams(0, -2, 1);
        bottomNav.addView(homeWrap, hwLp);

        FrameLayout micWrap = new FrameLayout(this);
        micWrap.setPadding(dp(6), 0, dp(6), 0);

        View micGlow = new View(this);
        micGlow.setBackground(makeGlowBg(
                new int[]{
                        Color.rgb(120, 60, 255),
                        Color.rgb(200, 40, 200)
                },
                Color.rgb(180, 80, 255),
                40
        ));

        FrameLayout.LayoutParams mgLp =
                new FrameLayout.LayoutParams(
                        dp(68), dp(68),
                        Gravity.CENTER
                );
        micWrap.addView(micGlow, mgLp);

        TextView micIcon = new TextView(this);
        micIcon.setText("🎤");
        micIcon.setTextSize(26);
        micIcon.setGravity(Gravity.CENTER);

        FrameLayout.LayoutParams miLp =
                new FrameLayout.LayoutParams(
                        -2, -2,
                        Gravity.CENTER
                );
        micWrap.addView(micIcon, miLp);

        LinearLayout.LayoutParams mwLp =
                new LinearLayout.LayoutParams(-2, -2);
        bottomNav.addView(micWrap, mwLp);

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

        LinearLayout.LayoutParams hwlLp =
                new LinearLayout.LayoutParams(0, -2, 1);
        bottomNav.addView(hWrap, hwlLp);

        LinearLayout.LayoutParams navLp =
                new LinearLayout.LayoutParams(-1, -2);
        navLp.setMargins(dp(10), 0, dp(10), dp(10));

        root.addView(bottomNav, navLp);

        setContentView(root);
    }

    private Drawable makeGlowBg(
            int[] gradientColors,
            int glowColor,
            float radiusDp
    ) {

        float radius =
                radiusDp *
                getResources().getDisplayMetrics().density;
        int[] glowAlphas = { 25, 55, 100 };
        Drawable[] layers = new Drawable[4];

        for (int i = 0; i < 3; i++) {
            GradientDrawable g = new GradientDrawable();
            g.setShape(GradientDrawable.RECTANGLE);
            g.setCornerRadius(radius);
            g.setColor(withAlpha(glowColor, glowAlphas[i]));
            layers[i] = g;
        }

        GradientDrawable main = new GradientDrawable(
                GradientDrawable.Orientation.LEFT_RIGHT,
                gradientColors
        );
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

    private int withAlpha(int color, int alpha) {
        return (color & 0x00FFFFFF) | (alpha << 24);
    }

    private int dp(int value) {
        return (int)(
                value *
                getResources().getDisplayMetrics().density
        );
    }

    private LinearLayout createCard(
            int background,
            int stroke1
    ) {

        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setGravity(Gravity.CENTER_HORIZONTAL);
        card.setPadding(
                dp(18), dp(22),
                dp(18), dp(22)
        );

        GradientDrawable bg = new GradientDrawable();
        bg.setColor(background);
        bg.setCornerRadius(dp(28));
        bg.setStroke(dp(2), stroke1);

        card.setBackground(bg);

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(
                        -1,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                )
