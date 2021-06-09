package com.boone.mddriven;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.io.FileOutputStream;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;



public class RealTimeUpdates extends Fragment {
    private final Handler mHandler = new Handler(); // Handler for the thread's message queue
    private Runnable mTimer;    // Timer for continuous updates
    private LineGraphSeries<DataPoint> mData;   // Graph data points
    private double graphLastXValue = 0d;    // Graph initial x value = 0

    private static final int DISPLAY_X_VALUES_COUNT = 40;   // Number of values displayed on x-axis
    private static final int SCALE_Y_MAXIMUM = 50;     // Maximum absolute values on y-axis
    private float y = 0;
    private float height = 0;
    private String fileName = "";
    private String fileValues = "";
    private String date = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_graph, container, false);

        final SurfaceView mySurface = (SurfaceView) rootView.findViewById(R.id.surfaceViewRecord);
        final GraphView graphRecord = (GraphView) rootView.findViewById(R.id.graphViewRecord);

        WindowManager wm = (WindowManager) rootView.getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        Point size = new Point();
        display.getSize(size);
        int windowWidth = size.x;

        int surfaceHeight = mySurface.getHolder().getSurfaceFrame().height();
        mySurface.getHolder().setFixedSize(windowWidth/3, surfaceHeight);

        mData = new LineGraphSeries<>();
        graphRecord.addSeries(mData);
        graphRecord.getViewport().setXAxisBoundsManual(true);
        graphRecord.getViewport().setMinX(0);
        graphRecord.getViewport().setMaxX(DISPLAY_X_VALUES_COUNT);
        graphRecord.getViewport().setYAxisBoundsManual(true);
        graphRecord.getViewport().setMinY(-SCALE_Y_MAXIMUM);
        graphRecord.getViewport().setMaxY(SCALE_Y_MAXIMUM);

        mySurface.setOnTouchListener(new SurfaceView.OnTouchListener(
        ) {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                float temp_y = event.getY();
                height = mySurface.getHeight();

                temp_y *= -1;
                y = ((temp_y / height) + 0.5f) * 2 * SCALE_Y_MAXIMUM;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    y = 0;
                }

                if(y > SCALE_Y_MAXIMUM) { y = SCALE_Y_MAXIMUM;}
                if(y < -SCALE_Y_MAXIMUM) { y = -SCALE_Y_MAXIMUM;}


                return true;
            }
        });

        return rootView;

    }

    public void startRecording() {
        mTimer = new Runnable() {
            @Override
            public void run() {
                graphLastXValue += 1d;
                mData.appendData(new DataPoint(graphLastXValue, y), true, DISPLAY_X_VALUES_COUNT);
                mHandler.postDelayed(this, 100);
                fileValues += y +",";
            }
        };
        mHandler.postDelayed(mTimer, 100);
    }

    public void stopRecording() {
        onPause();
    }

    @Override
    public void onPause() {
        mHandler.removeCallbacks(mTimer);
        super.onPause();
    }

    public void saveData(String name) {
        if(name.equals("")) {
            DateFormat df = new SimpleDateFormat("yyyy_MM_d_HHmmss");
            date = df.format(Calendar.getInstance().getTime());
            fileName = "record_"+date;
        } else {
            fileName = "record_" + name;
        }

        FileOutputStream outputStream;

        try {
            outputStream = getActivity().openFileOutput(fileName, Context.MODE_PRIVATE);
            outputStream.write(fileValues.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}