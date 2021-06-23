package com.example.playeractivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;

public class songList extends AppCompatActivity {

    RecyclerView myListViewSong;
    String[] items;
    MyAdaptor myAdaptor;
    public static ArrayList<File> mySong=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_list);
        myListViewSong=findViewById(R.id.mysongList);
        runTimePermission();

    }

    public void runTimePermission()
    {
        Dexter.withContext(this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
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
                           permissionToken.continuePermissionRequest();
                    }
                }).check();
    }

    public ArrayList<File> findSongInDevice(File file)
    {
        ArrayList<File> arrayList=new ArrayList<>();
        File [] files=file.listFiles();
        for(File singleFile: files)
        {
            if(singleFile.isDirectory() && !singleFile.isHidden())
            {
                arrayList.addAll(findSongInDevice(singleFile));
            }
            else
            {
                if(singleFile.getName().endsWith(".mp3") || singleFile.getName().endsWith(".wav")){
                    arrayList.add(singleFile);
                }
            }

        }
        return arrayList;
    }

    void display()
    {
        mySong=findSongInDevice(Environment.getExternalStorageDirectory());
        items=new String[mySong.size()];
        for(int i=0;i<mySong.size();i++)
        {
            items[i]=mySong.get(i).getName().toString().replace(".mp3","").replace(".wav","");

        }

        myAdaptor=new MyAdaptor(getApplicationContext(),mySong);
        myListViewSong.setLayoutManager(new LinearLayoutManager(songList.this));
        myListViewSong.setAdapter(myAdaptor);

//        ArrayAdapter<String> myAdaptor=new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item,items);
//        myListViewSong.setAdapter(myAdaptor);




        //listView item Listenr
//        myListViewSong.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                //song tittle
//                String songTittle= (String) myListViewSong.getItemAtPosition(position);
//                startActivity(new Intent(getApplicationContext(),MainActivity.class)
//                .putExtra("song",mySong)
//                        .putExtra("SongTittle",songTittle)
//                .putExtra("pos",position));
//            }
//        });
    }


}