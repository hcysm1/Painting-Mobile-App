/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.painting;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;


/**
 * Created by Dr Martin Flintham, School of Computer Science, University of Nottingham, UK.
 * <p>
 * Derived from android graphics API sample com.example.android.apis.graphics.Fingerpaint
 * android.googlesource.com/platform/development/+/master/samples/ApiDemos/src/com/example/android/apis/graphics/FingerPaint.java
 */

//Name: Sonia Mubasher
//Student ID: 20129528

public class FingerPainterView extends View {

    private Context context;
    private Canvas canvas;
    private Paint paint;
    private Bitmap bitmap;
    private Path path;
    private Uri uri;
    private ArrayList<Path> paths = new ArrayList<>(); //to store multiple paths
    private ArrayList<Integer> widths = new ArrayList<>(); //to store array of widths
    private ArrayList<Integer> colors = new ArrayList<>(); //to store array of colors
    private int currentColor = 0xFF000000; //current color of brush
    private int currentWidth; //current width of brush
    private Paint.Cap currentBrush = Paint.Cap.BUTT; //current brush cap

    public FingerPainterView(Context context) {
        super(context); // application context
        init(context);
    }

    public FingerPainterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public FingerPainterView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        this.context = context;

        path = new Path();
        paint = new Paint();

        // default brush style and colour
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setARGB(255, 0, 0, 0);
    }

    public void setBrush(Paint.Cap brush) {
        this.currentBrush = brush; //to set the shape of brush
    }

    public void setBrushWidth(int width) { //to set the brush width
        this.currentWidth = width; //to set the width of brush
    }

    public void setCurrentColor(int colour) { //to set the current colour
        this.currentColor = colour; //to set the colour of brush
    }

    public void load(Uri uri) {
        this.uri = uri; //to save the uri of image selected by user
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        // save superclass view state
        bundle.putParcelable("superState", super.onSaveInstanceState());

        try {
            // save bitmap to temporary cache file to overcome binder transaction size limit
            File f = File.createTempFile("fingerpaint", ".png", context.getCacheDir());
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, new FileOutputStream(f));
            // save temporary filename to bundle
            bundle.putString("tempfile", f.getAbsolutePath());
        } catch (IOException e) {
            Log.e("FingerPainterView", e.toString());
        }
        return bundle;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;

            try {
                // load cache file from bundle stored filename
                File f = new File(bundle.getString("tempfile"));
                Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
                // need to copy the bitmap to create a mutable version
                bitmap = b.copy(b.getConfig(), true);
                b.recycle();
                f.delete();
            } catch (IOException e) {
                Log.e("FingerPainterView", e.toString());
            }

            state = bundle.getParcelable("superState");
        }
        super.onRestoreInstanceState(state);
    }

    public void addPath(Path path) {
        paths.add(path); //adding paths to the drawing app for different widths, shapes and colours
        widths.add(currentWidth); //adding different width
        colors.add(currentColor); //adding different colours
    }

    public Path getLastPath() {
        if (paths.size() > 0) { //checking if we reached the end
            return paths.get(paths.size() - 1);
        }

        return null;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.TRANSPARENT);

        int i = 0;
        for (Path path : paths) {
            paint = new Paint();
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(widths.get(i)); //setting width
            paint.setStrokeCap(currentBrush); //setting shape
            paint.setColor(colors.get(i)); //setting colour
            canvas.drawBitmap(bitmap, 0, 0, paint);
            canvas.drawPath(path, paint);
            i++;
        }

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {


        if (bitmap == null) {
            if (uri != null) {
                try {
                    // attempt to load the uri provided, scale to fit our canvas
                    InputStream stream = context.getContentResolver().openInputStream(uri);
                    Bitmap bm = BitmapFactory.decodeStream(stream);
                    bitmap = Bitmap.createScaledBitmap(bm, Math.max(w, h), Math.max(w, h), false);
                    stream.close();
                    bm.recycle();
                } catch (IOException e) {
                    Log.e("FingerPainterView", e.toString());
                }
            } else {
                // create a square bitmap so is drawable even after rotation to landscape
                bitmap = Bitmap.createBitmap(Math.max(w, h), Math.max(w, h), Bitmap.Config.ARGB_8888);
            }
        }
        canvas = new Canvas(bitmap);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                path.reset();
                path.moveTo(x, y);
                path.lineTo(x, y);
                addPath(path); //calling the add path function
                break;
            case MotionEvent.ACTION_MOVE:
                path = getLastPath();
                if (path != null) {
                    path.lineTo(x, y);
                }

                break;
            case MotionEvent.ACTION_UP:
                canvas.drawPath(path, paint);
                path.reset();

                break;
        }
        invalidate();
        return true;
    }
}