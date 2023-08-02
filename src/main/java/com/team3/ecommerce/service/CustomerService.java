package com.team3.ecommerce.service;

import com.team3.ecommerce.entity.AuthenticationType;
import com.team3.ecommerce.entity.Country;
import com.team3.ecommerce.entity.Customer;
import com.team3.ecommerce.repository.CountryRepository;
import com.team3.ecommerce.repository.CustomerRepository;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@Transactional
public class CustomerService {
    @Autowired
    private CountryRepository countryRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private EmailService emailService;

    public String randomCodeEmail(String email) {
        Random rand = new Random();
        int randomNum = rand.nextInt(900000) + 100000;
        String to = email;
        String subject = "Ecommerce";
        String text = String.valueOf(randomNum);
        String content = "Xin Chào ...!\n" +
                "Bạn hoặc ai đó đã dùng email này để đăng ký tài khoản ở trung tâm TMDT-Ecommerce\n" +
                "\n" +
                "MÃ XÁC NHẬN CỦA BẠN LÀ  : " + text + "\n" +
                "Nhấn vào Link này để kích hoạt nhanh: " +
                "http://localhost:8080/api/v1/customers/do-activation?code=" + text +
                "\n" +
                "--------------------------------------\n" +
                " + Phone  : (+084)Dũng.Dũng.Dũng\n" +
                " + Email  : Ecommerce.codegym.vn@gmail.com\n" +
                " + Address: Lô TT-04 Số 23 Khu Đô Thị MonCity\n";
        emailService.sendMail(to, subject, content);
        return text;

    }
    public String randomCodeSMS(String phoneNumber) {
        Random rand = new Random();
        int randomNum = rand.nextInt(900000) + 100000;
        String text = String.valueOf(randomNum);

        // Khởi tạo Twilio client với ACCOUNT_SID và AUTH_TOKEN của bạn
        Twilio.init("AC72dffa0e8f9367aed92e460e2a3f884d", "1a946f50efd050a43e63202d5bccd596");

        // Sử dụng Twilio API để gửi tin nhắn SMS chứa mã OTP đến số điện thoại được chỉ định
        Message message = Message.creator(
                        new PhoneNumber(phoneNumber), // Số điện thoại đích
                        new PhoneNumber("+14068127325"), // Số điện thoại gửi (đã được cấp bởi Twilio)
                        "Ecommerce - Mã xác nhận của bạn là: " + text) // Nội dung tin nhắn
                .create();
        return text;

    }

    public Optional<Customer> getCustomerById(Integer id) {
        return customerRepository.findById(id);
    }// lấy ra một customer

    public List<Country> listAllCountries() {
        return countryRepository.findAllByOrderByNameAsc();
    } // danh sách quốc gia

    public boolean isEmailUnique(String email) {
        Customer customer = customerRepository.findByEmail(email);
        return customer == null;
    } // kiểm tra email là duy nhất

    public void registerCustomer(Customer customer) {
        customer.setEnabled(false);
        customer.setCreatedTime(new Date());
        customer.setAuthenticationType(AuthenticationType.DATABASE);
        customer.setAddressLine1(" ");
        customer.setAddressLine2(" ");
        customer.setCity(" ");
        customer.setPostalCode(" ");

        String confirmationCode = randomCodeEmail(customer.getEmail());
        customer.setVerificationCode(confirmationCode);

        customerRepository.save(customer);
    }

    public Customer activateCustomer(String code) {
        Customer user = customerRepository.findByVerificationCode(code);
        if (user != null && !user.isEnabled()) { // Kiểm tra người dùng tồn tại và chưa kích hoạt
            // Khi người dùng xác nhận mã, ta cần kích hoạt trạng thái của người dùng
            user.setEnabled(true);
            // Cập nhật thông tin người dùng đã kích hoạt vào cơ sở dữ liệu
            customerRepository.save(user);
        }
        return user;
    }

    public Customer findCustomerByEmail(String email) {
        return customerRepository.findByEmail(email);
    }
    // đăng kí tài khoản user customer mới

    // tìm kiếm Customer theo id
    public Optional<Customer> findById(Integer id) {
        return customerRepository.findById(id);
    }

    public Customer saveCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    public void updateUserEnabledStatus(Integer id, boolean enabled){
        customerRepository.updateEnabledStatus(id, enabled);
    }

    public void deleteCustomerById(Integer id) {
        customerRepository.deleteById(id);
    }
}
