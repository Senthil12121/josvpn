package com.josvpn.vpn

import android.net.VpnService
import android.content.Intent
import android.os.IBinder

/**
 * VPN service entry point. This intentionally does NOT establish a dummy TUN interface:
 * a TUN interface without a functioning packet engine can black-hole user traffic.
 *
 * Integrate a maintained WireGuard Android tunnel engine and a valid server profile before
 * implementing tunnel establishment. Never report CONNECTED until the engine confirms it.
 */
class JosVpnService : VpnService() {
    override fun onBind(intent: Intent?): IBinder? = super.onBind(intent)

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        stopSelf(startId)
        return START_NOT_STICKY
    }
}
