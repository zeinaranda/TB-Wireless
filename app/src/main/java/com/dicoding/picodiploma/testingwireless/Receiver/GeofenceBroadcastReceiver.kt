package com.dicoding.picodiploma.testingwireless.Receiver

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.dicoding.picodiploma.testingwireless.R
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofenceStatusCodes
import com.google.android.gms.location.GeofencingEvent


class GeofenceBroadcastReceiver() : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == ACTION_GEOFENCE_EVENT) {
            val geofencingEvent = GeofencingEvent.fromIntent(intent)

            if (geofencingEvent.hasError()) {
                val errorMessage = GeofenceStatusCodes.getStatusCodeString(geofencingEvent.errorCode)
                sendNotification(context, errorMessage)
                return
            }

            val geofenceTransition = geofencingEvent.geofenceTransition
            val broadcastIntent = Intent("my-event")
            val triggeringGeofences = geofencingEvent.triggeringGeofences
            val lokasi = geofencingEvent.triggeringLocation
            val requestId = triggeringGeofences[0].requestId

            when (geofenceTransition) {
                Geofence.GEOFENCE_TRANSITION_ENTER -> {
                    // Handle "enter" event
                    val geofenceTransitionDetails = "Anda telah memasuki area $requestId"
                    Log.i(TAG, geofenceTransitionDetails)
                    sendNotification(context, geofenceTransitionDetails)

                    broadcastIntent.putExtra("lokasi", requestId)
                    broadcastIntent.putExtra("latitude", lokasi.latitude.toString())
                    broadcastIntent.putExtra("longitude", lokasi.longitude.toString())
                    LocalBroadcastManager.getInstance(context).sendBroadcast(broadcastIntent)
                }
                Geofence.GEOFENCE_TRANSITION_DWELL -> {
                    // Handle "dwell" event
                    val geofenceTransitionDetails = "Anda telah di dalam area $requestId"
                    Log.i(TAG, geofenceTransitionDetails)
                    sendNotification(context, geofenceTransitionDetails)
                }
                Geofence.GEOFENCE_TRANSITION_EXIT -> {
                    // Handle "exit" event
                    val geofenceTransitionDetails = "Anda telah keluar dari area $requestId"
                    Log.i(TAG, geofenceTransitionDetails)
                    sendNotification(context, geofenceTransitionDetails)
                    broadcastIntent.putExtra("lokasi", "Diluar Area Absen")
                    broadcastIntent.putExtra("latitude", "Diluar Area Absen")
                    broadcastIntent.putExtra("longitude", "Diluar Area Absen")
                    LocalBroadcastManager.getInstance(context).sendBroadcast(broadcastIntent)
                }
                else -> {
                    // Handle unknown transition
                    Log.e("GeofenceReceiver", "Unknown transition: $geofenceTransition")
                }
            }
        }
    }


    private fun sendNotification(context: Context, geofenceTransitionDetails: String) {
        val mNotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val mBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle(geofenceTransitionDetails)
            .setContentText("Anda sudah bisa absen sekarang :)")
            .setSmallIcon(R.drawable.ic_baseline_notifications_active_24)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
            mBuilder.setChannelId(CHANNEL_ID)
            mNotificationManager.createNotificationChannel(channel)
        }
        val notification = mBuilder.build()
        mNotificationManager.notify(NOTIFICATION_ID, notification)
    }

//    private fun showBottomSheet(context: Context) {
//        val intent = Intent(context, BottomSheetActivity::class.java)
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//        context.startActivity(intent)
//    }

    companion object {
        private const val TAG = "GeofenceBroadcast"
        const val ACTION_GEOFENCE_EVENT = "GeofenceEvent"
        private const val CHANNEL_ID = "1"
        private const val CHANNEL_NAME = "Geofence Channel"
        private const val NOTIFICATION_ID = 1
    }
}