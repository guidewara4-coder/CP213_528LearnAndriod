package com.example.a528_lablearnandroid

import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.example.a528_lablearnandroid.ui.theme._528_LabLearnAndroidTheme

data class SensorDataState(
    val accelX: Float = 0f,
    val accelY: Float = 0f,
    val accelZ: Float = 0f,
    val latitude: Double = 0.0,
    val longitude: Double = 0.0
)

class AccelerometerTracker(context: Context) {
    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as android.hardware.SensorManager
    private val accelSensor = sensorManager.getDefaultSensor(android.hardware.Sensor.TYPE_ACCELEROMETER)
    private var listener: android.hardware.SensorEventListener? = null

    fun startListening(onSensorChanged: (Float, Float, Float) -> Unit) {
        listener = object : android.hardware.SensorEventListener {
            override fun onSensorChanged(event: android.hardware.SensorEvent?) {
                event?.values?.let {
                    if (it.size >= 3) {
                        onSensorChanged(it[0], it[1], it[2])
                    }
                }
            }
            override fun onAccuracyChanged(sensor: android.hardware.Sensor?, accuracy: Int) {}
        }
        accelSensor?.let {
            sensorManager.registerListener(listener, it, android.hardware.SensorManager.SENSOR_DELAY_UI)
        }
    }

    fun stopListening() {
        listener?.let {
            sensorManager.unregisterListener(it)
        }
        listener = null
    }
}

class LocationTracker(context: Context) {
    private val fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
    private var locationCallback: LocationCallback? = null

    @SuppressLint("MissingPermission")
    fun startListening(onLocationUpdated: (Double, Double) -> Unit) {
        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 2000)
            .setMinUpdateIntervalMillis(1000)
            .build()

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                for (location in locationResult.locations) {
                    onLocationUpdated(location.latitude, location.longitude)
                }
            }
        }

        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback!!,
            Looper.getMainLooper()
        )
    }

    fun stopListening() {
        locationCallback?.let {
            fusedLocationClient.removeLocationUpdates(it)
        }
        locationCallback = null
    }
}

class MultiSensorViewModel(application: Application) : AndroidViewModel(application) {
    private val accelTracker = AccelerometerTracker(application)
    private val locationTracker = LocationTracker(application)

    private val _sensorState = MutableStateFlow(SensorDataState())
    val sensorState: StateFlow<SensorDataState> = _sensorState.asStateFlow()

    fun startAccelerometer() {
        accelTracker.startListening { x, y, z ->
            _sensorState.value = _sensorState.value.copy(accelX = x, accelY = y, accelZ = z)
        }
    }

    fun startLocationUpdates() {
        locationTracker.startListening { lat, lon ->
            _sensorState.value = _sensorState.value.copy(latitude = lat, longitude = lon)
        }
    }

    override fun onCleared() {
        super.onCleared()
        accelTracker.stopListening()
        locationTracker.stopListening()
    }
}

class SensorActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            _528_LabLearnAndroidTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MultiSensorScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun MultiSensorScreen(
    modifier: Modifier = Modifier,
    viewModel: MultiSensorViewModel = viewModel()
) {
    val context = LocalContext.current
    val sensorState by viewModel.sensorState.collectAsState()

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val isGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        if (isGranted) {
            viewModel.startLocationUpdates()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.startAccelerometer()
        
        val fineLocationGranted = ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        
        val coarseLocationGranted = ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        
        if (fineLocationGranted || coarseLocationGranted) {
            viewModel.startLocationUpdates()
        } else {
            locationPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    Column(
        modifier = modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Accelerometer (X, Y, Z)", fontSize = 20.sp)
        Text(text = "X: ${"%.2f".format(sensorState.accelX)}", fontSize = 16.sp)
        Text(text = "Y: ${"%.2f".format(sensorState.accelY)}", fontSize = 16.sp)
        Text(text = "Z: ${"%.2f".format(sensorState.accelZ)}", fontSize = 16.sp)
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Text(text = "GPS Location (Lat, Lon)", fontSize = 20.sp)
        Text(text = "Latitude: ${sensorState.latitude}", fontSize = 16.sp)
        Text(text = "Longitude: ${sensorState.longitude}", fontSize = 16.sp)
        
        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = {
            locationPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }) {
            Text("Request Location Permission")
        }
    }
}
