package com.example.sangeet;

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
      ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.listView);

        Dexter.withContext(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        Toast.makeText(MainActivity.this, "External storage permission given", Toast.LENGTH_SHORT).show();
                        ArrayList<File> mySongs = fetchSong(Environment.getExternalStorageDirectory());
                        String [] Items = new String[mySongs.size()];
                        for(int i=0 ; i<mySongs.size() ; i++)
                        {
                            Items[i] = mySongs.get(i).getName().replace(".mp3","");
                        }
                        ArrayAdapter<String> ad = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1,Items);
                         listView.setAdapter(ad);

                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                                String currentSong = listView.getItemAtPosition(position).toString();
                                intent.putExtra("songList",mySongs);
                                intent.putExtra("currentSong",currentSong);
                                intent.putExtra("position",position);
                                startActivity(intent);
                                Toast.makeText(MainActivity.this, "Playing the selected music", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                          permissionToken.continuePermissionRequest();
                    }
                })
                .check();
    }
       public ArrayList<File> fetchSong(File file)
       {
           ArrayList arrayList = new ArrayList();
           File[] songs = file.listFiles();
           if(songs!=null)
           {
               for(File myFile : songs)
               {
                   if(!myFile.isHidden() && myFile.isDirectory())
                   {
                       arrayList.addAll(fetchSong(myFile));
                   }
                   else
                   {
                       if(myFile.getName().endsWith(".mp3") && !myFile.getName().startsWith("."))
                       {
                           arrayList.add(myFile);
                       }
                   }
               }
           }
           return arrayList;
       }
}