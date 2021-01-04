
//Name: Sonia Mubasher
//Student ID: 20129528


package com.example.painting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private static final int request_code1 = 1; //for brush activity
    private static final int request_code2 = 2; //for color activity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent(); //Implicit intent
        FingerPainterView paintingView = (FingerPainterView) findViewById(R.id.viewPaint);
        paintingView.load(intent.getData()); //getting the image selected from user


        //to change colour
        Button button1 = (Button) findViewById(R.id.buttonClr);
        //to change brush size and shape
        Button button2 = (Button) findViewById(R.id.buttonSize);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(MainActivity.this, ClrActivity.class); //to open colour activity when user clicks on clr button
                startActivityForResult(intent1, request_code2); //start the colour activity
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(MainActivity.this, BrushActivity.class);
                startActivityForResult(intent2, request_code1); //start the brush activity

            }
        });
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == request_code1) &&
                (resultCode == RESULT_OK)) {
            TextView textView1 = (TextView) findViewById(R.id.sel);
            String returnString1 = data.getExtras().getString("returnData1"); //getting width from brush activity
            textView1.setText(" Brush Width: " + returnString1); // displaying it on main activity
            int width = Integer.valueOf(returnString1);
            FingerPainterView paintingView = (FingerPainterView) findViewById(R.id.viewPaint);
            paintingView.setBrushWidth(width); //calling the setbrushwidth function from fingerpainter view to set the width of brush
            TextView textView2 = (TextView) findViewById(R.id.shp);
            String returnString2 = data.getExtras().getString("returnData2"); //getting shape from brush activity
            textView2.setText("Brush Shape: " + returnString2); //displaying it on main activity
            if (returnString2.equals("round")) {
                paintingView.setBrush(Paint.Cap.ROUND); //if user entered round set the brush to round
            } else {
                paintingView.setBrush(Paint.Cap.SQUARE); //else set it to square
            }


        } else {
            TextView textView3 = (TextView) findViewById(R.id.currentClr);
            int returnString3 = data.getExtras().getInt("returnData3"); //getting colour from colour activity
            textView3.setText("Current Colour: " + Integer.toHexString(returnString3)); //displaying it on main activity
            FingerPainterView paintingView = (FingerPainterView) findViewById(R.id.viewPaint);
            paintingView.setCurrentColor(returnString3); //calling set current colour function from finger painter view
        }
    }

    public void onClick(View view) { //on click for clr button
    }

    public void onClick1(View view) { //on click for brush button
    }

}
