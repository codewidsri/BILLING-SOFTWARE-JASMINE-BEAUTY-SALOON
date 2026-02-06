package com.billingapp.util;

import java.awt.Desktop;
import java.io.File;

public class PrinterUtil {

    public static void print(File pdf) throws Exception {
        // Desktop.getDesktop().print(pdf);
        Desktop.getDesktop().open(pdf);

    }
}
