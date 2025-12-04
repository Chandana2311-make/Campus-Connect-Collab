package com.example.campusconnectandcollab.ui.screens

import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaceholderLostFoundScreen(
    navController: NavHostController? = null,
    onImageCaptured: (Bitmap) -> Unit = {}
) {
    // Launcher that returns a Bitmap (TakePicturePreview)
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap: Bitmap? ->
        bitmap?.let {
            // send to callback (ViewModel/upload logic should handle upload)
            onImageCaptured(it)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Lost & Found") },
                navigationIcon = {
                    IconButton(onClick = { navController?.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        // default FAB position is bottom-end which is bottom-right (approx 4 o'clock)
        floatingActionButton = {
            FloatingActionButton(
                onClick = { cameraLauncher.launch(null) }, // required for TakePicturePreview
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                // Use a built-in android drawable to avoid needing material-icons dependency
                Icon(
                    painter = painterResource(id = android.R.drawable.ic_menu_camera),
                    contentDescription = "Open Camera"
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            // Non-clickable center text
            Text(
                text = "No lost item found",
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}
