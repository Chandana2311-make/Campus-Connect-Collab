package com.example.campusconnectandcollab

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onLogin: (String, String, String, Boolean) -> Unit = { _, _, _, _ -> }
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var eventSelected by remember { mutableStateOf(false) }
    var lostFoundSelected by remember { mutableStateOf(false) }

    var isAdmin by remember { mutableStateOf(false) }

    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF6C63FF),
                        Color(0xFFFF6584)
                    )
                )
            )
    ) {

        Column(
            modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(60.dp))

            Box(
                modifier = Modifier.size(100.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.9f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "C",
                    fontSize = 42.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF6C63FF)
                )
            }

            Spacer(modifier = Modifier.height(15.dp))

            Text(
                "Campus Connect & Collab",
                fontSize = 24.sp,
                color = Color.White,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center
            )

            Text(
                "Login to continue",
                fontSize = 15.sp,
                color = Color.White.copy(alpha = 0.9f)
            )

            Spacer(modifier = Modifier.height(30.dp))

            Card(
                modifier = Modifier.fillMaxWidth().padding(5.dp),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {

                Column(
                    modifier = Modifier.padding(22.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                    )

                    Spacer(modifier = Modifier.height(15.dp))

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password") },
                        modifier = Modifier.fillMaxWidth(),
                        visualTransformation = PasswordVisualTransformation()
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = eventSelected,
                            onCheckedChange = {
                                eventSelected = it
                                if (it) {
                                    lostFoundSelected = false
                                }
                            }
                        )
                        Text("Event Updates", fontSize = 15.sp)
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = lostFoundSelected,
                            onCheckedChange = {
                                lostFoundSelected = it
                                if (it) {
                                    eventSelected = false
                                    isAdmin = false
                                }
                            }
                        )
                        Text("Lost & Found", fontSize = 15.sp)
                    }

                    if (eventSelected) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(
                                checked = isAdmin,
                                onCheckedChange = { isAdmin = it }
                            )
                            Text("Login as Admin (Event Organiser)", fontSize = 15.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(25.dp))

                    Button(
                        onClick = {
                            when {
                                !eventSelected && !lostFoundSelected ->
                                    Toast.makeText(context, "Select Event or Lost & Found", Toast.LENGTH_SHORT).show()

                                eventSelected && lostFoundSelected ->
                                    Toast.makeText(context, "Select only ONE option", Toast.LENGTH_SHORT).show()

                                lostFoundSelected && isAdmin ->
                                    Toast.makeText(context, "Admin allowed only for Events", Toast.LENGTH_SHORT).show()

                                else -> {
                                    val selectedSystem =
                                        if (eventSelected) "events" else "lost_found"

                                    onLogin(email, password, selectedSystem, isAdmin)
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF1A1A40)
                        ),
                        modifier = Modifier.fillMaxWidth().height(52.dp),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text("Login", fontSize = 19.sp, color = Color(0xFF4EE1C1))
                    }
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewLoginScreen() {
    LoginScreen()
}
