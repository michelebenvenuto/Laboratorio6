package com.example.hp.musicplayerapp

import android.content.*
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import java.util.ArrayList
import java.util.Collections
import java.util.Comparator
import android.net.Uri
import android.database.Cursor
import android.view.View
import com.example.hp.musicplayerapp.MOdels.Song
import com.example.hp.musicplayerapp.MOdels.SongAdapter
import kotlinx.android.synthetic.main.activity_main.*
import android.widget.MediaController.MediaPlayerControl;
import com.example.hp.musicplayerapp.MOdels.MusicController
import com.example.hp.musicplayerapp.MOdels.MusicService
import com.example.hp.musicplayerapp.R.id.songList
import com.example.hp.musicplayerapp.MOdels.MusicService.MusicBinder
import android.os.IBinder
import android.content.ComponentName
import com.example.hp.musicplayerapp.R.id.songList
import android.content.ServiceConnection






class MainActivity : AppCompatActivity() {


    private lateinit var songsArray: ArrayList<Song>
    private lateinit var controller:MusicController
    private var musicSrv: MusicService?= null
    private var playIntent: Intent?=null
    private var musicBound=false

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

    //connect to the service
    private val musicConnection = object : ServiceConnection {

        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val binder = service as MusicBinder
            //get service
            musicSrv = binder.service
            //pass list
            musicSrv!!.setList(songsArray)
            musicBound = true
        }

        override fun onServiceDisconnected(name: ComponentName) {
            musicBound = false
        }
    }

    override fun onStart() {
        super.onStart()
        if (playIntent==null) {
            playIntent = Intent(this, MusicService::class.java)
            bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE)
            startService(playIntent)
        }
    }

    fun songPicked(view: View) {
        musicSrv!!.setSong(Integer.parseInt(view.tag.toString()))
        musicSrv!!.playSong()
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
