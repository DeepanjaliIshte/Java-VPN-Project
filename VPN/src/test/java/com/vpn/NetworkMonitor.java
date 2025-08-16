package com.vpn;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class NetworkMonitor {
    private ScheduledExecutorService scheduler;
    private AtomicLong downloadBytes;
    private AtomicLong uploadBytes;
    private AtomicLong totalBytes;
    private boolean isMonitoring;

    public NetworkMonitor() {
        this.scheduler = Executors.newScheduledThreadPool(1);
        this.downloadBytes = new AtomicLong(0);
        this.uploadBytes = new AtomicLong(0);
        this.totalBytes = new AtomicLong(0);
        this.isMonitoring = false;
    }

    public void startMonitoring() {
        if (!isMonitoring) {
            isMonitoring = true;
            scheduler.scheduleAtFixedRate(this::updateNetworkStats, 0, 1, TimeUnit.SECONDS);
            System.out.println("Network monitoring started");
        }
    }

    public void stopMonitoring() {
        if (isMonitoring) {
            isMonitoring = false;
            scheduler.shutdown();
            System.out.println("Network monitoring stopped");
        }
    }

    private void updateNetworkStats() {
        // Simulate network traffic monitoring
        long download = (long)(Math.random() * 1024 * 100); // Random download bytes
        long upload = (long)(Math.random() * 1024 * 50);    // Random upload bytes
        
        downloadBytes.addAndGet(download);
        uploadBytes.addAndGet(upload);
        totalBytes.addAndGet(download + upload);
    }

    public long getDownloadBytes() {
        return downloadBytes.get();
    }

    public long getUploadBytes() {
        return uploadBytes.get();
    }

    public long getTotalBytes() {
        return totalBytes.get();
    }

    public String getFormattedDownload() {
        return formatBytes(downloadBytes.get());
    }

    public String getFormattedUpload() {
        return formatBytes(uploadBytes.get());
    }

    public String getFormattedTotal() {
        return formatBytes(totalBytes.get());
    }

    private String formatBytes(long bytes) {
        if (bytes < 1024) return bytes + " B";
        if (bytes < 1024 * 1024) return String.format("%.1f KB", bytes / 1024.0);
        if (bytes < 1024 * 1024 * 1024) return String.format("%.1f MB", bytes / (1024.0 * 1024.0));
        return String.format("%.1f GB", bytes / (1024.0 * 1024.0 * 1024.0));
    }

    public void resetStats() {
        downloadBytes.set(0);
        uploadBytes.set(0);
        totalBytes.set(0);
    }
}
