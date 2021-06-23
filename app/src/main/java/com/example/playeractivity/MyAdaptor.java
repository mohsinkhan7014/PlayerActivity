package com.example.playeractivity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;

public class MyAdaptor extends RecyclerView.Adapter<MusicHolder> {
    Context context;
    ArrayList<File> myMusicList;

    public MyAdaptor(Context context, ArrayList<File> myMusicList) {
        this.context = context;
        this.myMusicList = myMusicList;
    }

    @Override
    public MusicHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView= LayoutInflater.from(parent.getContext()).inflate(R.layout.my_music_view,parent,false);
        return new MusicHolder(mView);
    }

    @Override
    public void onBindViewHolder(MusicHolder holder, int position) {
        holder.songNameFormat.setText(songList.mySong.get(position).getName());


        holder.cardView.setOnClickListener(x->{

            //String songTittle= (String) myListViewSong.getItemAtPosition(position);
            context.startActivity(new Intent(context,MainActivity.class)
                    .putExtra("song",songList.mySong)
                    .putExtra("SongTittle",holder.songNameFormat.getText())
                    .putExtra("pos",position));
        });

    }

    @Override
    public int getItemCount() {
        if(myMusicList.size()>0)
        {
            return myMusicList.size();
        }
        else
        {
            return 1;
        }

    }
}
class MusicHolder extends RecyclerView.ViewHolder
{

    ImageView songImage;
    TextView  songNameFormat;

    CardView cardView;

    public MusicHolder(View itemView) {
        super(itemView);
        songImage=itemView.findViewById(R.id.imageMusic);
        songNameFormat=itemView.findViewById(R.id.nameDesc);
        cardView=itemView.findViewById(R.id.mycard);

    }
}
