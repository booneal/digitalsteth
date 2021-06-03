package com.boone.mddriven;

import android.app.Activity;
import android.app.LauncherActivity;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EKGActivity extends Activity {

    ListView mListView = null;
    File path = null;
    List<String> finalList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heart_sounds);

        Button btn_display = (Button) findViewById(R.id.btn_display);
        Button btn_record_new = (Button) findViewById(R.id.btn_record_new);
        Button btn_delete = (Button) findViewById(R.id.btn_delete);

        mListView = (ListView) findViewById(R.id.lv_files);
        mListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        updateList();

        btn_display.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String filesToLoad = "";
                SparseBooleanArray checked = mListView.getCheckedItemPositions();
                for (int i = 0; i < checked.size(); i++) {
                    if(checked.valueAt(i) == true) {
                        filesToLoad += "record_"+mListView.getItemAtPosition(checked.keyAt(i)) + ",";
                    }
                }

                Intent myIntent = new Intent(EKGActivity.this, RecordShow.class);
                myIntent.putExtra("files",filesToLoad);
                EKGActivity.this.startActivity(myIntent);
            }
        });

        btn_record_new.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(EKGActivity.this, RecordNew.class);
                EKGActivity.this.startActivity(myIntent);
            }
        });

        btn_delete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SparseBooleanArray checked = mListView.getCheckedItemPositions();
                for (int i = 0; i < checked.size(); i++) {
                    if(checked.valueAt(i)) {
                        File file = new File(path + "/record_" + mListView.getItemAtPosition(checked.keyAt(i)));
                        boolean deleted = file.delete();
                    }
                }
                updateList();
            }
        });
    }

    private void updateList() {
        path = getFilesDir();
        File[] files = path.listFiles();
        String[] names = new String[files.length];

        for (int i = 0; i < files.length; i++) {

            names[i] = (files[i].toString());
        }

        List<String> stringList = new ArrayList<String>(Arrays.asList(names));

        for (int i = stringList.size() - 1; i >= 0; i--) {
            if (!stringList.get(i).contains("record_")) {
                stringList.remove(i);
            }
        }

        int substrStart = path.toString().length();
        finalList = new ArrayList<String>();

        for (String s : stringList) {
            if (s.contains("record_")) {
                finalList.add(s.substring(substrStart + 8));
            }
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_multiple_choice,
                finalList);

        arrayAdapter.notifyDataSetChanged();
        mListView.setAdapter(arrayAdapter);
        for(int i = 0;i < mListView.getChildCount(); i++) {
            mListView.setItemChecked(i, false);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateList();
    }


}