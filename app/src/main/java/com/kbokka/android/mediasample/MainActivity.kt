package com.kbokka.android.mediasample

import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import java.io.IOException
import java.lang.IllegalArgumentException
import java.net.URI

class MainActivity : AppCompatActivity() {

  private var _player: MediaPlayer? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    _player = MediaPlayer()
    val mediaFileUriStr = "android.resource://${packageName}/${R.raw.summer_mountain}"
    val mediaFileUri = Uri.parse(mediaFileUriStr)

    try {
      _player?.setDataSource(applicationContext, mediaFileUri)
      _player?.setOnPreparedListener(PlayPreparedListener())
      _player?.setOnCompletionListener(PlayCompletionListener())
      _player?.prepareAsync()
    } catch (ex: IllegalArgumentException) {
      Log.e("MediaSample", "メディアプレイヤー準備時の例外発生", ex)
    } catch (ex: IOException) {
      Log.e("MediaSample", "メディアプレイヤー準備時の例外発生", ex)
    }
  }

  fun onPlayButtonClick(view: View) {
    _player?.let {
      val btPlay = findViewById<Button>(R.id.btPlay)
      if (it.isPlaying) {
        it.pause()
        btPlay.setText(R.string.bt_play_play)
      } else {
        it.start()
        btPlay.setText(R.string.bt_play_pause)
      }
    }
  }

  fun onBackButtonClick(view: View) {
    _player?.seekTo(0)
  }

  fun onForwardButtonClick(view: View) {
    _player?.let {
      val duration = it.duration
      it.seekTo(duration)
      if (!it.isPlaying) {
        it.start()
      }
    }
  }

  override fun onDestroy() {
    super.onDestroy()

    _player?.let {
      if (it.isPlaying) {
        it.stop()
      }
      it.release()
    }
    _player = null
  }

  private inner class PlayPreparedListener : MediaPlayer.OnPreparedListener {
    override fun onPrepared(mp: MediaPlayer) {
      findViewById<Button>(R.id.btPlay).isEnabled = true
      findViewById<Button>(R.id.btBack).isEnabled = true
      findViewById<Button>(R.id.btForward).isEnabled = true
    }
  }

  private inner class PlayCompletionListener : MediaPlayer.OnCompletionListener {
    override fun onCompletion(mp: MediaPlayer) {
      findViewById<Button>(R.id.btPlay).setText(R.string.bt_play_play)
    }
  }
}
