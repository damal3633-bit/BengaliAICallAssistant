package com.example.bengaliaicallassistant;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class BlockedNumbersActivity extends Activity {

    EditText numIn;
    LinearLayout listBox;
    TextView countText;
    SharedPreferences prefs;

    static final String PNAME = "trusted_numbers";
    static final String BKEY = "blocked_numbers";

    static final int W = Color.WHITE;
    static final int DT = Color.rgb(140, 155, 195);
    static final int RED = Color.rgb(235, 60, 110);

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        prefs = getSharedPreferences(PNAME, MODE_PRIVATE);
        setContentView(buildUI());
        refreshList();
    }

    View buildUI() {
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(Color.rgb(3, 5, 22));

        ScrollView sv = new ScrollView(this);
        sv.setFillViewport(true);
        LinearLayout c = new LinearLayout(this);
        c.setOrientation(LinearLayout.VERTICAL);
        c.setPadding(dp(20), dp(30), dp(20), dp(30));
        sv.addView(c);
        root.addView(sv, new LinearLayout.LayoutParams(-1, -1));

        LinearLayout hd = new LinearLayout(this);
        hd.setOrientation(LinearLayout.HORIZONTAL);
        hd.setGravity(Gravity.CENTER_VERTICAL);
        hd.setPadding(0, 0, 0, dp(20));

        TextView back = new TextView(this);
        back.setText("←");
        back.setTextSize(28);
        back.setTextColor(W);
        back.setPadding(0, 0, dp(16), 0);
        back.setClickable(true);
        back.setOnClickListener(v -> finish());
        hd.addView(back);

        TextView ti = new TextView(this);
        ti.setText("⛔  Blocked Numbers");
        ti.setTextColor(W);
        ti.setTextSize(22);
        ti.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        hd.addView(ti);
        c.addView(hd);

        TextView info = new TextView(this);
        info.setText("এই নম্বরগুলো থেকে আসা কল অটোমেটিক reject হবে।");
        info.setTextColor(DT);
        info.setTextSize(13);
        info.setPadding(0, 0, 0, dp(18));
        c.addView(info);

        numIn = new EditText(this);
        numIn.setHint("+919876543210");
        numIn.setHintTextColor(Color.GRAY);
        numIn.setTextColor(W);
        numIn.setTextSize(15);
        numIn.setInputType(InputType.TYPE_CLASS_PHONE);
        GradientDrawable ib = new GradientDrawable();
        ib.setColor(Color.rgb(8, 12, 38));
        ib.setCornerRadius(dp(22));
        ib.setStroke(dp(2), RED);
        numIn.setBackground(ib);
        numIn.setPadding(dp(18), dp(10), dp(18), dp(10));
        c.addView(numIn, new LinearLayout.LayoutParams(-1, dp(56)));

        TextView ab = new TextView(this);
        ab.setText("⛔   BLOCK THIS NUMBER");
        ab.setTextColor(W);
        ab.setTextSize(14);
        ab.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        ab.setGravity(Gravity.CENTER);
        ab.setClickable(true);
        ab.setFocusable(true);
        ab.setBackground(glow(
                new int[]{Color.rgb(230, 60, 90), Color.rgb(150, 30, 180)},
                RED, 28));
        LinearLayout.LayoutParams abP = new LinearLayout.LayoutParams(-1, dp(72));
        abP.setMargins(0, dp(14), 0, dp(4));
        c.addView(ab, abP);
        ab.setOnClickListener(v -> addNum());

        TextView st = new TextView(this);
        st.setText("Blocked list");
        st.setTextColor(W);
        st.setTextSize(18);
        st.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        st.setPadding(0, dp(24), 0, dp(8));
        c.addView(st);

        countText = new TextView(this);
        countText.setTextColor(DT);
        countText.setTextSize(13);
        countText.setPadding(0, 0, 0, dp(8));
        c.addView(countText);

        listBox = new LinearLayout(this);
        listBox.setOrientation(LinearLayout.VERTICAL);
        c.addView(listBox);

        return root;
    }

    void addNum() {
        String n = numIn.getText().toString().trim();
        if (n.isEmpty()) {
            Toast.makeText(this, "একটি ফোন নম্বর দিন", Toast.LENGTH_SHORT).show();
            return;
        }
        n = norm(n);
        Set<String> cur = new HashSet<>(
                prefs.getStringSet(BKEY, new HashSet<>()));
        if (cur.contains(n)) {
            Toast.makeText(this, "এই নম্বরটি আগে থেকেই Blocked",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        cur.add(n);
        prefs.edit().putStringSet(BKEY, cur).apply();
        numIn.setText("");
        refreshList();
        Toast.makeText(this, "✓ Number blocked", Toast.LENGTH_SHORT).show();
    }

    void refreshList() {
        listBox.removeAllViews();
        Set<String> sv = prefs.getStringSet(BKEY, new HashSet<>());
        ArrayList<String> nums = new ArrayList<>(sv);
        Collections.sort(nums);
        countText.setText(nums.size() + " blocked number(s)");
        if (nums.isEmpty()) {
            TextView e = new TextView(this);
            e.setText("এখনও কোনো Blocked Number যোগ করা হয়নি।");
            e.setTextColor(DT);
            e.setTextSize(14);
            e.setPadding(dp(8), dp(10), dp(8), dp(10));
            listBox.addView(e);
            return;
        }
        for (String n : nums) {
            addRow(n);
        }
    }

    void addRow(String n) {
        LinearLayout r = new LinearLayout(this);
        r.setOrientation(LinearLayout.HORIZONTAL);
        r.setGravity(Gravity.CENTER_VERTICAL);
        r.setPadding(dp(14), dp(10), dp(10), dp(10));
        GradientDrawable rb = new GradientDrawable();
        rb.setColor(Color.rgb(10, 16, 48));
        rb.setCornerRadius(dp(22));
        rb.setStroke(dp(2), RED);
        r.setBackground(rb);

        TextView nt = new TextView(this);
        nt.setText("⛔  " + n);
        nt.setTextColor(W);
        nt.setTextSize(15);
        r.addView(nt, new LinearLayout.LayoutParams(0, -2, 1));

        TextView db = new TextView(this);
        db.setText("🗑");
        db.setTextSize(18);
        db.setTextColor(W);
        db.setGravity(Gravity.CENTER);
        db.setClickable(true);
        db.setFocusable(true);
        db.setBackground(glow(
                new int[]{Color.rgb(235, 45, 110), Color.rgb(160, 20, 90)},
                Color.rgb(255, 60, 120), 18));
        db.setOnClickListener(v -> delNum(n));

        LinearLayout.LayoutParams dp2 =
                new LinearLayout.LayoutParams(dp(52), dp(52));
        dp2.setMargins(dp(6), 0, 0, 0);
        r.addView(db, dp2);

        LinearLayout.LayoutParams rp = new LinearLayout.LayoutParams(-1, -2);
        rp.setMargins(0, dp(6), 0, dp(6));
        listBox.addView(r, rp);
    }

    void delNum(String n) {
        Set<String> cur = new HashSet<>(
                prefs.getStringSet(BKEY, new HashSet<>()));
        cur.remove(n);
        prefs.edit().putStringSet(BKEY, cur).apply();
        refreshList();
        Toast.makeText(this, "Unblocked", Toast.LENGTH_SHORT).show();
    }

    Drawable glow(int[] colors, int gc, float rdp) {
        float r = rdp * getResources().getDisplayMetrics().density;
        int[] a = {25, 55, 100};
        Drawable[] ls = new Drawable[4];
        for (int i = 0; i < 3; i++) {
            GradientDrawable g = new GradientDrawable();
            g.setShape(GradientDrawable.RECTANGLE);
            g.setCornerRadius(r);
            g.setColor((gc & 0x00FFFFFF) | (a[i] << 24));
            ls[i] = g;
        }
        GradientDrawable m = new GradientDrawable(
                GradientDrawable.Orientation.LEFT_RIGHT, colors);
        m.setShape(GradientDrawable.RECTANGLE);
        m.setCornerRadius(r);
        ls[3] = m;
        LayerDrawable l = new LayerDrawable(ls);
        int s = dp(2);
        l.setLayerInset(1, s, s, s, s);
        l.setLayerInset(2, s * 2, s * 2, s * 2, s * 2);
        l.setLayerInset(3, s * 3, s * 3, s * 3, s * 3);
        return l;
    }

    int dp(int v) {
        return (int) (v * getResources().getDisplayMetrics().density);
    }

    String norm(String n) {
        return n.replace(" ", "").replace("-", "")
                .replace("(", "").replace(")", "");
    }
                        }
