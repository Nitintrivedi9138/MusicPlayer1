package com.example.music;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView songsList;
    String[] items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        songsList = findViewById(R.id.songsList);
        runtimePermission();

    }

    public void runtimePermission(){
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        display();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest(); //If user denied then it will request again
                    }
                }).check();
    }

    public ArrayList<File> findsongs(File file){
        ArrayList<File> arrayList = new ArrayList<>();
        File[] files = file.listFiles();

        for(File singlefile:files){
            if(singlefile.isDirectory() && !singlefile.isHidden())
            {
                arrayList.addAll(findsongs(singlefile));
            }
            else {
                if(singlefile.getName().endsWith(".mp3") || singlefile.getName().endsWith(".wav")){
                    arrayList.add(singlefile);
                }
            }
        }
        return arrayList;
    }

    void display(){
        final ArrayList<File> mySongs = findsongs(Environment.getExternalStorageDirectory());
            items = new String[mySongs.size()];
                for (int i = 0; i <= mySongs.size(); i++) {

                    items[i] = mySongs.get(i).getName().replace(".mp3", "")
                            .replace(".wav", "");
                }


        ArrayAdapter<String> myAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        songsList.setAdapter(myAdapter);

        songsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    String songName = songsList.getItemAtPosition(position).toString();
                    startActivity(new Intent(getApplicationContext(), Player.class)
                    .putExtra("songs", mySongs)
                    .putExtra("songName", songName));
            }
        });
    }
}

