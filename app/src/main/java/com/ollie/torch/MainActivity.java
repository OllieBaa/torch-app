package com.ollie.torch;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/** A huge toggle button, plus a shortcut to where you pick this app as the assistant. */
public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int pad = dp(24);
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(pad, pad * 2, pad, pad);
        root.setBackgroundColor(Color.BLACK);

        Button toggle = new Button(this);
        toggle.setText("Torch on / off");
        toggle.setTextSize(28);
        toggle.setOnClickListener(v -> Torch.toggle(this));
        root.addView(toggle, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 0, 1f));

        TextView help = new TextView(this);
        help.setText("To use the power button: tap below, choose \"Digital assistant app\", "
                + "and pick Torch. Then in Settings > System > Gestures > Press & hold power "
                + "button, choose \"Digital assistant\".");
        help.setTextColor(Color.LTGRAY);
        help.setTextSize(15);
        help.setGravity(Gravity.CENTER);
        help.setPadding(0, pad, 0, pad);
        root.addView(help);

        Button setup = new Button(this);
        setup.setText("Open default apps settings");
        setup.setOnClickListener(v ->
                startActivity(new Intent(Settings.ACTION_MANAGE_DEFAULT_APPS_SETTINGS)));
        root.addView(setup);

        setContentView(root);
    }

    private int dp(int value) {
        return Math.round(value * getResources().getDisplayMetrics().density);
    }
}
