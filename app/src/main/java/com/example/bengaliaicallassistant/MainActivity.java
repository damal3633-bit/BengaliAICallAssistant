package com.example.bengaliaicallassistant;

import android.app.Activity;
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
import android.view.ViewGroup;
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

    private android.content.SharedPreferences preferences;

    private static final String PREFS_NAME = "trusted_numbers";
    private static final String NUMBERS_KEY = "numbers";

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
