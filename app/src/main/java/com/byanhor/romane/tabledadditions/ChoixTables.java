package com.byanhor.romane.tabledadditions;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.view.View;
import android.graphics.Typeface;
import android.content.Intent;


public class ChoixTables extends Activity {

    String operationChoice = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choix_tables);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Typeface useFont = Typeface.createFromAsset(getAssets(), "fonts/eraser.ttf");
        for (int i = 1; i <=9; i++)
        {
            int drawableResourceId = this.getResources().getIdentifier("textView"+i, "id", this.getPackageName());
            ((TextView)findViewById(drawableResourceId)).setTypeface(useFont);
        }
        ((TextView)findViewById(R.id.textViewAdditionOrMultiplication)).setTypeface(useFont);
        operationChoice = "+";

        // Set a checked change listener for switch button
        ((Switch)findViewById(R.id.switchPlusMult)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ((TextView)findViewById(R.id.textViewAdditionOrMultiplication)).setText(isChecked ? R.string.signemult : R.string.signeplus);
                operationChoice = isChecked ? "x" : "+";
            }
        });
    }

    final String EXTRA_OPERATION = "OPERATION";
    final String EXTRA_TABLEX = "TABLE";

    public void onClickX(View v, String i) {
        Intent tableDesXNewActivity = new Intent(this, TableDesX.class);
        tableDesXNewActivity.putExtra(EXTRA_OPERATION, operationChoice);
        tableDesXNewActivity.putExtra(EXTRA_TABLEX, i);
        startActivity(tableDesXNewActivity);
    }
    public void onClick1(View v) { onClickX(v, "1"); }
    public void onClick2(View v) { onClickX(v, "2"); }
    public void onClick3(View v) { onClickX(v, "3"); }
    public void onClick4(View v) { onClickX(v, "4"); }
    public void onClick5(View v) { onClickX(v, "5"); }
    public void onClick6(View v) { onClickX(v, "6"); }
    public void onClick7(View v) { onClickX(v, "7"); }
    public void onClick8(View v) { onClickX(v, "8"); }
    public void onClick9(View v) { onClickX(v, "9"); }

}
