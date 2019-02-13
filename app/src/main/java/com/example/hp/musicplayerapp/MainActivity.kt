package com.example.hp.musicplayerapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import java.util.ArrayList
import java.util.Collections
import java.util.Comparator
import android.net.Uri
import android.content.ContentResolver
import android.database.Cursor
import com.example.hp.musicplayerapp.MOdels.Song
import com.example.hp.musicplayerapp.MOdels.SongAdapter
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private lateinit var songsArray: ArrayList<Song>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        songsArray = ArrayList()
        getSongList()

        Collections.sort(songsArray,
            Comparator<Song> { a, b -> a.songName.compareTo(b.songName) })
        val songAdpt= SongAdapter(this,songsArray)
        songList.adapter= songAdpt
    }
    fun getSongList(){
        val musicResolver = contentResolver
        val musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val musicCursor= musicResolver.query(musicUri,null,null,null,null)

        if(musicCursor!=null && musicCursor.moveToFirst()){
            //obtener la informacion del cursor
            val titleIndex= musicCursor.getColumnIndex(android.provider.MediaStore.Audio.Media.TITLE)
            val idIndex= musicCursor.getColumnIndex(android.provider.MediaStore.Audio.Media._ID)
            val authorIndex= musicCursor.getColumnIndex(android.provider.MediaStore.Audio.Media.ARTIST)
            //utilizar la informacion para crear nuevas Canciones
            do {
                val id= musicCursor.getLong(idIndex)
                val title= musicCursor.getString(titleIndex)
                val author= musicCursor.getString(authorIndex)
                val songToAdd = Song(id,title,author)
                songsArray.add(songToAdd)
            }while (musicCursor.moveToNext())
        }
    }
}
