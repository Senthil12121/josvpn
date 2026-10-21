package com.josvpn

import android.app.Activity
import android.content.Intent
import android.net.VpnService
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.josvpn.vpn.JosVpnService

private val Navy = Color(0xFF101A35)
private val Accent = Color(0xFF6C63FF)
private val Muted = Color(0xFF9CA8C4)

class MainActivity : ComponentActivity() {
    private var message by mutableStateOf("Ready to configure")
    private val vpnPermission = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            message = "Android VPN permission granted. Add a real tunnel engine and server configuration to connect."
        } else {
            message = "VPN permission was not granted."
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme(colorScheme = darkColorScheme(
                primary = Accent, background = Navy, surface = Color(0xFF1B2747),
                onBackground = Color.White, onSurface = Color.White
            )) {
                JosVpnScreen(
                    message = message,
                    onConnect = {
                        val prepare = VpnService.prepare(this)
                        if (prepare != null) vpnPermission.launch(prepare)
                        else message = "Permission already granted. A real WireGuard tunnel engine and server profile are still required."
                    },
                    onDisconnect = {
                        stopService(Intent(this, JosVpnService::class.java))
                        message = "Disconnected (no tunnel was started by this starter project)."
                    }
                )
            }
        }
    }
}

@Composable
private fun JosVpnScreen(message: String, onConnect: () -> Unit, onDisconnect: () -> Unit) {
    var selectedServer by remember { mutableStateOf("Choose a configured server") }
    Surface(modifier = Modifier.fillMaxSize(), color = Navy) {
        Column(
            modifier = Modifier.fillMaxSize().padding(horizontal = 22.dp, vertical = 18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                Icon(Icons.Default.Security, contentDescription = null, tint = Accent, modifier = Modifier.size(32.dp))
                Spacer(Modifier.width(10.dp))
                Column(Modifier.weight(1f)) {
                    Text("JOS VPN", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Text("Private connection dashboard", fontSize = 12.sp, color = Muted)
                }
                Icon(Icons.Default.Settings, contentDescription = "Settings", tint = Muted)
            }
            Spacer(Modifier.height(34.dp))
            Box(
                modifier = Modifier.size(220.dp).background(Color(0xFF1B2747), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Box(Modifier.size(174.dp).background(Color(0xFF26365E), CircleShape), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.Security, contentDescription = null, tint = Accent, modifier = Modifier.size(52.dp))
                        Spacer(Modifier.height(8.dp))
                        Text("NOT CONNECTED", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 14.sp)
                        Text("Tunnel not configured", color = Muted, fontSize = 11.sp)
                    }
                }
            }
            Spacer(Modifier.height(26.dp))
            Text("Your connection", color = Muted, fontSize = 13.sp)
            Spacer(Modifier.height(10.dp))
            Card(colors = CardDefaults.cardColors(containerColor = Color(0xFF1B2747)), shape = RoundedCornerShape(18.dp)) {
                Column(Modifier.fillMaxWidth().padding(16.dp)) {
                    Text("VPN server", color = Muted, fontSize = 12.sp)
                    Spacer(Modifier.height(4.dp))
                    Text(selectedServer, color = Color.White, fontWeight = FontWeight.SemiBold)
                    Spacer(Modifier.height(12.dp))
                    OutlinedButton(onClick = { selectedServer = "No server configured — see README" }, modifier = Modifier.fillMaxWidth()) {
                        Text("Select server")
                    }
                }
            }
            Spacer(Modifier.height(18.dp))
            Button(onClick = onConnect, modifier = Modifier.fillMaxWidth().height(54.dp), shape = RoundedCornerShape(16.dp)) {
                Text("Set up VPN permission", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.height(8.dp))
            OutlinedButton(onClick = onDisconnect, modifier = Modifier.fillMaxWidth().height(50.dp), shape = RoundedCornerShape(16.dp)) {
                Text("Disconnect / Reset")
            }
            Spacer(Modifier.height(18.dp))
            Text(message, color = Muted, fontSize = 12.sp, lineHeight = 17.sp)
            Spacer(Modifier.weight(1f))
            Text("JOS VPN • Personal project", color = Muted, fontSize = 11.sp)
        }
    }
}
