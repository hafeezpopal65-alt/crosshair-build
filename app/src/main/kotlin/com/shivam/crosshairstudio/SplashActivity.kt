package com.shivam.crosshairstudio

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {

    private var progressWidth = 0
    private val handler = Handler(Looper.getMainLooper())
    private var totalWidth = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        setContentView(R.layout.activity_splash)

        val progressBar = findViewById<View>(R.id.progressBar)
        val pctLabel = findViewById<TextView>(R.id.splashPercent)
        val progressTrack = findViewById<View>(R.id.progressTrack)

        progressTrack.post {
            totalWidth = progressTrack.width
            animateProgress(progressBar, pctLabel)
        }
    }

    private fun animateProgress(bar: View, pct: TextView) {
        var current = 0
        val step = totalWidth / 25

        val runnable = object : Runnable {
            override fun run() {
                current += step
                if (current >= totalWidth) {
                    current = totalWidth
                    bar.layoutParams = bar.layoutParams.also {
                        (it as ViewGroup.LayoutParams).width = current
                    }
                    bar.requestLayout()
                    pct.text = "100%"
                    handler.postDelayed({
                        startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                        finish()
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    }, 300)
                    return
                }
                val percent = (current.toFloat() / totalWidth * 100).toInt()
                bar.layoutParams = bar.layoutParams.also {
                    (it as ViewGroup.LayoutParams).width = current
                }
                bar.requestLayout()
                pct.text = "$percent%"
                handler.postDelayed(this, 30)
            }
        }
        handler.post(runnable)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }
}
