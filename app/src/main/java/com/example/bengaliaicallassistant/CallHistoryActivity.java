package com.example.bengaliaicallassistant;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class CallHistoryActivity extends Activity {

    static final String PREFS = "trusted_numbers";
    static final String HKEY = "call_history";

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(buildUI());
    }

    View buildUI() {
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(Color.rgb(3, 5, 22));
        root.setPadding(dp(20), dp(30), dp(20), dp(30));

        ScrollView sv = new ScrollView(this);
        sv.setFillViewport(true);

        LinearLayout content = new LinearLayout(this);
        content.setOrientation(LinearLayout.VERTICAL);

        LinearLayout header = new LinearLayout(this);
        header.setOrientation(LinearLayout.HORIZONTAL);
        header.setGravity(Gravity.CENTER_VERTICAL);
        header.setPadding(0, 0, 0, dp(20));

        TextView back = new TextView(this);
        back.setText("←");
        back.setTextSize(28);
        back.setTextColor(Color.WHITE);
        back.setPadding(0, 0, dp(16), 0);
        back.setClickable(true);
        back.setOnClickListener(v -> finish());
        header.addView(back);

        TextView title = new TextView(this);
        title.setText("📞  Call History");
        title.setTextColor(Color.WHITE);
        title.setTextSize(22);
        title.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        header.addView(title);

        content.addView(header);

        SharedPreferences prefs = getSharedPreferences(PREFS, MODE_PRIVATE);
        Set<String> raw = prefs.getStringSet(HKEY, new HashSet<>());
        ArrayList<String> items = new ArrayList<>(raw);

        Collections.sort(items, (a, b) -> {
            try {
                long ta = Long.parseLong(a.split("\\|")[1]);
                long tb = Long.parseLong(b.split("\\|")[1]);
                return Long.compare(tb, ta);
            } catch (Exception e) {
                return 0;
            }
        });

        if (items.isEmpty()) {
            TextView empty = new TextView(this);
            empty.setText("এখনো কোনো কল history নেই।");
            empty.setTextColor(Color.rgb(140, 155, 195));
            empty.setTextSize(15);
            empty.setPadding(dp(8), dp(20), dp(8), dp(20));
            content.addView(empty);
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat(
                    "dd MMM yyyy, hh:mm a", Locale.getDefault());

            for (String item : items) {
                content.addView(makeRow(item, sdf));
            }
        }

        sv.addView(content);
        root.addView(sv, new LinearLayout.LayoutParams(-1, -1));
        return root;
    }

    View makeRow(String item, SimpleDateFormat sdf) {
        String[] parts = item.split("\\|");
        String number = parts.length > 0 ? parts[0] : "Unknown";
        long time = 0;
        try {
            time = Long.parseLong(parts[1]);
        } catch (Exception e) {
            time = 0;
        }

        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.VERTICAL);
        row.setPadding(dp(16), dp(14), dp(16), dp(14));

        GradientDrawable bg = new GradientDrawable();
        bg.setColor(Color.rgb(10, 16, 48));
        bg.setCornerRadius(dp(20));
        bg.setStroke(dp(2), Color.rgb(180, 80, 255));
        row.setBackground(bg);

        LinearLayout.LayoutParams rp = new LinearLayout.LayoutParams(-1, -2);
        rp.setMargins(0, dp(6), 0, dp(6));
        row.setLayoutParams(rp);

        TextView numText = new TextView(this);
        numText.setText("👤  " + number);
        numText.setTextColor(Color.WHITE);
        numText.setTextSize(16);
        numText.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        row.addView(numText);

        TextView timeText = new TextView(this);
        timeText.setText(sdf.format(new Date(time)));
        timeText.setTextColor(Color.rgb(140, 155, 195));
        timeText.setTextSize(13);
        timeText.setPadding(0, dp(4), 0, 0);
        row.addView(timeText);

        return row;
    }

    int dp(int v) {
        return (int) (v * getResources().getDisplayMetrics().density);
    }
}
