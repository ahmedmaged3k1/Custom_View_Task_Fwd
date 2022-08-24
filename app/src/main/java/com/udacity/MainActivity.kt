package com.udacity


import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0

    private lateinit var notificationManager: NotificationManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var action: NotificationCompat.Action
    private lateinit var button: LoadingButton
    private lateinit var radioButton: RadioGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        button = findViewById(R.id.custom_button)
        radioButton = findViewById(R.id.radioGroup)
        createNotificationChannel()
        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        custom_button.setOnClickListener {
            if (radioButton.checkedRadioButtonId != -1) {
                val toast = Toast.makeText(applicationContext, "Downloading", Toast.LENGTH_SHORT)
                toast.show()
                when (radioButton.checkedRadioButtonId) {
                    1 -> {
                        download(URL)
                        makeNotificaiton(URL)
                    }
                    2 -> {
                        download(URL2)
                        makeNotificaiton(URL2)


                    }
                    3 -> {
                        download(URL2)
                        makeNotificaiton(URL3)

                    }


                }

            } else {
                val toast = Toast.makeText(
                    applicationContext,
                    "Please Select Item To Download",
                    Toast.LENGTH_SHORT
                )
                toast.show()

            }


        }
    }


    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
        }
    }

    private fun makeNotificaiton(url: String) {
        var title: String = " "
        if (url.equals(URL)) {
            title = "The Project 1 Is Downloaded"
        }
        if (url.equals(URL2)) {
            title = "The Project 2 Is Downloaded"
        }
        if (url.equals(URL3)) {
            title = "The Project 3 Is Downloaded"
        }
        val intent = Intent(this, DetailActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val builder = NotificationCompat.Builder(this, "100")
            .setSmallIcon(R.drawable.ic_assistant_black_24dp)
            .setContentTitle("Udacity Android Kotlin Nano Degree")
            .setContentText(title)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())

        val manager = NotificationManagerCompat.from(this).notify(100, builder.build())
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name ="100"
            val descriptionText = "Downloading Channel"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(100.toString(), name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun download(url: String) {
        val request =
            DownloadManager.Request(Uri.parse(url))
                .setTitle(getString(R.string.app_name))
                .setDescription(getString(R.string.app_description))
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadID =
            downloadManager.enqueue(request)// enqueue puts the download request in the queue.
    }

    companion object {
        private const val URL =
            "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"
        private const val URL2 =
            "https://github.com/square/retrofit/archive/refs/heads/master.zip"
        private const val URL3 =
            "https://github.com/bumptech/glide/archive/refs/heads/master.zip"
        private const val CHANNEL_ID = "channelId"
    }


}
