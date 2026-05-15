package com.arogyanidhi.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import com.arogyanidhi.R
import com.arogyanidhi.data.Hospital
import com.arogyanidhi.data.HospitalResponse
import com.arogyanidhi.util.LocationHelper
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import java.io.InputStream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HospitalScreen(navController: NavHostController? = null) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var currentLocation by remember { mutableStateOf<android.location.Location?>(null) }
    var userDistrict by remember { mutableStateOf("Bengaluru") }
    var userSubDistrict by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    var selectedDistrictFilter by remember { mutableStateOf<String?>(null) }

    // Load hospitals from JSON
    val allHospitals = remember {
        loadHospitalsFromJson(context)
    }

    // Permission Launcher
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
            permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true) {
            fetchUserLocation(context, coroutineScope) { location, district, subDistrict ->
                currentLocation = location
                userDistrict = district
                userSubDistrict = subDistrict
                selectedDistrictFilter = district
            }
        } else {
            Toast.makeText(context, "Location permission is required for better results", Toast.LENGTH_LONG).show()
        }
    }

    LaunchedEffect(Unit) {
        LocationHelper.init(context)

        // Auto request permission
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionLauncher.launch(arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ))
        } else {
            fetchUserLocation(context, coroutineScope) { location, district, subDistrict ->
                currentLocation = location
                userDistrict = district
                userSubDistrict = subDistrict
                selectedDistrictFilter = district
            }
        }
    }

    // District-based + Search filtering
    val filteredHospitals = remember(allHospitals, searchQuery, selectedDistrictFilter) {
        allHospitals.filter { hospital ->
            val matchesSearch = searchQuery.isBlank() ||
                    hospital.name.contains(searchQuery, ignoreCase = true) ||
                    hospital.district.contains(searchQuery, ignoreCase = true)

            val matchesDistrict = selectedDistrictFilter == null ||
                    hospital.district.equals(selectedDistrictFilter, ignoreCase = true)

            matchesSearch && matchesDistrict
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("KASS Empanelled Hospitals") },
                actions = {
                    IconButton(onClick = {
                        permissionLauncher.launch(arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        ))
                    }) {
                        Text("📍")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            // Location Info Card
            if (userSubDistrict.isNotEmpty() || userDistrict.isNotEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "📍 Detected: $userSubDistrict, $userDistrict",
                            style = MaterialTheme.typography.titleSmall
                        )
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            // Search Field
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Search Hospital or District") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            // District Filter Chips
            Text("Filter by District:", style = MaterialTheme.typography.titleSmall)
            Spacer(modifier = Modifier.height(8.dp))

            val uniqueDistricts = allHospitals.map { it.district }.distinct().sorted()

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.height(56.dp)
            ) {
                items(uniqueDistricts) { district ->
                    FilterChip(
                        selected = selectedDistrictFilter == district,
                        onClick = {
                            selectedDistrictFilter = if (selectedDistrictFilter == district) null else district
                        },
                        label = { Text(district) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }

            // Hospital List
            LazyColumn {
                items(filteredHospitals) { hospital ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(hospital.name, style = MaterialTheme.typography.titleMedium)
                            Text("📍 ${hospital.district}", style = MaterialTheme.typography.bodyMedium)
                            Text(hospital.address, style = MaterialTheme.typography.bodySmall)
                            Text(
                                text = hospital.speciality,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.tertiary
                            )
                        }
                    }
                }

                if (filteredHospitals.isEmpty()) {
                    item {
                        Text(
                            text = "No hospitals found for the selected filter.",
                            modifier = Modifier.padding(32.dp),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }
    }
}

// Helper function to fetch location
private fun fetchUserLocation(
    context: android.content.Context,
    coroutineScope: kotlinx.coroutines.CoroutineScope,
    onResult: (android.location.Location?, String, String) -> Unit
) {
    coroutineScope.launch {
        val location = LocationHelper.getCurrentLocation(context)
        if (location != null) {
            val districtInfo = LocationHelper.getDistrictFromLocation(context, location)
            if (districtInfo != null) {
                onResult(location, districtInfo.first, districtInfo.second)
            } else {
                onResult(location, "Bengaluru", "")
            }
        }
    }
}

// Load JSON
private fun loadHospitalsFromJson(context: android.content.Context): List<Hospital> {
    return try {
        val inputStream: InputStream = context.resources.openRawResource(R.raw.hospitals)
        val jsonString = inputStream.bufferedReader().use { it.readText() }
        val json = Json { ignoreUnknownKeys = true }
        json.decodeFromString<HospitalResponse>(jsonString).hospitals
    } catch (e: Exception) {
        e.printStackTrace()
        emptyList()
    }
}