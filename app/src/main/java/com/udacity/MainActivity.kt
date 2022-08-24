package com.udacity


import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.database.Cursor
import android.media.RingtoneManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.RadioButton
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
    private lateinit var radioSelected: RadioButton
    private lateinit var radioSelected2: RadioButton
    private lateinit var radioSelected3: RadioButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        button = findViewById(R.id.custom_button)
        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
        createNotificationChannel()

        custom_button.setOnClickListener {
            Log.d(TAG, "onCreate: asdasddsd")
            radioButton = findViewById(R.id.radioGroup)
            if (radioButton.checkedRadioButtonId != -1) {
                val toast = Toast.makeText(applicationContext, "Downloading", Toast.LENGTH_SHORT)
                toast.show()
                Log.d(TAG, "onCreate: radio ${radioButton.checkedRadioButtonId} ")
                radioSelected = findViewById(R.id.project)
                radioSelected2 = findViewById(R.id.glide)
                radioSelected3 = findViewById(R.id.retrofit)




                if (radioSelected.isChecked) {

                    var status: String = ""
                    val intStatus = checkDownloadStatus(URL)
                    if (intStatus == 1) {
                        status = "Download Success"
                    } else if (intStatus == 0) {
                        status = "Download Failed"

                    } else if (intStatus == 2) {
                        status = "Downloading Successfully"

                    } else if (intStatus == 3) {
                        status = "Download is Running "

                    }
                    if (!checkForInternet(applicationContext)) {
                        status = "Download Failed"
                    }

                    makeNotificaiton(URL, status)
                }
                if (radioSelected2.isChecked) {

                    var status: String = ""
                    val intStatus = checkDownloadStatus(URL2)
                    if (intStatus == 1) {
                        status = "Download Success"
                    } else if (intStatus == 0) {
                        status = "Download Failed"

                    } else if (intStatus == 2) {
                        status = "Downloading Successfully"

                    } else if (intStatus == 3) {
                        status = "Download is Running "

                    }
                    if (!checkForInternet(applicationContext)) {
                        status = "Download Failed"
                    }
                    makeNotificaiton(URL2, status)


                }
                if (radioSelected3.isChecked) {

                    var status: String = "Default "
                    val intStatus = checkDownloadStatus(URL3)
                    Log.d("TAG", "onCreate: $intStatus")
                    if (intStatus == 1) {
                        status = "Download Success"
                    } else if (intStatus == 0) {
                        status = "Download Failed"

                    } else if (intStatus == 2) {
                        status = "Downloading Successfully"

                    } else if (intStatus == 3) {
                        status = "Download is Running "

                    }
                    if (!checkForInternet(applicationContext)) {
                        status = "Download Failed"
                    }
                    makeNotificaiton(URL3, status)

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

    private fun makeNotificaiton(url: String, status: String) {
        Log.d(TAG, "makeNotificaiton: entered ")
        var title: String = " "
        Log.d(TAG, "makeNotificaiton: $url")
        if (url.equals(URL)) {
            title = "This Project  Is  Downloaded"
        }
        if (url.equals(URL2)) {
            title = "The Glide Image  project  Is Downloaded"
        }
        if (url.equals(URL3)) {
            title = "The Retrofit - Type Safe   Is Downloaded"
        }
        val intent = Intent(this, DetailActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        intent.putExtra("status", status)
        intent.putExtra("File", title)

        val pendingIntent: PendingIntent =
            PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        var id = Math.random()
        val builder = NotificationCompat.Builder(this, "100")
            .setSmallIcon(R.drawable.ic_assistant_black_24dp)
            .setContentTitle("Udacity Android Kotlin Nano Degree")
            .setContentText(title)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .addAction(R.drawable.abc_vector_test,"Check The Status",pendingIntent)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())

        val manager = NotificationManagerCompat.from(this).notify(100, builder.build())
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "100"
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

    private fun checkDownloadStatus(url: String): Int {
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
        val cursor: Cursor =
            downloadManager.query(DownloadManager.Query().setFilterById(downloadID))
        var statusId: Int = -1


        if (cursor.moveToNext()) {
            val status: Int = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
            cursor.close()

            if (status == DownloadManager.STATUS_FAILED) {
                statusId = 0
            } else if (status == DownloadManager.STATUS_PENDING || status == DownloadManager.STATUS_PAUSED) {
                // do something pending or paused
                statusId = 2
            } else if (status == DownloadManager.STATUS_SUCCESSFUL) {
                // do something when successful
                statusId = 1
            } else if (status == DownloadManager.STATUS_RUNNING) {
                // do something when running
                statusId = 3
            }
        }


        Log.d(TAG, "checkDownloadStatus: $statusId")
        return statusId
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

    private fun checkForInternet(context: Context): Boolean {

        // register activity with the connectivity manager service
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        // if the android version is equal to M
        // or greater we need to use the
        // NetworkCapabilities to check what type of
        // network has the internet connection
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            // Returns a Network object corresponding to
            // the currently active default data network.
            val network = connectivityManager.activeNetwork ?: return false

            // Representation of the capabilities of an active network.
            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

            return when {
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true


                else -> false
            }
        } else {
            // if the android version is below M
            @Suppress("DEPRECATION") val networkInfo =
                connectivityManager.activeNetworkInfo ?: return false
            @Suppress("DEPRECATION")
            return networkInfo.isConnected
        }
    }


}
