package com.example.hp.musicplayerapp.MOdels

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.LinearLayout
import android.widget.TextView
import com.example.hp.musicplayerapp.R


class SongAdapter(var c: Context, var songsList: ArrayList<Song>) : BaseAdapter() {
    private var songsInflater =LayoutInflater.from(c)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val songLay = songsInflater.inflate(R.layout.song, parent, false) as LinearLayout
        val songView = songLay.findViewById(R.id.song_title) as TextView
        val artistView = songLay.findViewById(R.id.song_artist) as TextView
        val currSong = songsList.get(position)
        songView.setText(currSong.songName)
        artistView.setText(currSong.songAuthor)
        songLay.tag = position
        return songLay
    }

    override fun getItem(position: Int): Any {
        return 0
    }

    override fun getItemId(position: Int): Long {

        return 0
    }

    override fun getCount(): Int {
        return songsList.size
    }
}