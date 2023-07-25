package com.team3.ecommerce;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AbstractExporter {

    /**
     * Thiết lập thông số trong phản hồi HTTP để xuất dữ liệu và đính kèm tệp tin.
     *
     * @param response    Phản hồi HTTP (HttpServletResponse) để thiết lập thông tin xuất dữ liệu.
     * @param contentType Loại nội dung của tệp tin muốn xuất, ví dụ: "application/pdf", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",...
     * @param extension   Phần mở rộng (extension) của tên tệp tin, ví dụ: ".pdf", ".xlsx",...
     * @param prefix      Tiền tố được thêm vào tên tệp tin để xác định mục đích của tệp và tránh trùng lặp, ví dụ: "report_", "data_",...
     * @throws IOException Nếu có lỗi trong việc thiết lập thông tin phản hồi HTTP.
     */
    public void setResponseHeader(HttpServletResponse response, String contentType, String extension, String prefix) throws IOException {
        // Định dạng thời gian hiện tại để tạo tiền tố cho tên tệp tin
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String timestamp = dateFormatter.format(new Date());
        // Tạo tên tệp tin bằng cách kết hợp tiền tố, thời gian và phần mở rộng
        String fileName = prefix + timestamp + extension;

        // Thiết lập loại nội dung (content type) của tệp tin trong phản hồi HTTP
        response.setContentType(contentType);

        // Thiết lập thông tin để tệp tin đính kèm có thể được tải về hoặc mở trực tiếp trên trình duyệt
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=" + fileName;
        response.setHeader(headerKey, headerValue);
    }
}
