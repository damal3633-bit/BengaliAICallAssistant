package com.example.bengaliaicallassistant;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class MainActivity extends Activity {

    private LinearLayout conversationLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(Color.rgb(5, 10, 20));

        TextView header = new TextView(this);
        header.setText("Bengali AI Call Assistant");
        header.setTextColor(Color.WHITE);
        header.setTextSize(22);
        header.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        header.setGravity(Gravity.CENTER);
        header.setPadding(20, 35, 20, 35);

        root.addView(header);

        TextView status = new TextView(this);
        status.setText("●  Assistant Ready");
        status.setTextColor(Color.rgb(50, 220, 120));
        status.setTextSize(16);
        status.setPadding(25, 10, 25, 20);

        root.addView(status);

        ScrollView scrollView = new ScrollView(this);

        conversationLayout = new LinearLayout(this);
        conversationLayout.setOrientation(LinearLayout.VERTICAL);
        conversationLayout.setPadding(20, 10, 20, 20);

        TextView empty = new TextView(this);
        empty.setText(
                "এখনও কোনো কল নেই।\n\n" +
                "Unknown call এলে এখানে লাইভ কথোপকথনের text দেখা যাবে।"
        );
        empty.setTextColor(Color.LTGRAY);
        empty.setTextSize(16);
        empty.setGravity(Gravity.CENTER);
        empty.setPadding(20, 80, 20, 80);

        conversationLayout.addView(empty);
        scrollView.addView(conversationLayout);

        root.addView(
                scrollView,
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        0,
                        1
                )
        );

        Button handoverButton = new Button(this);
        handoverButton.setText("CALL HANDOVER");
        handoverButton.setTextSize(16);
        handoverButton.setTextColor(Color.WHITE);
        handoverButton.setBackgroundColor(Color.rgb(25, 100, 220));

        handoverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                status.setText("●  Handover requested");
            }
        });

        root.addView(
                handoverButton,
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        65
                )
        );

        setContentView(root);
    }
}
