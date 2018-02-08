package com.byanhor.romane.tabledadditions;

import android.content.Context;
import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintStream;

/**
 * Created by orhanda on 1/25/2018.
 */

public class UserStats {

    FileOutputStream outputStream;
    PrintStream printStream;
    int tableauSuccess[][] = new int[10][10];
    int tableauTry[][] = new int[10][10];

    public UserStats(Context context) {

        String filename = context.getResources().getString(R.string.statsfilename);

        File file = new File(context.getFilesDir(), filename);
        if (!file.exists())
            FirstStatsGeneration(context, filename);

        if (!file.exists())
            FirstStatsGeneration(context, filename);

        try(BufferedReader br = new BufferedReader(new FileReader(file))) {
            int l = 0;
            for (String line; (line = br.readLine()) != null; ) {
                String[] tokens1 = line.split("[|]");
                for (int c = 0; c <= 9; c++) {
                    String[] tokens2 = tokens1[c].split("[/]");
                    tableauSuccess[l][c] = Integer.parseInt(tokens2[0]);
                    tableauTry[l][c] = Integer.parseInt(tokens2[1]);
                }
                l++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void FirstStatsGeneration(Context context, String filename) {

        File contextPath = context.getDir("stats", Context.MODE_PRIVATE);
        File fileWithinContextPath = new File(contextPath, filename);
        try {
            outputStream = new FileOutputStream(fileWithinContextPath);
            printStream = new PrintStream(outputStream);
            printStream.println("Tables d\'additions");
            printStream.println("  +  |  0  |  1  |  2  |  3  |  4  |  5  |  6  |  7  |  8  |  9  |");
            for (int l = 1; l <= 9; l++) {
                printStream.format("  %d  |", l);
                for (int c = 0; c <= 9; c++) {
                    printStream.format("  %d / %d |", 0, 0);
                }
                printStream.println("");
            }
            printStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
