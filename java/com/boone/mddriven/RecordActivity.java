package com.boone.mddriven;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.os.Environment;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.boone.mddriven.adapter.VoiceRecyclerViewAdapter;
import com.boone.mddriven.fragment.VoiceDialogFragment;
import com.boone.mddriven.model.Record;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecordActivity extends AppCompatActivity {
    private static final String TAG = RecordActivity.class.getName();

    @BindView(R.id.fabVoiceRecord)
    FloatingActionButton fabVoiceRecord;
    @BindView(R.id.recyclerViewRecords)
    RecyclerView recyclerViewRecords;
    @BindView(R.id.textViewNoRecord)
    TextView textViewNoRecord;

    List<File> files = new ArrayList<>();
    List<Record> records = new ArrayList<Record>();
    MediaMetadataRetriever mmr;
    private BottomSheetBehavior mBehavior;
    private BottomSheetDialog mBottomSheetDialog;
    private View bottom_sheet;
    private VoiceRecyclerViewAdapter voiceRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        ButterKnife.bind(this);
        checkPermission();

        fabVoiceRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VoiceDialogFragment dialog = new VoiceDialogFragment();
                dialog.show(getSupportFragmentManager(), "VoiceDialogFragment");
//                dialog.onDismiss(new DialogInterface() {
//                    @Override
//                    public void cancel() {
//
//                    }
//
//                    @Override
//                    public void dismiss() {
//                        voiceRecyclerViewAdapter.notifyDataSetChanged();
//                    }
//                });
            }
        });
        mmr = new MediaMetadataRetriever();
        files = getListFiles(new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                + "/Voice Recorder/"));

        if (files.size() == 0) {
            textViewNoRecord.setVisibility(View.VISIBLE);
        } else {
            initRecyclerView();
            mmr.release();
            bottom_sheet = findViewById(R.id.bottom_sheet);
            mBehavior = BottomSheetBehavior.from(bottom_sheet);
        }
    }

    private void checkPermission() {
        String[] PERMISSIONS = {
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
        };
        if ((ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED)
                || (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                || (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, 101);
        }
    }

    private void startPlayRecord(String time_date, String videoName) {
        Intent intent = new Intent(getApplicationContext(), RecordPlayActivity.class);
        intent.putExtra("time", time_date);
        intent.putExtra("record_name", videoName);
        getApplicationContext().startActivity(intent);
    }

    private void initRecyclerView() {
        textViewNoRecord.setVisibility(View.INVISIBLE);
        recyclerViewRecords.setVisibility(View.VISIBLE);
        voiceRecyclerViewAdapter = new VoiceRecyclerViewAdapter(getApplicationContext(), records, new VoiceRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Record record, int position) {
                //Toast.makeText(getContext(), record.getName(), Toast.LENGTH_LONG).show();
                showBottomSheetDialog(record, position);
            }
        });
        recyclerViewRecords.setAdapter(voiceRecyclerViewAdapter);
        recyclerViewRecords.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }

    private void showBottomSheetDialog(final Record record, final int position) {
        if (mBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }

        final View view = getLayoutInflater().inflate(R.layout.layout_sheet_list, null);

        ((View) view.findViewById(R.id.lyt_preview)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPlayRecord(record.getMinute(), record.getName());
                mBottomSheetDialog.dismiss();
            }
        });

        ((View) view.findViewById(R.id.lyt_share)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File deleteFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                        + "/Voice Recorder/" + record.getName() + ".m4a");
                if (deleteFile.exists()) {
                    deleteFile.delete();
                    files.remove(position);
                    voiceRecyclerViewAdapter.notifyItemRemoved(position);
                    voiceRecyclerViewAdapter.notifyItemRangeChanged(position, files.size());
                    voiceRecyclerViewAdapter.notifyDataSetChanged();
                    Toast.makeText(RecordActivity.this, "Record Deleted", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(RecordActivity.this, "Something wrong", Toast.LENGTH_LONG).show();
                }
                mBottomSheetDialog.dismiss();
            }
        });

        mBottomSheetDialog = new BottomSheetDialog(RecordActivity.this);
        mBottomSheetDialog.setContentView(view);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mBottomSheetDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        mBottomSheetDialog.show();
        mBottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                mBottomSheetDialog = null;
            }
        });
    }

    private String getDuration(String filename) {
        mmr.setDataSource(Environment.getExternalStorageDirectory().getPath() + "/Voice Recorder/" + filename);
        long durationMs = Long.parseLong(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
        long duration = durationMs / 1000;
        long h = duration / 3600;
        long m = (duration - h * 3600) / 60;
        long s = duration - (h * 3600 + m * 60);
        String durationValue;
        if (h == 0) {
            durationValue = String.format("00:%d:%d", m, s);
        } else {
            durationValue = String.format("%d:%d:%d", h, m, s);
        }

        return durationValue;
    }

    private List<File> getListFiles(File parentDir) {
        ArrayList<File> inFiles = new ArrayList<File>();
        if (parentDir.exists()) {
            File[] files = parentDir.listFiles();
            for (File file : files) {
                if (file.isDirectory()) {
                    inFiles.addAll(getListFiles(file));
                } else {
                    if (file.getName().endsWith(".m4a")) {
                        inFiles.add(file);
                        records.add(new Record(file.getName().toString().replace(".m4a", ""), getDuration(file.getName().toString())));
                    }
                }
            }
        }
        return inFiles;
    }
}
