package com.billingapp.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DatabaseBackupUtil {

    private static final String DB_FILE = "billing.db";
    private static final String BACKUP_DIR = "Billing_backups";

    /* ---------- BACKUP ---------- */
    public static File backupDatabase() throws IOException {

        File dbFile = new File(DB_FILE);
        if (!dbFile.exists()) {
            throw new IOException("Database file not found");
        }

        File backupDir = new File(System.getProperty("user.home"),BACKUP_DIR);
        if (!backupDir.exists()) {
            backupDir.mkdirs();
        }

        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm"));

        File backupFile = new File(backupDir, "billing_backup_" + timestamp + ".db");

        Files.copy(dbFile.toPath(), backupFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

        return backupFile;
    }

    /* ---------- RESTORE ---------- */
    public static void restoreDatabase(File backupFile) throws IOException {

        if (backupFile == null || !backupFile.exists()) {
            throw new IllegalArgumentException("Invalid backup file");
        }

        if (!backupFile.getName().toLowerCase().endsWith(".db")) {
            throw new IllegalArgumentException("Invalid file type. Please select a .db file");
        }

        try (var in = new FileInputStream(backupFile)) {
            byte[] header = new byte[16];
            if (in.read(header) != 16 || !new String(header).startsWith("SQLite format 3")) {
                throw new IllegalArgumentException("Selected file is not a valid SQLite database");
            }
        }

        File dbFile = new File(DB_FILE);

        Files.copy(backupFile.toPath(), dbFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }
}