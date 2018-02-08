package com.byanhor.romane.tabledadditions;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.TypedValue;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.DragShadowBuilder;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class TableDesX extends Activity implements View.OnTouchListener, View.OnDragListener, View.OnClickListener {
    String operation;
    String table;
    String other;
    int result;
    String resultDizaine;
    String resultUnite;
    MediaPlayer applaudissementsPlayer;
    //UserStats userStats;
    File statsFile;
    FileOutputStream statsStream;
    String statsAddition[][] = new String[10][10];
    String statsMultiplication[][] = new String[10][10];

    final String EXTRA_OPERATION = "OPERATION";
    final String EXTRA_TABLEX = "TABLE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_des_x);
        //userStats = new UserStats(this);

        StatsGeneration();

        applaudissementsPlayer = MediaPlayer.create(this, R.raw.applaudissements);

        Typeface useFont = Typeface.createFromAsset(getAssets(), "fonts/showg.ttf");
        for (int i = 0; i <= 9; i++) {
            int drawableResourceId = this.getResources().getIdentifier("textView" + i, "id", this.getPackageName());
            ((TextView) findViewById(drawableResourceId)).setTypeface(useFont);
            TypedValue outValue = new TypedValue();
            getResources().getValue(R.dimen.size2, outValue, true);
            ((TextView) findViewById(drawableResourceId)).setTextSize(outValue.getFloat());
        }
        ((TextView) findViewById(R.id.textViewPremierChiffre)).setTypeface(useFont);
        ((TextView) findViewById(R.id.textViewDeuxiemeChiffre)).setTypeface(useFont);
        ((TextView) findViewById(R.id.textViewSigne)).setTypeface(useFont);
        ((TextView) findViewById(R.id.textViewEgal)).setTypeface(useFont);
        ((TextView) findViewById(R.id.textViewResultatDizaine)).setTypeface(useFont);
        ((TextView) findViewById(R.id.textViewResultatUnite)).setTypeface(useFont);
        TypedValue outValue = new TypedValue();
        getResources().getValue(R.dimen.size1, outValue, true);
        ((TextView) findViewById(R.id.textViewPremierChiffre)).setTextSize(outValue.getFloat());
        ((TextView) findViewById(R.id.textViewDeuxiemeChiffre)).setTextSize(outValue.getFloat());
        ((TextView) findViewById(R.id.textViewSigne)).setTextSize(outValue.getFloat());
        ((TextView) findViewById(R.id.textViewEgal)).setTextSize(outValue.getFloat());
        ((TextView) findViewById(R.id.textViewResultatDizaine)).setTextSize(outValue.getFloat());
        ((TextView) findViewById(R.id.textViewResultatUnite)).setTextSize(outValue.getFloat());

        Intent intent = getIntent();
        if (intent == null) return;

        operation = intent.getStringExtra(EXTRA_OPERATION);
        table = intent.getStringExtra(EXTRA_TABLEX);
        ((TextView) findViewById(R.id.textViewSigne)).setText(operation);
        if (operation.equals("x"))
            ((TextView) findViewById(R.id.textViewSigne)).setTextSize(0.5f * outValue.getFloat());

        int drawableResourceId = this.getResources().getIdentifier("textView" + table, "id", this.getPackageName());
        TextView myTextView = (TextView) findViewById(drawableResourceId);
        myTextView.setTextColor(ContextCompat.getColor(this, R.color.colorAccent));

        TextView premierChiffre = (TextView) findViewById(R.id.textViewPremierChiffre);
        premierChiffre.setText(table);

        for (int i = 0; i <= 9; i++) {
            drawableResourceId = this.getResources().getIdentifier("textView" + i, "id", this.getPackageName());
            findViewById(drawableResourceId).setOnTouchListener(this);
        }

        findViewById(R.id.textViewResultatDizaine).setOnDragListener(this);
        findViewById(R.id.textViewResultatUnite).setOnDragListener(this);

        ((LinearLayout) findViewById(R.id.imageLayout)).setClickable(false);
        ((LinearLayout) findViewById(R.id.imageLayout)).setOnClickListener(this);
        ((TextView) findViewById(R.id.textViewEgal)).setClickable(true);
        ((TextView) findViewById(R.id.textViewEgal)).setOnClickListener(this);

        random_operation();
    }

    public void random_operation() {
        if (applaudissementsPlayer.isPlaying()) {
            applaudissementsPlayer.stop();
            applaudissementsPlayer.reset();
        }

        ((LinearLayout) findViewById(R.id.imageLayout)).setBackgroundResource(R.mipmap.waiting);
        ((LinearLayout) findViewById(R.id.imageLayout)).setClickable(false);

        Random rand = new Random();

        int pound[] = new int[10];
        for (int c = 0; c <= 9; c++) {
            String cur = (operation.equals("+") ? statsAddition[Integer.parseInt(table)][c] : statsMultiplication[Integer.parseInt(table)][c]);
            List<String> list = new ArrayList<>(Arrays.asList(cur.split("/")));
            float a = new Float(list.get(0));
            float b = new Float(list.get(1));
            float ratio = a / b;
            Float incr = new Float(100.0 - 90.0 * ratio);
            pound[c] = (c == 0 ? 0 : pound[c - 1]) + incr.intValue();
        }
        int r = rand.nextInt(pound[9]);
        int c = 0;
        while (r > pound[c]) c++;
        other = String.valueOf(c);

        TextView deuxiemeChiffre = (TextView) findViewById(R.id.textViewDeuxiemeChiffre);
        deuxiemeChiffre.setText(other);
        result = (operation.equals("+") ? Integer.parseInt(table) + Integer.parseInt(other) : Integer.parseInt(table) * Integer.parseInt(other));
        findViewById(R.id.textViewResultatDizaine).setVisibility(result > 9 ? View.VISIBLE : View.INVISIBLE);
        ((TextView) findViewById(R.id.textViewResultatDizaine)).setText("?");
        ((TextView) findViewById(R.id.textViewResultatDizaine)).setAlpha(0.4f);
        ((TextView) findViewById(R.id.textViewResultatUnite)).setText("?");
        ((TextView) findViewById(R.id.textViewResultatUnite)).setAlpha(0.4f);
        /*if (operation.equals("+"))
            ((TextView) findViewById(R.id.textViewDebug)).setText(statsAddition[Integer.parseInt(table)][Integer.parseInt(other)]);
        else
            ((TextView) findViewById(R.id.textViewDebug)).setText(statsMultiplication[Integer.parseInt(table)][Integer.parseInt(other)]);*/
        resultDizaine = "";
        resultUnite = "";
    }

    @Override
    public boolean onDrag(View dstView, DragEvent event) {
        int action = event.getAction();
        switch (event.getAction()) {
            case DragEvent.ACTION_DRAG_STARTED:
                // do nothing
                break;
            case DragEvent.ACTION_DRAG_ENTERED:
                //v.setBackgroundDrawable(enterShape);
                break;
            case DragEvent.ACTION_DRAG_ENDED:
            case DragEvent.ACTION_DRAG_EXITED:
                View srcView = (View) event.getLocalState();
                srcView.setVisibility(View.VISIBLE);
                break;
            case DragEvent.ACTION_DROP:
                // Dropped, reassign View to ViewGroup
                srcView = (View) event.getLocalState();
                ViewGroup srcViewGroup = (ViewGroup) srcView.getParent();
                String txtSrc = srcView.getResources().getResourceEntryName(srcView.getId());
                String txtDst = dstView.getResources().getResourceEntryName(dstView.getId());
                if (txtDst.equals("textViewResultatDizaine")) {
                    resultDizaine = txtSrc.replaceAll("textView", "");
                    ((TextView) dstView).setText(resultDizaine);
                    ((TextView) dstView).setAlpha(1.0f);
                } else if (txtDst.equals("textViewResultatUnite")) {
                    resultUnite = txtSrc.replaceAll("textView", "");
                    ((TextView) dstView).setText(resultUnite);
                    ((TextView) dstView).setAlpha(1.0f);
                }
                if ((result > 9 && !resultDizaine.equals("") && !resultUnite.equals("")) || (result <= 9 && !resultUnite.equals(""))) {
                    int userResult = Integer.parseInt(resultUnite) + (result > 9 ? 10 * Integer.parseInt(resultDizaine) : 0);
                    if (userResult == result) {
                        StatsSuccesOrNot(Integer.parseInt(table), Integer.parseInt(other), true);
                        ((LinearLayout) findViewById(R.id.imageLayout)).setBackgroundResource(R.mipmap.good);
                        ((LinearLayout) findViewById(R.id.imageLayout)).setClickable(true);
                        if (!applaudissementsPlayer.isPlaying()) {
                            applaudissementsPlayer.release();
                            applaudissementsPlayer = MediaPlayer.create(this, R.raw.applaudissements);
                            applaudissementsPlayer.start();
                        }
                    } else {
                        StatsSuccesOrNot(Integer.parseInt(table), Integer.parseInt(other), false);
                        ((LinearLayout) findViewById(R.id.imageLayout)).setBackgroundResource(R.mipmap.bad);
                        resultDizaine = "";
                        resultUnite = "";
                        ((TextView) findViewById(R.id.textViewResultatDizaine)).setText("?");
                        ((TextView) findViewById(R.id.textViewResultatDizaine)).setAlpha(0.4f);
                        ((TextView) findViewById(R.id.textViewResultatUnite)).setText("?");
                        ((TextView) findViewById(R.id.textViewResultatUnite)).setAlpha(0.4f);
                    }
                }
                break;
            default:
                break;
        }
        return true;
    }

    float dX, dY;
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            ClipData data = ClipData.newPlainText("", "");
            DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
            view.startDrag(data, shadowBuilder, view, 0);
            view.setVisibility(View.INVISIBLE);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.textViewEgal:
                Intent userStatsDisplayNewActivity = new Intent(this, UserStatsDisplay.class);
                startActivity(userStatsDisplayNewActivity);
                break;
            default:
                random_operation();
                break;
        }
    }

    public void StatsSuccesOrNot(int l, int c, boolean ok) {
        if (operation.equals("+")) {
            String statsbefore = statsAddition[l][c];
            List<String> list = new ArrayList<>(Arrays.asList(statsbefore.split("/")));
            statsAddition[l][c] = Integer.parseInt(list.get(0)) + (ok ? 1 : 0) + "/" + new Integer(Integer.parseInt(list.get(1)) + 1);
        } else {
            String statsbefore = statsMultiplication[l][c];
            List<String> list = new ArrayList<>(Arrays.asList(statsbefore.split("/")));
            statsMultiplication[l][c] = Integer.parseInt(list.get(0)) + (ok ? 1 : 0) + "/" + new Integer(Integer.parseInt(list.get(1)) + 1);
        }
        StatsGenerationWrite(false);
    }

    public void StatsGenerationRead() {
        StringBuilder text = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(statsFile.toString()));
            String line = br.readLine(); // skip line Tables d'additions
            line = br.readLine(); // skip line header
            for (int l = 1; l <= 9; l++) {
                line = br.readLine();
                line = line.replaceAll(" ", "");
                List<String> list = new ArrayList<>(Arrays.asList(line.split("\\|")));
                for (int c = 0; c < 10; c++) statsAddition[l][c] = list.get(c + 1);
            }
            line = br.readLine(); // Tables de multiplications
            line = br.readLine(); // skip line header
            for (int l = 1; l <= 9; l++) {
                line = br.readLine();
                line = line.replaceAll(" ", "");
                List<String> list = new ArrayList<>(Arrays.asList(line.split("\\|")));
                for (int c = 0; c < 10; c++) statsMultiplication[l][c] = list.get(c + 1);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void StatsGenerationWrite(boolean init) {
        try {
            statsStream = new FileOutputStream(statsFile, false);
            PrintStream printStream = new PrintStream(statsStream);
            printStream.println("Tables d\'additions");
            printStream.println("  +  |  0  |  1  |  2  |  3  |  4  |  5  |  6  |  7  |  8  |  9  |");
            for (int l = 1; l <= 9; l++) {
                printStream.format("  %d  |", l);
                for (int c = 0; c <= 9; c++) {
                    if (init)
                        printStream.format("  %d/%d |", 1, 1);
                    else
                        printStream.format("  %s |", statsAddition[l][c]);
                }
                printStream.println("");
            }
            printStream.println("Tables de multiplications");
            printStream.println("  x  |  0  |  1  |  2  |  3  |  4  |  5  |  6  |  7  |  8  |  9  |");
            for (int l = 1; l <= 9; l++) {
                printStream.format("  %d  |", l);
                for (int c = 0; c <= 9; c++) {
                    if (init)
                        printStream.format("  %d/%d |", 1, 1);
                    else
                        printStream.format("  %s |", statsMultiplication[l][c]);
                }
                printStream.println("");
            }
            printStream.close();
            if (statsStream != null)
                statsStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void StatsGeneration() {
        String folder = getApplicationContext().getFilesDir().getAbsolutePath();
        File statsFolder = new File(folder);
        if (!statsFolder.exists())
            statsFolder.mkdirs();
        statsFile = new File(folder + File.separator + getResources().getString(R.string.statsfilename));
        if (!statsFile.exists())
            StatsGenerationWrite(true);
        StatsGenerationRead();
    }
}



