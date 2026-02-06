package com.billingapp.ui.admin.importexporttab;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import com.billingapp.util.DatabaseBackupUtil;

public class ImportExportDataTab extends JPanel {

    JButton backup, restore;

    public ImportExportDataTab() {
        setBorder(new EmptyBorder(10, 10, 10, 10));

        backup = new JButton("Backup Database");
        backup.setPreferredSize(new Dimension(150, 60));
        backup.setMaximumSize(new Dimension(150, 60));
        backup.setFont(new Font("Segoe UI", Font.BOLD, 15));
        backup.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backup.setForeground(Color.WHITE);
        backup.setBorder(new LineBorder(new Color(13, 110, 253), 0, true));
        backup.setBackground(new Color(13, 110, 253));
        backup.addActionListener(e -> {
            try {
                File backup = DatabaseBackupUtil.backupDatabase();
                JOptionPane.showMessageDialog(
                        null,
                        "Backup successful!\nSaved at:\n" + backup.getAbsolutePath(),
                        "Backup",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                        null,
                        ex.getMessage(),
                        "Backup Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        restore = new JButton("Restore");
        restore.setPreferredSize(new Dimension(150, 60));
        restore.setMaximumSize(new Dimension(150, 60));
        restore.setFont(new Font("Segoe UI", Font.BOLD, 15));
        restore.setCursor(new Cursor(Cursor.HAND_CURSOR));
        restore.setForeground(Color.WHITE);
        restore.setBorder(new LineBorder(new Color(13, 110, 253), 0, true));
        restore.setBackground(Color.RED);
        restore.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser("backups");
            int choice = chooser.showOpenDialog(null);

            if (choice != JFileChooser.APPROVE_OPTION)
                return;

            int confirm = JOptionPane.showConfirmDialog(
                    null,
                    """
                            Restoring will overwrite current data.
                            Application must restart.

                            Continue?
                            """,
                    "Confirm Restore",
                    JOptionPane.YES_NO_OPTION);

            if (confirm != JOptionPane.YES_OPTION)
                return;

            try {
                DatabaseBackupUtil.restoreDatabase(chooser.getSelectedFile());

                JOptionPane.showMessageDialog(
                        null,
                        "Restore completed.\nApplication will now close.",
                        "Restore",
                        JOptionPane.INFORMATION_MESSAGE);

                System.exit(0); // 🔴 REQUIRED

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                        null,
                        ex.getMessage(),
                        "Restore Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        add(backup);
        add(restore);
    }
}
