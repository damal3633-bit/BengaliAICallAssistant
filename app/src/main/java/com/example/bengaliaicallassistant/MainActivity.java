package com.example.bengaliaicallassistant;

import android.app.Activity;
import android.content.Intent;
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

    EditText numIn;
    LinearLayout listBox;
    TextView savedCount;
    TextView trustedCard;
    TextView trustedPill;
    TextView totalCard;
    TextView blockedCard;
    SharedPreferences prefs;

    static final String PNAME = "trusted_numbers";
    static final String NKEY = "numbers";
    static final String TKEY = "total_calls";
    static final String BKEY = "blocked_numbers";
    static final String BCKEY = "blocked_calls";

    static final int W = Color.WHITE;
    static final int LT = Color.rgb(200, 210, 235);
    static final int DT = Color.rgb(140, 155, 195);
    static final int BL = Color.rgb(60, 170, 255);
    static final int PU = Color.rgb(180, 80, 255);

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        prefs = getSharedPreferences(PNAME, MODE_PRIVATE);
        setContentView(buildUI());
        refreshList();
        refreshStats();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshStats();
    }

    void refreshStats() {
        if (totalCard != null) {
            int t = prefs.getInt(TKEY, 0);
            totalCard.setText("📞\nTotal Calls\nScreened\n" + t);
        }
        if (blockedCard != null) {
            Set<String> bl = prefs.getStringSet(BKEY, new HashSet<>());
            blockedCard.setText("⛔\nBlocked\n(If Any)\n" + bl.size());
        }
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
        root.addView(sv, new LinearLayout.LayoutParams(-1, 0, 1));

        LinearLayout hd = new LinearLayout(this);
        hd.setOrientation(LinearLayout.HORIZONTAL);
        hd.setGravity(Gravity.CENTER_VERTICAL);
        hd.setPadding(dp(4), 0, dp(4), 0);

        TextView hi = new TextView(this);
        hi.setText("🤖");
        hi.setTextSize(30);
        LinearLayout.LayoutParams hiP = new LinearLayout.LayoutParams(-2, -2);
        hiP.setMargins(0, 0, dp(12), 0);
        hd.addView(hi, hiP);

        TextView ti = new TextView(this);
        ti.setText("Bengali AI Call Assistant");
        ti.setTextColor(W);
        ti.setTextSize(20);
        ti.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        hd.addView(ti, new LinearLayout.LayoutParams(0, -2, 1));

        TextView gr = new TextView(this);
        gr.setText("⚙");
        gr.setTextSize(24);
        gr.setTextColor(Color.rgb(120, 150, 220));
        hd.addView(gr);
        c.addView(hd);

        ti.post(() -> {
            Shader sh = new LinearGradient(0, 0, ti.getWidth(), 0,
                    new int[]{Color.rgb(80, 180, 255),
                            Color.rgb(180, 100, 255),
                            Color.rgb(255, 120, 200)},
                    null, Shader.TileMode.CLAMP);
            ti.getPaint().setShader(sh);
            ti.invalidate();
        });

        TextView sub = new TextView(this);
        sub.setText("অজানা কল, এখন আর চিন্তার কারণ নয়!");
        sub.setTextColor(DT);
        sub.setTextSize(13);
        sub.setGravity(Gravity.CENTER);
        sub.setPadding(0, dp(6), 0, dp(22));
        c.addView(sub);

        LinearLayout hero = card(Color.rgb(15, 22, 70), PU);
        c.addView(hero);

        FrameLayout rw = new FrameLayout(this);
        View rg = new View(this);
        rg.setBackground(glow(
                new int[]{Color.rgb(70, 130, 255), Color.rgb(180, 80, 255)},
                Color.rgb(140, 100, 255), 60));
        rw.addView(rg, new FrameLayout.LayoutParams(dp(120), dp(120), Gravity.CENTER));

        TextView re = new TextView(this);
        re.setText("🤖");
        re.setTextSize(58);
        re.setGravity(Gravity.CENTER);
        rw.addView(re, new FrameLayout.LayoutParams(-2, -2, Gravity.CENTER));

        LinearLayout.LayoutParams rp = new LinearLayout.LayoutParams(-2, -2);
        rp.gravity = Gravity.CENTER_HORIZONTAL;
        rp.setMargins(0, 0, 0, dp(8));
        hero.addView(rw, rp);

        TextView ht = new TextView(this);
        ht.setText("AI Call Assistant");
        ht.setTextColor(W);
        ht.setTextSize(23);
        ht.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        ht.setGravity(Gravity.CENTER);
        hero.addView(ht);

        TextView sp = new TextView(this);
        sp.setText("✓   Call Screening ON");
        sp.setTextColor(W);
        sp.setTextSize(14);
        sp.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        sp.setGravity(Gravity.CENTER);
        sp.setBackground(glow(
                new int[]{Color.rgb(20, 190, 110), Color.rgb(20, 150, 190)},
                Color.rgb(40, 230, 150), 24));
        sp.setPadding(dp(18), dp(9), dp(18), dp(9));
        LinearLayout.LayoutParams spP = new LinearLayout.LayoutParams(-2, -2);
        spP.gravity = Gravity.CENTER_HORIZONTAL;
        spP.setMargins(0, dp(12), 0, dp(12));
        hero.addView(sp, spP);

        TextView hi2 = new TextView(this);
        hi2.setText("অজানা নম্বর থেকে আসা কলগুলো\n" +
                "আমি স্ক্রিন করে আপনার জন্য\n" +
                "সঠিক তথ্য জানাবো।");
        hi2.setTextColor(LT);
        hi2.setTextSize(14);
        hi2.setGravity(Gravity.CENTER);
        hi2.setPadding(0, dp(4), 0, dp(4));
        hero.addView(hi2);

        LinearLayout st = new LinearLayout(this);
        st.setOrientation(LinearLayout.HORIZONTAL);
        st.setGravity(Gravity.CENTER);
        st.setPadding(0, dp(18), 0, dp(18));

        totalCard = statCard("📞", "Total Calls\nScreened", "0",
                Color.rgb(30, 200, 130));
        trustedCard = statCard("🛡", "Trusted\nNumbers", "0",
                Color.rgb(60, 150, 255));
        blockedCard = statCard("⛔", "Blocked\n(If Any)", "0",
                Color.rgb(170, 90, 255));

        blockedCard.setClickable(true);
        blockedCard.setFocusable(true);
        blockedCard.setOnClickListener(v -> startActivity(
                new Intent(MainActivity.this, BlockedNumbersActivity.class)));

        LinearLayout.LayoutParams s1 = new LinearLayout.LayoutParams(0, -2, 1);
        s1.setMargins(0, 0, dp(6), 0);
        LinearLayout.LayoutParams s2 = new LinearLayout.LayoutParams(0, -2, 1);
        s2.setMargins(dp(3), 0, dp(3), 0);
        LinearLayout.LayoutParams s3 = new LinearLayout.LayoutParams(0, -2, 1);
        s3.setMargins(dp(6), 0, 0, 0);
        st.addView(totalCard, s1);
        st.addView(trustedCard, s2);
        st.addView(blockedCard, s3);
        c.addView(st);

        LinearLayout tb = card(Color.rgb(12, 20, 60), BL);
        c.addView(tb);

        LinearLayout th = new LinearLayout(this);
        th.setOrientation(LinearLayout.HORIZONTAL);
        th.setGravity(Gravity.CENTER_VERTICAL);
        TextView tt = new TextView(this);
        tt.setText("👥   Trusted Numbers");
        tt.setTextColor(W);
        tt.setTextSize(19);
        tt.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        th.addView(tt, new LinearLayout.LayoutParams(0, -2, 1));

        trustedPill = new TextView(this);
        trustedPill.setText("0 Trusted");
        trustedPill.setTextColor(W);
        trustedPill.setTextSize(12);
        trustedPill.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        trustedPill.setGravity(Gravity.CENTER);
        trustedPill.setBackground(glow(
                new int[]{Color.rgb(20, 190, 110), Color.rgb(20, 150, 190)},
                Color.rgb(40, 230, 150), 20));
        trustedPill.setPadding(dp(14), dp(6), dp(14), dp(6));
        th.addView(trustedPill);
        tb.addView(th);

        TextView ti2 = new TextView(this);
        ti2.setText("বিশ্বস্ত নম্বরগুলো এখানে দিন");
        ti2.setTextColor(DT);
        ti2.setTextSize(13);
        ti2.setPadding(0, dp(6), 0, dp(14));
        tb.addView(ti2);

        numIn = new EditText(this);
        numIn.setHint("+919876543210");
        numIn.setHintTextColor(Color.GRAY);
        numIn.setTextColor(W);
        numIn.setTextSize(15);
        numIn.setInputType(InputType.TYPE_CLASS_PHONE);
        GradientDrawable ib = new GradientDrawable();
        ib.setColor(Color.rgb(8, 12, 38));
        ib.setCornerRadius(dp(22));
        ib.setStroke(dp(2), BL);
        numIn.setBackground(ib);
        numIn.setPadding(dp(18), dp(10), dp(18), dp(10));
        tb.addView(numIn, new LinearLayout.LayoutParams(-1, dp(56)));

        TextView ab = new TextView(this);
        ab.setText("＋   ADD TRUSTED NUMBER");
        ab.setTextColor(W);
        ab.setTextSize(14);
        ab.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        ab.setGravity(Gravity.CENTER);
        ab.setClickable(true);
        ab.setFocusable(true);
        ab.setBackground(glow(
                new int[]{Color.rgb(0, 130, 255), Color.rgb(190, 40, 255)},
                Color.rgb(140, 80, 255), 28));
        LinearLayout.LayoutParams abP = new LinearLayout.LayoutParams(-1, dp(72));
        abP.setMargins(0, dp(14), 0, dp(4));
        tb.addView(ab, abP);
        ab.setOnClickListener(v -> addNum());

        LinearLayout sh = new LinearLayout(this);
        sh.setOrientation(LinearLayout.HORIZONTAL);
        sh.setGravity(Gravity.CENTER_VERTICAL);
        sh.setPadding(dp(6), dp(22), dp(6), dp(10));
        TextView st2 = new TextView(this);
        st2.setText("⭐   Saved Trusted Numbers");
        st2.setTextColor(W);
        st2.setTextSize(18);
        st2.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        sh.addView(st2, new LinearLayout.LayoutParams(0, -2, 1));
        c.addView(sh);

        savedCount = new TextView(this);
        savedCount.setTextColor(DT);
        savedCount.setTextSize(13);
        savedCount.setPadding(dp(6), 0, dp(6), dp(8));
        c.addView(savedCount);

        listBox = new LinearLayout(this);
        listBox.setOrientation(LinearLayout.VERTICAL);
        c.addView(listBox);

        TextView ft = new TextView(this);
        ft.setText("\n🎙   AI Call Assistant\nবাংলায় স্মার্ট কল সহায়তা");
        ft.setTextColor(DT);
        ft.setTextSize(13);
        ft.setGravity(Gravity.CENTER);
        ft.setPadding(0, dp(24), 0, dp(14));
        c.addView(ft);

        LinearLayout bn = new LinearLayout(this);
        bn.setOrientation(LinearLayout.HORIZONTAL);
        bn.setGravity(Gravity.CENTER_VERTICAL);
        bn.setPadding(dp(10), dp(10), dp(10), dp(10));
        GradientDrawable nb = new GradientDrawable();
        nb.setColor(Color.rgb(8, 10, 30));
        nb.setStroke(dp(2), PU);
        nb.setCornerRadii(new float[]{
                dp(30), dp(30), dp(30), dp(30), 0, 0, 0, 0});
        bn.setBackground(nb);

        LinearLayout hm = new LinearLayout(this);
        hm.setOrientation(LinearLayout.VERTICAL);
        hm.setGravity(Gravity.CENTER);
        TextView hI = new TextView(this);
        hI.setText("🏠");
        hI.setTextSize(22);
        hI.setGravity(Gravity.CENTER);
        TextView hL = new TextView(this);
        hL.setText("Home");
        hL.setTextColor(PU);
        hL.setTextSize(12);
        hL.setGravity(Gravity.CENTER);
        hL.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        hm.addView(hI);
        hm.addView(hL);
        bn.addView(hm, new LinearLayout.LayoutParams(0, -2, 1));

        LinearLayout mw = new LinearLayout(this);
        mw.setOrientation(LinearLayout.VERTICAL);
        mw.setGravity(Gravity.CENTER);
        mw.setClickable(true);
        mw.setFocusable(true);
        mw.setOnClickListener(v -> startActivity(
                new Intent(MainActivity.this, CallHistoryActivity.class)));

        FrameLayout mf = new FrameLayout(this);
        View mg = new View(this);
        mg.setBackground(glow(
                new int[]{Color.rgb(120, 60, 255), Color.rgb(200, 40, 200)},
                Color.rgb(180, 80, 255), 40));
        mf.addView(mg, new FrameLayout.LayoutParams(dp(68), dp(68), Gravity.CENTER));
        TextView mI = new TextView(this);
        mI.setText("🎤");
        mI.setTextSize(26);
        mI.setGravity(Gravity.CENTER);
        mf.addView(mI, new FrameLayout.LayoutParams(-2, -2, Gravity.CENTER));
        mw.addView(mf, new LinearLayout.LayoutParams(dp(72), dp(72)));

        TextView mLabel = new TextView(this);
        mLabel.setText("History");
        mLabel.setTextSize(11);
        mLabel.setTextColor(W);
        mLabel.setGravity(Gravity.CENTER);
        mLabel.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        LinearLayout.LayoutParams mlP = new LinearLayout.LayoutParams(-2, -2);
        mlP.setMargins(0, dp(2), 0, 0);
        mw.addView(mLabel, mlP);
        bn.addView(mw, new LinearLayout.LayoutParams(-2, -2));

        LinearLayout hw = new LinearLayout(this);
        hw.setOrientation(LinearLayout.VERTICAL);
        hw.setGravity(Gravity.CENTER);
        hw.setClickable(true);
        hw.setFocusable(true);
        hw.setOnClickListener(v -> startActivity(
                new Intent(MainActivity.this, BlockedNumbersActivity.class)));

        TextView hoI = new TextView(this);
        hoI.setText("⛔");
        hoI.setTextSize(22);
        hoI.setGravity(Gravity.CENTER);
        TextView hoL = new TextView(this);
        hoL.setText("Blocked");
        hoL.setTextColor(DT);
        hoL.setTextSize(12);
        hoL.setGravity(Gravity.CENTER);
        hw.addView(hoI);
        hw.addView(hoL);
        bn.addView(hw, new LinearLayout.LayoutParams(0, -2, 1));

        LinearLayout.LayoutParams bp = new LinearLayout.LayoutParams(-1, -2);
        bp.setMargins(dp(10), 0, dp(10), dp(10));
        root.addView(bn, bp);

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
                prefs.getStringSet(NKEY, new HashSet<>()));
        if (cur.contains(n)) {
            Toast.makeText(this, "এই নম্বরটি আগে থেকেই Trusted",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        cur.add(n);
        prefs.edit().putStringSet(NKEY, cur).apply();
        numIn.setText("");
        refreshList();
        Toast.makeText(this, "✓ Trusted number added", Toast.LENGTH_SHORT).show();
    }

    void refreshList() {
        listBox.removeAllViews();
        Set<String> sv = prefs.getStringSet(NKEY, new HashSet<>());
        ArrayList<String> nums = new ArrayList<>(sv);
        Collections.sort(nums);
        savedCount.setText(nums.size() + " Trusted number saved");
        if (trustedCard != null) {
            trustedCard.setText("🛡\nTrusted\nNumbers\n" + nums.size());
        }
        if (trustedPill != null) {
            trustedPill.setText(nums.size() + " Trusted");
        }
        if (nums.isEmpty()) {
            TextView e = new TextView(this);
            e.setText("এখনও কোনো Trusted Number যোগ করা হয়নি।");
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
        rb.setStroke(dp(2), PU);
        r.setBackground(rb);

        TextView nt = new TextView(this);
        nt.setText("👤  " + n);
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
                prefs.getStringSet(NKEY, new HashSet<>()));
        cur.remove(n);
        prefs.edit().putStringSet(NKEY, cur).apply();
        refreshList();
        Toast.makeText(this, "Trusted number deleted", Toast.LENGTH_SHORT).show();
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

    LinearLayout card(int bg, int s1) {
        LinearLayout cd = new LinearLayout(this);
        cd.setOrientation(LinearLayout.VERTICAL);
        cd.setGravity(Gravity.CENTER_HORIZONTAL);
        cd.setPadding(dp(18), dp(22), dp(18), dp(22));
        GradientDrawable b = new GradientDrawable();
        b.setColor(bg);
        b.setCornerRadius(dp(28));
        b.setStroke(dp(2), s1);
        cd.setBackground(b);
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(-1,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        p.setMargins(0, dp(8), 0, dp(8));
        cd.setLayoutParams(p);
        return cd;
    }

    TextView statCard(String ic, String lb, String v, int ac) {
        TextView t = new TextView(this);
        t.setText(ic + "\n" + lb + "\n" + v);
        t.setTextColor(W);
        t.setTextSize(13);
        t.setGravity(Gravity.CENTER);
        t.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
               t.setPadding(dp(6), dp(14), dp(6), dp(14));
        t.setLineSpacing(dp(2), 1f);
        GradientDrawable b = new GradientDrawable();
        b.setColor(Color.rgb(10, 16, 48));
        b.setCornerRadius(dp(22));
        b.setStroke(dp(2), ac);
        t.setBackground(b);
        return t;
    }

    String norm(String n) {
        return n.replace(" ", "").replace("-", "")
                .replace("(", "").replace(")", "");
    }
}
