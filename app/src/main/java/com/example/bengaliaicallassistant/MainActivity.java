package com.example.bengaliaicallassistant;

import android.app.Activity;
import android.app.role.RoleManager;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Gravity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {

    private static final int ROLE_REQUEST_CODE = 1001;
    private TextView statusText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setGravity(Gravity.CENTER);
        root.setPadding(30, 40, 30, 40);
        root.setBackgroundColor(Color.rgb(5, 10, 20));

        TextView title = new TextView(this);
        title.setText("Bengali AI Call Assistant");
        title.setTextColor(Color.WHITE);
        title.setTextSize(24);
        title.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        title.setGravity(Gravity.CENTER);

        root.addView(title);

        statusText = new TextView(this);
        statusText.setText(
                "Call Assistant প্রস্তুত\n\n" +
                "Incoming call শনাক্ত করার জন্য Call Screening চালু করুন।"
        );
        statusText.setTextColor(Color.LTGRAY);
        statusText.setTextSize(17);
        statusText.setGravity(Gravity.CENTER);
        statusText.setPadding(10, 50, 10, 30);

        root.addView(statusText);

        Button enableButton = new Button(this);
        enableButton.setText("ENABLE CALL SCREENING");

        enableButton.setOnClickListener(v -> requestCallScreeningRole());

        root.addView(
                enableButton,
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        65
                )
        );

        setContentView(root);
    }

    private void requestCallScreeningRole() {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {

            RoleManager roleManager = getSystemService(RoleManager.class);

            if (roleManager != null &&
                    roleManager.isRoleAvailable(RoleManager.ROLE_CALL_SCREENING)) {

                if (!roleManager.isRoleHeld(RoleManager.ROLE_CALL_SCREENING)) {

                    Intent intent = roleManager.createRequestRoleIntent(
                            RoleManager.ROLE_CALL_SCREENING
                    );

                    startActivityForResult(intent, ROLE_REQUEST_CODE);

                } else {
                    statusText.setText(
                            "✓ Call Screening চালু আছে\n\n" +
                            "এখন incoming call শনাক্ত করা যাবে।"
                    );
                }

            } else {
                statusText.setText(
                        "এই ফোনে Call Screening role available নয়।"
                );
            }

        } else {
            statusText.setText(
                    "এই feature-এর জন্য Android 10 বা তার পরের version প্রয়োজন।"
            );
        }
    }

    @Override
    protected void onActivityResult(
            int requestCode,
            int resultCode,
            Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ROLE_REQUEST_CODE) {

            if (resultCode == RESULT_OK) {
                statusText.setText(
                        "✓ Call Screening চালু হয়েছে\n\n" +
                        "এখন incoming call শনাক্ত করার প্রস্তুতি সম্পূর্ণ।"
                );
            } else {
                statusText.setText(
                        "Call Screening চালু করা হয়নি।"
                );
            }
        }
    }
}
