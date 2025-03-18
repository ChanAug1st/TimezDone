package com.example.timezdone.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.timezdone.viewmodel.TimeZDoneViewModel
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TimezDoneApp()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimezDoneApp(viewModel: TimeZDoneViewModel = viewModel()) {
    TimezDoneContent(
        time = viewModel.time,
        onFetchTime = { region, city -> viewModel.fetchTime(region, city) }
    )
}

@Composable
fun TimezDoneContent(
    time: kotlinx.coroutines.flow.StateFlow<String>,
    onFetchTime: (String, String) -> Unit
) {
    var region = "Europe"
    var city = "Helsinki"

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
            OutlinedTextField(
                value = region,
                onValueChange = { region = it },
                label = { Text("Region (e.g., Europe)") }
            )
            OutlinedTextField(
                value = city,
                onValueChange = { city = it },
                label = { Text("City (e.g., Helsinki)") }
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { onFetchTime(region, city) }) {
                Text("Get Time")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = time.value)  // 直接使用 `StateFlow` 数据
        }
    }
}