package com.byanhor.romane.tabledadditions;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;

/**
 * Created by orhanda on 2/7/2018.
 */

public class UserStatsDisplay extends Activity {

    final String EXTRA_OPERATION = "OPERATION";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_stats_display);

        int drawableResourceId = this.getResources().getIdentifier("textViewStats", "id", this.getPackageName());
        TypedValue outValue = new TypedValue();
        getResources().getValue(R.dimen.size3, outValue, true);
        ((TextView) findViewById(drawableResourceId)).setTextSize(outValue.getFloat());

        /*Intent intent = getIntent();
        if (intent == null)
        {
            return;
        }*/

        String filename = getResources().getString(R.string.statsfilename);
        File file = new File(getFilesDir(), filename);
        StringBuilder strbuilder = new StringBuilder();
        try(BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                strbuilder.append(line);
                strbuilder.append('\n');
            }
        }
        catch (IOException e) {
            ((TextView) findViewById(drawableResourceId)).setText(e.toString());
        }
        ((TextView) findViewById(drawableResourceId)).setText((CharSequence)strbuilder);
    }
}
