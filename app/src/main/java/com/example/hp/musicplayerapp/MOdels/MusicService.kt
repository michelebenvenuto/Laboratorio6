package com.example.hp.musicplayerapp.MOdels

import android.app.Service
import android.content.ContentUris
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import android.os.PowerManager
import android.util.Log;
import java.lang.Exception

class MusicService( ): Service(), MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener  {
    private lateinit var player: MediaPlayer
    private lateinit var songs: ArrayList<Song>
    var songPosition: Int=0
    private var musicBind = MusicBinder()
    override fun onPrepared(mp: MediaPlayer?) {
        mp!!.start()
    }

    override fun onError(mp: MediaPlayer?, what: Int, extra: Int): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCompletion(mp: MediaPlayer?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onBind(intent: Intent?): IBinder? {
        return musicBind
    }

    override fun onUnbind(intent: Intent?): Boolean {
        player.stop()
        player.release()
        return false
    }
    override fun onCreate(){
        super.onCreate()
        songPosition=0
        player= MediaPlayer()
        initMusicPlayer()
        musicBind=MusicBinder()
    }
    fun initMusicPlayer(){
        player.setWakeMode(applicationContext,PowerManager.PARTIAL_WAKE_LOCK)
        player.setAudioStreamType(AudioManager.STREAM_MUSIC)
        player.setOnPreparedListener(this)
        player.setOnCompletionListener(this)
        player.setOnErrorListener(this)
    }
    fun setList(theSongs: ArrayList<Song>){
        songs=theSongs
    }
    fun playSong(){
        player.reset()
        var playSong=songs.get(songPosition)

        var currSong=playSong.id

        var trackUri=ContentUris.withAppendedId(android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,currSong)
        try {
            player.setDataSource(applicationContext,trackUri)
        }catch (e: Exception){
            Log.e("MUSIC SERVICE", "Error setting data source", e)
        }
        player.prepareAsync()
    }
    fun setSong(songIndex:Int){
        songPosition=songIndex
    }

    inner class MusicBinder : Binder() {
        internal val service: MusicService
            get() = this@MusicService
    }
}