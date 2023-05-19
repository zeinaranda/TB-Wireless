package com.dicoding.picodiploma.testingwireless

import android.Manifest
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.dicoding.picodiploma.testingwireless.Model.LatLong
import com.dicoding.picodiploma.testingwireless.ViewModel.MapsViewModel
import com.dicoding.picodiploma.testingwireless.ViewModel.MapsViewModelFactory

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.dicoding.picodiploma.testingwireless.databinding.ActivityMapsBinding
import com.dicoding.picodiploma.testingwireless.databinding.BottomSheetBinding
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.material.bottomsheet.BottomSheetDialog

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var geofencingClient: GeofencingClient
    private lateinit var jurusanTextView: TextView
    private lateinit var latitudeTextView: TextView
    private lateinit var longitudeTextView: TextView
    private lateinit var checkInButton: AppCompatButton
    private lateinit var listJurusan : List<LatLong>
    private val viewModel: MapsViewModel by viewModels {
        MapsViewModelFactory.getInstance(this)
    }

    private val geofenceRadius = 40.0

    private val geofencePendingIntent: PendingIntent by lazy {
        val intent = Intent(this, GeofenceBroadcastReceiver::class.java)
        intent.action = GeofenceBroadcastReceiver.ACTION_GEOFENCE_EVENT
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_MUTABLE)
        } else {
            PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        }
    }

    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val lokasi = intent?.getStringExtra("lokasi")
            val latitude = intent?.getStringExtra("latitude")
            val longitude = intent?.getStringExtra("longitude")
            if (lokasi != null) {
                jurusanTextView.text = lokasi
                latitudeTextView.text = latitude
                longitudeTextView.text = longitude
            }
            checkInButton.setOnClickListener{
                if (lokasi != null) {
                    val idLokasi = getIdLokasi(lokasi)
                    Log.i("Check In","Check in dengan id lokasi $idLokasi")
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        toolbar.title = "Maps"
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val intentFilter = IntentFilter("my-event")
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, intentFilter)
        jurusanTextView = findViewById(R.id.jurusan)
        latitudeTextView = findViewById(R.id.latitude)
        longitudeTextView = findViewById(R.id.longitude)
        checkInButton = findViewById(R.id.btn_check)

        viewModel.getMarker().observe(this, { response ->
            if (response != null) {
                when (response) {
                    is com.dicoding.picodiploma.testingwireless.utils.Result.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is com.dicoding.picodiploma.testingwireless.utils.Result.Success -> {
                        binding.progressBar.visibility = View.GONE
                        listJurusan = response.data.data
                        showMarker(listJurusan)
                    }
                    is com.dicoding.picodiploma.testingwireless.utils.Result.Failure -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(this, response.error, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onResume() {
        super.onResume()

        // Daftarkan GeofenceBroadcastReceiver sebagai penerima siaran
        val intentFilter = IntentFilter(GeofenceBroadcastReceiver.ACTION_GEOFENCE_EVENT)
        registerReceiver(broadcastReceiver, intentFilter)
    }

    override fun onPause() {
        super.onPause()

        // Batalkan pendaftaran GeofenceBroadcastReceiver sebagai penerima siaran
        unregisterReceiver(broadcastReceiver)
    }

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver)
    }

    override fun onMapReady(googleMap: GoogleMap) {

        mMap = googleMap

        mMap.uiSettings.isZoomControlsEnabled = true

        getMyLocation()
        //addGeofence()

    }


    private val requestBackgroundLocationPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                getMyLocation()
            }
        }

    private val runningQOrLater = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q

    @TargetApi(Build.VERSION_CODES.Q)
    private val requestLocationPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                if (runningQOrLater) {
                    requestBackgroundLocationPermissionLauncher.launch(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                } else {
                    getMyLocation()
                }
            }
        }

    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    @TargetApi(Build.VERSION_CODES.Q)
    private fun checkForegroundAndBackgroundLocationPermission(): Boolean {
        val foregroundLocationApproved = checkPermission(Manifest.permission.ACCESS_FINE_LOCATION)
        val backgroundPermissionApproved =
            if (runningQOrLater) {
                checkPermission(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
            } else {
                true
            }
        return foregroundLocationApproved && backgroundPermissionApproved
    }

    @SuppressLint("MissingPermission")
    private fun getMyLocation() {
        if (checkForegroundAndBackgroundLocationPermission()) {
            mMap.isMyLocationEnabled = true
            val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    val currentLocation = LatLng(location.latitude, location.longitude)
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 18f))
                }
            }
        } else {
            requestLocationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    @SuppressLint("MissingPermission")
    private fun addGeofence(dataJurusan : LatLong) {
        geofencingClient = LocationServices.getGeofencingClient(this)

        val geofence = Geofence.Builder()
            .setRequestId(dataJurusan.jurusan)
            .setCircularRegion(dataJurusan.latitude, dataJurusan.longitude, geofenceRadius.toFloat())
            .setExpirationDuration(Geofence.NEVER_EXPIRE)
            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_DWELL or Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_EXIT)
            .setLoiteringDelay(5000)
            .build()

        val geofencingRequest = GeofencingRequest.Builder()
            .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
            .addGeofence(geofence)
            .build()

        geofencingClient.removeGeofences(geofencePendingIntent).run {
            addOnCompleteListener {
                geofencingClient.addGeofences(geofencingRequest, geofencePendingIntent).run {
                    addOnSuccessListener {
                        showToast("Geofencing added")
                    }
                }
            }
        }
    }

    private fun showMarker(listStory: List<LatLong>) {
        for (story in listStory) {
            if (story.latitude != null && story.longitude != null) {
                val latlng = LatLng(story.latitude, story.longitude)
                mMap.addMarker(
                    MarkerOptions()
                        .position(latlng)
                )
                mMap.addCircle(
                    CircleOptions()
                        .center(latlng)
                        .radius(geofenceRadius)
                        .fillColor(0x22FF0000)
                        .strokeColor(Color.RED)
                        .strokeWidth(3f)
                )
                addGeofence(story)
            }
        }
    }

    private fun showToast(text: String) {
        Toast.makeText(this@MapsActivity, text, Toast.LENGTH_SHORT).show()
    }

    private fun getIdLokasi (lokasi:String): String {
        for (jurusan in listJurusan) {
            if (jurusan.jurusan == lokasi) {
                return jurusan.id
            }
        }
        return "Kosong"
    }

}