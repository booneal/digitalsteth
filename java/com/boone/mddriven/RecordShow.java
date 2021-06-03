package com.boone.mddriven;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class RecordShow extends Activity {
    private LineGraphSeries<DataPoint> mData;

    private static final int SCALE_Y_MAXIMUM = 50;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_show);

        final GraphView graphDisplay = (GraphView) findViewById(R.id.graphViewDisplay);

        // Obtain filenames from MainActivity
        String fileNames = getIntent().getExtras().getString("files");
        String[] files = fileNames.split(",");
        int maxGraphLength = 0;

        // Create individual graph for each file
        for(String f: files) {
            String[] dataPoints = readFromFile(f).split(",");
            mData = new LineGraphSeries<>();
            for(int i = 0; i < dataPoints.length - 1; i++) {
                DataPoint dp = new DataPoint(i, Double.parseDouble(dataPoints[i]));
                // Log.d("Data Point", i + ": " + dp.getX() + "/" + dp.getY());
                mData.appendData(dp,false,dataPoints.length,false);

            }
            // optionally add data to graph
            mData.setColor(Color.BLUE);
            mData.setThickness(1);
            // COMMENT/UNCOMMENT NEXT LINE TO DISPLAY ALL THE INDIVIDUAL GRAPHS
            graphDisplay.addSeries(mData);
            // Remember maxGraphLength for later use
            if(dataPoints.length > maxGraphLength) {
                maxGraphLength = dataPoints.length;
            }
        }

        // Combined graph for selected files
        Double[] combinedData = new Double[maxGraphLength];
        Integer[] numberOfDataPoints = new Integer[maxGraphLength];
        for(int index = 0; index < maxGraphLength; index++) {
            combinedData[index] = 0d;
            numberOfDataPoints[index] = 0;
        }

        // Add all data point values and store the number of data points at given position i
        for(String f: files) {
            String[] dataPoints = readFromFile(f).split(",");
            for(int i = 0; i < dataPoints.length - 1; i++) {
                if(dataPoints[i] != null) {
                    combinedData[i] += Double.parseDouble(dataPoints[i]);
                    numberOfDataPoints[i] += 1;
                }
            }
        }
        mData = new LineGraphSeries<>();
        Double value = 0d;
        // Calculate combined value by adding up the values and dividing by the number of points at given position j
        for(int j = 0; j < combinedData.length - 1; j++) {
            value = combinedData[j] / numberOfDataPoints[j];
            DataPoint dp = new DataPoint(j, value);
            mData.appendData(dp,false,combinedData.length,false);
        }
        // add data to graph
        mData.setColor(Color.GREEN);
        mData.setThickness(3);
        graphDisplay.addSeries(mData);

        // GraphView specific settings
        graphDisplay.getViewport().setXAxisBoundsManual(true);
        graphDisplay.getViewport().setMinX(0);
        graphDisplay.getViewport().setMaxX(maxGraphLength);
        graphDisplay.getViewport().setYAxisBoundsManual(true);
        graphDisplay.getViewport().setMinY(-SCALE_Y_MAXIMUM);
        graphDisplay.getViewport().setMaxY(SCALE_Y_MAXIMUM);
        graphDisplay.getViewport().setScrollable(true);
        graphDisplay.getViewport().setScrollableY(false);
        graphDisplay.getViewport().setScalable(true);
        graphDisplay.getViewport().setScalableY(false);
    }

    private String readFromFile(String fileName) {

        String ret = "";

        try {
            InputStream inputStream = getApplication().openFileInput(fileName);

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("RecordShow activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("RecordShow activity", "Can not read file: " + e.toString());
        }

        return ret;
    }
}