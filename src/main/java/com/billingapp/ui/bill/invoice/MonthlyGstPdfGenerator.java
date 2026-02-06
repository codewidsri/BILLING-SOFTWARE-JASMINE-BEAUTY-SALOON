package com.billingapp.ui.bill.invoice;

import java.io.File;
import java.util.ArrayList;

import com.billingapp.model.Bill;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.properties.AreaBreakType;

public class MonthlyGstPdfGenerator {

    public static void generateMonthlyReport(ArrayList<Bill> bills, File outputFile) throws Exception {

        PdfWriter writer = new PdfWriter(outputFile);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf, PageSize.A4);

        for (int i = 0; i < bills.size(); i++) {

            InvoicePdfGenerator.renderBill(document, bills.get(i));

            if (i < bills.size() - 1) {
                document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
            }
        }

        document.close();
    }
}
