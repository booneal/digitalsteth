package com.boone.mddriven;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;



public class RecordNew extends AppCompatActivity implements SaveFileDialog.SaveFileDialogListener {

    Button btn_startstop;
    Button btn_save;
    RealTimeUpdates realtimeUpdates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_new);

        btn_startstop = (Button) findViewById(R.id.btn_startstop);
        btn_save = (Button) findViewById(R.id.btn_save);
        realtimeUpdates = (RealTimeUpdates) getFragmentManager().findFragmentById(R.id.fragment_realtime);

        btn_startstop.setText("Start"); // fuck android for not recognizing text in button when clicked first without this line.
        btn_save.setEnabled(false);     // saving is only enabled once something has been recorded and recording is stopped

        btn_startstop.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(btn_startstop.getText() == "Start") {
                    btn_startstop.setText("Stop");
                    btn_save.setEnabled(false);
                    realtimeUpdates.startRecording();
                    // Start Recording
                } else {
                    btn_startstop.setText("Start");
                    btn_save.setEnabled(true);
                    realtimeUpdates.stopRecording();
                    // Stop Recording
                }
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(btn_startstop.getText() == "Start") {
                    // Allow saving only when stopped
                    DialogFragment newFragment = new SaveFileDialog();
                    newFragment.show(getSupportFragmentManager(), "SaveFile");
                }
            }
        });

    }

    public void onDialogPositiveClick(DialogFragment dialog) {
        // User touched the dialog's positive button
        EditText editText = (EditText) dialog.getDialog().findViewById(R.id.et_filename);
        String filename = "";
        if (editText != null) {
            Log.e("", "Value is: " + editText.getText());
            filename = editText.getText().toString();
        } else {
            Log.e("", "EditText not found!");
        }
        realtimeUpdates.saveData(filename);
        finish();
    }

    public void onDialogNegativeClick(DialogFragment dialog) {
        // User touched the dialog's negative button

    }

}