package com.example.campusconnectandcollab.ui.screens

import android.Manifest
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LostFoundScreen(navController: NavHostController) {
    val context = LocalContext.current
    val firestore = FirebaseFirestore.getInstance()

    // Manually specify the storage bucket URL to prevent common setup issues.
    // Make sure this URL matches the one in your Firebase console.
    val storage = FirebaseStorage.getInstance("gs://campusconnectandcollab.appspot.com")

    var capturedBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var isUploading by remember { mutableStateOf(false) }

    // State for the dialog and user details
    var showDetailsDialog by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var department by remember { mutableStateOf("") }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap: Bitmap? ->
        // The result can be null if the user cancels, so handle that.
        if (bitmap != null) {
            capturedBitmap = bitmap
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // Permission was granted, launch the camera.
            cameraLauncher.launch(null)
        } else {
            // Permission was denied.
            Toast.makeText(context, "Camera permission is required.", Toast.LENGTH_SHORT).show()
        }
    }

    // This function handles the entire upload process.
    fun uploadImageAndDetails(bitmap: Bitmap, userName: String, userPhone: String, userDept: String) {
        isUploading = true

        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, baos)
        val data = baos.toByteArray()

        // Create a unique file name using the current time.
        val storageRef = storage.reference.child("lost_found/${System.currentTimeMillis()}.jpg")

        // 1. Upload the image bytes to Cloud Storage
        storageRef.putBytes(data)
            .addOnSuccessListener { uploadTask ->
                Log.d("Firebase", "Image upload successful.")
                // 2. Get the public download URL for the uploaded image.
                storageRef.downloadUrl.addOnSuccessListener { downloadUrl: Uri ->
                    Log.d("Firebase", "Download URL received: $downloadUrl")

                    // 3. Create a map of the data to save in Firestore.
                    val itemDetails = mapOf(
                        "imageUrl" to downloadUrl.toString(),
                        "userName" to userName,
                        "userPhone" to userPhone,
                        "userDepartment" to userDept,
                        "timestamp" to System.currentTimeMillis()
                    )

                    // 4. Save the item details to the Firestore database.
                    firestore.collection("lost_found_items")
                        .add(itemDetails)
                        .addOnSuccessListener { documentReference ->
                            Log.d("Firebase", "Firestore write successful! Document ID: ${documentReference.id}")
                            // --- SUCCESS! ---
                            // Reset state and show success message.
                            isUploading = false
                            capturedBitmap = null
                            showDetailsDialog = false
                            name = ""
                            phone = ""
                            department = ""
                            Toast.makeText(context, "Upload successful!", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener { firestoreError ->
                            // Handle Firestore write failure
                            Log.e("Firebase", "Firestore write failed.", firestoreError)
                            isUploading = false
                            Toast.makeText(context, "Database write failed. Please try again.", Toast.LENGTH_SHORT).show()
                        }
                }.addOnFailureListener { urlError ->
                    // Handle failure to get download URL
                    Log.e("Firebase", "Failed to get download URL.", urlError)
                    isUploading = false
                    Toast.makeText(context, "Error getting image URL. Please try again.", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { uploadError ->
                // Handle image upload failure
                Log.e("Firebase", "Image upload failed.", uploadError)
                isUploading = false
                Toast.makeText(context, "Upload failed. Check connection and Firebase rules.", Toast.LENGTH_LONG).show()
            }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Lost & Found") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    // Launch the permission request. The result will be handled by permissionLauncher.
                    permissionLauncher.launch(Manifest.permission.CAMERA)
                }
            ) {
                Icon(
                    painter = painterResource(id = android.R.drawable.ic_menu_camera),
                    contentDescription = "Open Camera"
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier.fillMaxSize().padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            if (capturedBitmap != null) {
                // Show the captured image and the 'Upload' button
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(
                        bitmap = capturedBitmap!!.asImageBitmap(),
                        contentDescription = "Captured Image",
                        modifier = Modifier.size(250.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { showDetailsDialog = true }) {
                        Text("Upload")
                    }
                }
            } else {
                // Show the initial prompt
                Text(
                    text = "Click the camera icon to report a lost item",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }

    // This is the dialog for entering user details.
    if (showDetailsDialog) {
        Dialog(onDismissRequest = {
            // Don't allow dismissing the dialog while uploading.
            if (!isUploading) {
                showDetailsDialog = false
            }
        }) {
            Card(modifier = Modifier.padding(16.dp)) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Please provide your details", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Your Name") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = phone,
                        onValueChange = { phone = it },
                        label = { Text("Phone Number") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = department,
                        onValueChange = { department = it },
                        label = { Text("Department") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            if (name.isNotBlank() && phone.isNotBlank() && department.isNotBlank()) {
                                capturedBitmap?.let { bitmap ->
                                    uploadImageAndDetails(bitmap, name, phone, department)
                                }
                            } else {
                                Toast.makeText(context, "All fields are required", Toast.LENGTH_SHORT).show()
                            }
                        },
                        enabled = !isUploading, // Disable button while uploading
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        if (isUploading) {
                            // Show a progress indicator and text
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = MaterialTheme.colorScheme.onPrimary,
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Submitting...")
                        } else {
                            Text("Submit")
                        }
                    }
                }
            }
        }
    }
}
