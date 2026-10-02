package com.example.bengaliaicallassistant;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    private EditText trustedNumberInput;
    private TextView trustedListText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
                "এই নম্বরগুলো থেকে কল এলে AI screening bypass করার জন্য trusted হিসেবে রাখা হবে."
        );
        info.setTextColor(Color.LTGRAY);
        info.setTextSize(15);
        info.setPadding(0, 10, 0, 15);

        root.addView(info);

        trustedNumberInput = new EditText(this);
        trustedNumberInput.setHint("Add your trusted number");
        trustedNumberInput.setHintTextColor(Color.GRAY);
        trustedNumberInput.setTextColor(Color.WHITE);
        trustedNumberInput.setInputType(
                android.text.InputType.TYPE_CLASS_PHONE
        );

        root.addView(trustedNumberInput);

        Button addButton = new Button(this);
        addButton.setText("ADD TRUSTED NUMBER");

        root.addView(addButton);

        trustedListText = new TextView(this);
        trustedListText.setText(
                "\nTrusted numbers:\n\nNo trusted numbers added yet."
        );
        trustedListText.setTextColor(Color.WHITE);
        trustedListText.setTextSize(16);
        trustedListText.setPadding(0, 20, 0, 20);

        root.addView(trustedListText);

        addButton.setOnClickListener(v -> {

            String number =
                    trustedNumberInput.getText().toString().trim();

            if (number.isEmpty()) {
                Toast.makeText(
                        this,
                        "একটি ফোন নম্বর দিন",
                        Toast.LENGTH_SHORT
                ).show();
                return;
            }

            trustedListText.setText(
                    "\nTrusted numbers:\n\n✓ " + number
            );

            trustedNumberInput.setText("");

            Toast.makeText(
                    this,
                    "Trusted number added",
                    Toast.LENGTH_SHORT
            ).show();
        });

        setContentView(root);
    }
}
