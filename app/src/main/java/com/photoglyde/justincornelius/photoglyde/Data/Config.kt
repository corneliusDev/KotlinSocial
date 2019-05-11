package com.photoglyde.justincornelius.photoglyde.Data


import android.app.Application
import android.graphics.Bitmap
import android.media.audiofx.DynamicsProcessing
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import im.ene.toro.exoplayer.Config
import im.ene.toro.exoplayer.ExoCreator
import im.ene.toro.exoplayer.MediaSourceBuilder
import im.ene.toro.exoplayer.ToroExo
import java.io.File

/**
 * @author eneim (2018/01/26).
 */
class Configuration : Application() {

    companion object {
        var cacheFile = 2 * 1024 * 1024.toLong() // size of each cache file.
        var configuration: com.photoglyde.justincornelius.photoglyde.Data.Configuration? = null
        var config1: Config? = null
        var exoCreator: ExoCreator? = null
    }

        override fun onCreate() {
        super.onCreate()
        println("====demo on create")
        configuration = this
        val cache = SimpleCache(File(filesDir.path + "/toro_cache"),
            LeastRecentlyUsedCacheEvictor(cacheFile))
         config1 = Config.Builder()
             .setMediaSourceBuilder(MediaSourceBuilder.LOOPING)
             .setCache(cache)
             .build()

        exoCreator = ToroExo.with(this).getCreator(config1)

        println("====demo on create $config1 and $exoCreator")
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        if (level >= TRIM_MEMORY_BACKGROUND) ToroExo.with(this).cleanUp()
    }
}