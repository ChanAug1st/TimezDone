package com.example.timezdone

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TimeZDoneApp()
        }
    }
}

@Composable
fun TimeZDoneApp() {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Time zDone") })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            TimeZoneSelector()
        }
    }
}

@Composable
fun TimeZoneSelector() {
    // List of available time zones
    val timeZones = listOf("Europe/Helsinki", "Asia/Shanghai", "America/New_York", "Europe/London", "Asia/Tokyo")
    var selectedZone by remember { mutableStateOf(timeZones[0]) }
    var time by remember { mutableStateOf("Select a time zone and query") }
    var expanded by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Column {
        Text(text = "Select Time Zone:", fontSize = 18.sp)
        Box(modifier = Modifier.clickable { expanded = true }) {
            Text(text = selectedZone, fontSize = 18.sp, modifier = Modifier.padding(8.dp))
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            timeZones.forEach { zone ->
                DropdownMenuItem(text = { Text(zone) }, onClick = {
                    selectedZone = zone
                    expanded = false
                })
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            scope.launch {
                time = fetchTimeForTimezone(selectedZone) ?: "Query failed"
            }
        }) {
            Text("Query Time")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Current Time: $time", fontSize = 18.sp)
    }
}

// Function to fetch the current time for a given time zone
suspend fun fetchTimeForTimezone(timezone: String): String? {
    return withContext(Dispatchers.IO) {
        try {
            val client = OkHttpClient()
            val request = Request.Builder()
                .url("http://worldtimeapi.org/api/timezone/$timezone")
                .build()
            val response = client.newCall(request).execute()
            val json = JSONObject(response.body?.string() ?: return@withContext null)
            json.getString("datetime") // Extracting the datetime string
        } catch (e: Exception) {
            null // Return null if an error occurs
        }
    }
}
