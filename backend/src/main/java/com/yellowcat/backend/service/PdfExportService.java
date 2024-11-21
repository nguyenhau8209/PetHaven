package com.yellowcat.backend.service;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.LineSeparator;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
public class PdfExportService {


    public byte[] generateInvoice(String NgayThanhToan, String MaHoaDon, String PhuongThucThanhToan, String DichVu, double SoTien, String ThoiGian) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(outputStream);
            com.itextpdf.kernel.pdf.PdfDocument pdfDoc = new com.itextpdf.kernel.pdf.PdfDocument(writer);
            Document document = new Document(pdfDoc);

            // Load font hỗ trợ tiếng Việt
            String fontPath = "fonts/arial.ttf";
            PdfFont vietnameseFont = PdfFontFactory.createFont(fontPath, PdfEncodings.IDENTITY_H);

            // Tiêu đề hóa đơn
            document.add(new Paragraph("HÓA ĐƠN THANH TOÁN")
                    .setFont(vietnameseFont) // Cài đặt font tiếng Việt
                    .setBold()
                    .setFontSize(18)
                    .setMarginBottom(20));

            // Tạo bảng với 2 cột
            float[] columnWidths = {3, 7}; // Độ rộng các cột
            Table table = new Table(columnWidths)
                    .setWidth(500)
                    .setMarginBottom(10);

            // Thêm các hàng thông tin
            addTableRowWithBorder(table, "Ngày thanh toán", NgayThanhToan, vietnameseFont);
            addTableRowWithBorder(table, "Số đơn đặt hàng", MaHoaDon, vietnameseFont);
            addTableRowWithBorder(table, "Phương thức thanh toán", PhuongThucThanhToan, vietnameseFont);
            addTableRowWithBorder(table, "Dịch vụ", DichVu, vietnameseFont);
            addTableRowWithBorder(table, "Số tiền", String.valueOf(SoTien), vietnameseFont);
            addTableRowWithBorder(table, "Thời gian", ThoiGian, vietnameseFont);

            // Thêm bảng vào document
            document.add(table);

            // Đóng document
            document.close();
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi tạo PDF", e);
        }
    }

    // Hàm hỗ trợ thêm hàng vào bảng
    private void addTableRowWithBorder(Table table, String column1, String column2, PdfFont font) {
        table.addCell(new Cell().add(new Paragraph(column1).setFont(font))); // Cột 1
        table.addCell(new Cell().add(new Paragraph(column2).setFont(font))); // Cột 2
    }
}



