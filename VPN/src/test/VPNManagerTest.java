package com.vpn;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class VPNManagerTest {
    private VPNManager vpnManager;
    private VPNServer testServer;

    @BeforeEach
    void setUp() {
        vpnManager = new VPNManager();
        testServer = new VPNServer("Test Server", "Test Location", "192.168.1.1", 50);
    }

    @Test
    void testInitialState() {
        assertFalse(vpnManager.isConnected());
        assertNull(vpnManager.getCurrentServer());
        assertEquals("Disconnected", vpnManager.getConnectionStatus());
    }

    @Test
    void testGetAvailableServers() {
        var servers = vpnManager.getAvailableServers();
        assertNotNull(servers);
        assertFalse(servers.isEmpty());
        assertTrue(servers.size() >= 6); // We initialized 6 servers
    }

    @Test
    void testConnect() {
        boolean connected = vpnManager.connect(testServer);
        assertTrue(connected);
        assertTrue(vpnManager.isConnected());
        assertEquals(testServer, vpnManager.getCurrentServer());
    }

    @Test
    void testDisconnect() {
        // First connect
        vpnManager.connect(testServer);
        assertTrue(vpnManager.isConnected());
        
        // Then disconnect
        vpnManager.disconnect();
        assertFalse(vpnManager.isConnected());
        assertNull(vpnManager.getCurrentServer());
    }

    @Test
    void testConnectionStatus() {
        assertEquals("Disconnected", vpnManager.getConnectionStatus());
        
        vpnManager.connect(testServer);
        assertEquals("Connected to Test Server", vpnManager.getConnectionStatus());
        
        vpnManager.disconnect();
        assertEquals("Disconnected", vpnManager.getConnectionStatus());
    }
}
