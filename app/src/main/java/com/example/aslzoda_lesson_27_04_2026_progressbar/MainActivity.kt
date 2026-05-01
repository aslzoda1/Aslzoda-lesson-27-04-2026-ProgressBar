package com.example.aslzoda_lesson_27_04_2026_progressbar

import android.content.Context
import android.graphics.Color
import android.os.*
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val circularBar = findViewById<ProgressBar>(R.id.circularProgressBar)
        val horizontalBar = findViewById<ProgressBar>(R.id.horizontalProgressBar)
        val btnDownload = findViewById<Button>(R.id.btnDownload)
        val percentText = findViewById<TextView>(R.id.percentText)
        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        btnDownload.setOnClickListener {
            // Tugma animatsiyasi
            btnDownload.animate().scaleX(0.95f).scaleY(0.95f).setDuration(100).withEndAction {
                btnDownload.animate().scaleX(1f).scaleY(1f).start()
            }

            btnDownload.isEnabled = false
            btnDownload.text = "Preparing..."

            lifecycleScope.launch {
                for (i in 0..100) {
                    delay(50)

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        horizontalBar.setProgress(i, true)
                    } else {
                        horizontalBar.progress = i
                    }

                    percentText.text = "$i%"

                    // Har 25% da vibratsiya
                    if (i % 25 == 0 && i != 0) {
                        vibratePhone(vibrator, 15)
                    }
                }

                // Yakuniy animatsiyalar
                circularBar.animate()
                    .alpha(0f)
                    .scaleX(0.5f)
                    .scaleY(0.5f)
                    .setInterpolator(AccelerateDecelerateInterpolator())
                    .setDuration(500)
                    .withEndAction {
                        circularBar.visibility = View.GONE
                        btnDownload.text = "Installed Successfully"
                        btnDownload.setBackgroundColor(Color.parseColor("#34C759"))

                        // Muvaffaqiyat signali
                        vibratePhone(vibrator, 50)
                    }
            }
        }
    }

    // Vibratsiyani xavfsiz chaqirish funksiyasi
    private fun vibratePhone(vibrator: Vibrator, duration: Long) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(duration)
        }
    }
}