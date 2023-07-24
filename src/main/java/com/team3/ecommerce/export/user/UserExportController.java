package com.team3.ecommerce.export.user;

import com.team3.ecommerce.entity.User;
import com.team3.ecommerce.export.UserCsvExporter;
import com.team3.ecommerce.export.UserExcelExporter;
import com.team3.ecommerce.export.UserPdfExporter;
import com.team3.ecommerce.service.AccountUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users/export")
@CrossOrigin("*")
public class UserExportController {
    @Autowired
    private AccountUserService accountUserService;

    @GetMapping("/csv")
    public void exportToCSV(HttpServletResponse response) throws IOException {
        List<User> listUsers = accountUserService.listAll();
        UserCsvExporter exporter = new UserCsvExporter();
        exporter.export(listUsers, response);
    }

    @GetMapping("/excel")
    public void exportToExcel(HttpServletResponse response) throws IOException {
        List<User> listUsers = accountUserService.listAll();

        UserExcelExporter exporter = new UserExcelExporter();
        exporter.export(listUsers, response);
    }

    @GetMapping("/pdf")
    public void exportToPDF(HttpServletResponse response) throws IOException {
        List<User> listUsers = accountUserService.listAll();

        UserPdfExporter exporter = new UserPdfExporter();
        exporter.export(listUsers, response);
    }
}
