package com.billingapp.ui.bill.invoice;

import java.io.File;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import com.billingapp.model.Bill;
import com.billingapp.model.BillItems;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.*;

public class InvoicePdfGenerator {

        private static final BigDecimal GST_RATE = new BigDecimal("5"); // 5% inclusive

        public static void generateInvoice(Bill bill, File file) throws Exception {

                PdfWriter writer = new PdfWriter(file);
                PdfDocument pdf = new PdfDocument(writer);
                Document document = new Document(pdf, PageSize.A4);
                document.setMargins(20, 20, 20, 20);

                PdfFont boldFont = PdfFontFactory.createFont("Helvetica-Bold");
                PdfFont normalFont = PdfFontFactory.createFont("Helvetica");

                /* ---------- HEADER ---------- */
                // ---------- LOAD LOGO FROM JAR RESOURCES ----------
                InputStream logoStream = InvoicePdfGenerator.class
                                .getResourceAsStream("/assets/images/logo.png");

                if (logoStream == null) {
                        throw new RuntimeException("Logo image not found in resources");
                }

                ImageData logoData = ImageDataFactory.create(
                                logoStream.readAllBytes());

                Image logo = new Image(logoData).scaleToFit(130, 130);

                Paragraph shop = new Paragraph()
                                .setFont(boldFont)
                                .add("JASMINE BEAUTY SALON\n")
                                .setFont(normalFont)
                                .add("1st Floor, City Union Bank,\n")
                                .add("Near Padma Theatre Bus Stop, Villapuram\n")
                                .add("Madurai - 625012\n")
                                .add("Phone: 9629226991\n")
                                .add("Email: jasminebeautysalon369@gmail.com\n")
                                .add("GSTIN : 33CXKPM1810P1Z8")
                                .setTextAlignment(TextAlignment.RIGHT);

                Table header = new Table(UnitValue.createPercentArray(new float[] { 2, 5 }))
                                .useAllAvailableWidth();

                header.addCell(new Cell().add(logo).setBorder(Border.NO_BORDER));
                header.addCell(new Cell().add(shop).setBorder(Border.NO_BORDER));
                document.add(header);

                document.add(new Paragraph("\n"));

                /* ---------- BILL INFO ---------- */
                Table info = new Table(UnitValue.createPercentArray(new float[] { 1, 1 }))
                                .useAllAvailableWidth();

                Cell customerCell = new Cell()
                                .add(new Paragraph(
                                                "Bill To:\n" +
                                                                bill.getCustomerName() +
                                                                "\nPhone: " + bill.getCustomerMobileNumber()))
                                .setBorder(Border.NO_BORDER)
                                .setTextAlignment(TextAlignment.LEFT);

                Cell invoiceCell = new Cell()
                                .add(new Paragraph(
                                                "Invoice No: " + bill.getInvoiceNumber() +
                                                                "\nDate: " + LocalDate.now()))
                                .setBorder(Border.NO_BORDER)
                                .setTextAlignment(TextAlignment.RIGHT);

                info.addCell(customerCell);
                info.addCell(invoiceCell);

                document.add(info);

                document.add(new Paragraph("\n"));

                /* ---------- ITEMS ---------- */
                Table items = new Table(UnitValue.createPercentArray(
                                new float[] { 1, 3, 3, 2, 2, 2 }))
                                .useAllAvailableWidth();

                addHeader(items, "#", boldFont);
                addHeader(items, "Service", boldFont);
                addHeader(items, "Sub Service", boldFont);
                addHeader(items, "Qty", boldFont);
                addHeader(items, "Price", boldFont);
                addHeader(items, "Total", boldFont);

                BigDecimal grossTotal = BigDecimal.ZERO;
                int index = 1;

                for (BillItems it : bill.getBillItems()) {
                        BigDecimal rowTotal = it.getPrice()
                                        .multiply(BigDecimal.valueOf(it.getQuantity()));

                        grossTotal = grossTotal.add(rowTotal);

                        items.addCell(cell(String.valueOf(index++)));
                        items.addCell(cell(it.getServiceName()));
                        items.addCell(cell(it.getSubServiceName()));
                        items.addCell(cell(String.valueOf(it.getQuantity())));
                        items.addCell(cell(it.getPrice().toString()));
                        items.addCell(cell(rowTotal.toString()));
                }

                document.add(items);
                document.add(new Paragraph("\n"));

                // ---------- GST BREAKUP (INCLUSIVE) ----------
                BigDecimal hundred = BigDecimal.valueOf(100);
                BigDecimal divisor = hundred.add(GST_RATE); // 100 + GST_RATE

                BigDecimal taxableAmount = grossTotal.multiply(hundred).divide(divisor, 2, RoundingMode.HALF_UP);

                BigDecimal gstAmount = grossTotal.subtract(taxableAmount);

                BigDecimal cgst = gstAmount.divide(BigDecimal.valueOf(2), 2, RoundingMode.HALF_UP);

                BigDecimal sgst = gstAmount.divide(BigDecimal.valueOf(2), 2, RoundingMode.HALF_UP);

                Table totals = new Table(UnitValue.createPercentArray(new float[] { 3, 1 }))
                                .setWidth(UnitValue.createPercentValue(40))
                                .setHorizontalAlignment(HorizontalAlignment.RIGHT);

                totals.addCell(total("Taxable Amount"));
                totals.addCell(total(taxableAmount));

                totals.addCell(total("CGST (2.5%)"));
                totals.addCell(total(cgst));

                totals.addCell(total("SGST (2.5%)"));
                totals.addCell(total(sgst));

                totals.addCell(totalBold("Grand Total", boldFont));
                totals.addCell(totalBold(grossTotal, boldFont));

                document.add(totals);

                document.close();
        }

        public static void renderBill(Document document, Bill bill) throws Exception {
                document.setMargins(20, 20, 20, 20);

                PdfFont boldFont = PdfFontFactory.createFont("Helvetica-Bold");
                PdfFont normalFont = PdfFontFactory.createFont("Helvetica");

                /* ---------- HEADER ---------- */
                // ---------- LOAD LOGO FROM JAR RESOURCES ----------
                InputStream logoStream = InvoicePdfGenerator.class
                                .getResourceAsStream("/assets/images/logo.png");

                if (logoStream == null) {
                        throw new RuntimeException("Logo image not found in resources");
                }

                ImageData logoData = ImageDataFactory.create(
                                logoStream.readAllBytes());

                Image logo = new Image(logoData).scaleToFit(130, 130);

                Paragraph shop = new Paragraph()
                                .setFont(boldFont)
                                .add("JASMINE BEAUTY SALON\n")
                                .setFont(normalFont)
                                .add("1st Floor, City Union Bank,\n")
                                .add("Near Padma Theatre Bus Stop, Villapuram\n")
                                .add("Madurai - 625012\n")
                                .add("Phone: 9629226991\n")
                                .add("Email: jasminebeautysalon369@gmail.com\n")
                                .add("GSTIN : 33CXKPM1810P1Z8")
                                .setTextAlignment(TextAlignment.RIGHT);

                Table header = new Table(UnitValue.createPercentArray(new float[] { 2, 5 }))
                                .useAllAvailableWidth();

                header.addCell(new Cell().add(logo).setBorder(Border.NO_BORDER));
                header.addCell(new Cell().add(shop).setBorder(Border.NO_BORDER));
                document.add(header);

                document.add(new Paragraph("\n"));

                /* ---------- BILL INFO ---------- */
                Table info = new Table(UnitValue.createPercentArray(new float[] { 1, 1 }))
                                .useAllAvailableWidth();

                Cell customerCell = new Cell()
                                .add(new Paragraph(
                                                "Bill To:\n" +
                                                                bill.getCustomerName() +
                                                                "\nPhone: " + bill.getCustomerMobileNumber()))
                                .setBorder(Border.NO_BORDER)
                                .setTextAlignment(TextAlignment.LEFT);

                Cell invoiceCell = new Cell()
                                .add(new Paragraph(
                                                "Invoice No: " + bill.getInvoiceNumber() +
                                                                "\nDate: " + LocalDate.now()))
                                .setBorder(Border.NO_BORDER)
                                .setTextAlignment(TextAlignment.RIGHT);

                info.addCell(customerCell);
                info.addCell(invoiceCell);

                document.add(info);

                document.add(new Paragraph("\n"));

                /* ---------- ITEMS ---------- */
                Table items = new Table(UnitValue.createPercentArray(
                                new float[] { 1, 3, 3, 2, 2, 2 }))
                                .useAllAvailableWidth();

                addHeader(items, "#", boldFont);
                addHeader(items, "Service", boldFont);
                addHeader(items, "Sub Service", boldFont);
                addHeader(items, "Qty", boldFont);
                addHeader(items, "Price", boldFont);
                addHeader(items, "Total", boldFont);

                BigDecimal grossTotal = BigDecimal.ZERO;
                int index = 1;

                for (BillItems it : bill.getBillItems()) {
                        BigDecimal rowTotal = it.getPrice()
                                        .multiply(BigDecimal.valueOf(it.getQuantity()));

                        grossTotal = grossTotal.add(rowTotal);

                        items.addCell(cell(String.valueOf(index++)));
                        items.addCell(cell(it.getServiceName()));
                        items.addCell(cell(it.getSubServiceName()));
                        items.addCell(cell(String.valueOf(it.getQuantity())));
                        items.addCell(cell(it.getPrice().toString()));
                        items.addCell(cell(rowTotal.toString()));
                }

                document.add(items);
                document.add(new Paragraph("\n"));

                // ---------- GST BREAKUP (INCLUSIVE) ----------
                BigDecimal hundred = BigDecimal.valueOf(100);
                BigDecimal divisor = hundred.add(GST_RATE); // 100 + GST_RATE

                BigDecimal taxableAmount = grossTotal.multiply(hundred).divide(divisor, 2, RoundingMode.HALF_UP);

                BigDecimal gstAmount = grossTotal.subtract(taxableAmount);

                BigDecimal cgst = gstAmount.divide(BigDecimal.valueOf(2), 2, RoundingMode.HALF_UP);

                BigDecimal sgst = gstAmount.divide(BigDecimal.valueOf(2), 2, RoundingMode.HALF_UP);

                Table totals = new Table(UnitValue.createPercentArray(new float[] { 3, 1 }))
                                .setWidth(UnitValue.createPercentValue(40))
                                .setHorizontalAlignment(HorizontalAlignment.RIGHT);

                totals.addCell(total("Taxable Amount"));
                totals.addCell(total(taxableAmount));

                totals.addCell(total("CGST (2.5%)"));
                totals.addCell(total(cgst));

                totals.addCell(total("SGST (2.5%)"));
                totals.addCell(total(sgst));

                totals.addCell(totalBold("Grand Total", boldFont));
                totals.addCell(totalBold(grossTotal, boldFont));

                document.add(totals);
        }

        /* ---------- HELPERS ---------- */

        private static void addHeader(Table table, String text, PdfFont font) {
                table.addHeaderCell(new Cell()
                                .add(new Paragraph(text).setFont(font))
                                .setBackgroundColor(ColorConstants.LIGHT_GRAY)
                                .setTextAlignment(TextAlignment.CENTER));
        }

        private static Cell cell(String text) {
                return new Cell()
                                .add(new Paragraph(text))
                                .setTextAlignment(TextAlignment.CENTER);
        }

        private static Cell total(Object value) {
                return new Cell()
                                .add(new Paragraph(value.toString()))
                                .setBorder(Border.NO_BORDER)
                                .setTextAlignment(TextAlignment.RIGHT);
        }

        private static Cell totalBold(Object value, PdfFont font) {
                return new Cell()
                                .add(new Paragraph(value.toString()).setFont(font))
                                .setBorder(Border.NO_BORDER)
                                .setTextAlignment(TextAlignment.RIGHT);
        }
}
