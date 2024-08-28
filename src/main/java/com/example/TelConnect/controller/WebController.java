package com.example.TelConnect.controller;

import com.example.TelConnect.model.Demo;
import com.example.TelConnect.service.DemoService;
import jakarta.validation.Valid;
import com.example.TelConnect.model.Customer;
import com.example.TelConnect.model.LoginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import com.example.TelConnect.service.CustomerService;

import java.util.List;

@RestController
@RequestMapping("/")
public class WebController {

    private final CustomerService customerService;
    private final DemoService demoService;

    @Autowired
    public WebController(CustomerService customerService, DemoService demoService) {
        this.customerService = customerService;
        this.demoService= demoService;
    }

    // Handler method to handle login request
    @PostMapping("/login")
    public ResponseEntity<String> handleLogin(@RequestBody LoginRequest loginRequest) {
        String email = loginRequest.getCustomerEmail();
        String password = loginRequest.getPassword();
        try {
            // Authenticate the customer using your custom service
            int authenticate = customerService.authenticateCustomer(email, password);

            // If authentication is successful
            if (authenticate == 1) {
                return ResponseEntity.ok("Login successful");

                // If authentication fails
            } else if (authenticate == 0) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");

                //Error in login
            } else if (authenticate ==-1) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User does not exist");
            }
        } catch (Exception e) {
            // Handle unexpected errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

        // Default return in case none of the conditions are met
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred during login");
    }

//    @GetMapping("verifyEmail")
//    public ResponseEntity<String> verifyCustomerEmail(@RequestParam String customerEmail){
//
//    }



    // Handler method to handle customer registration after verification of email
    @PostMapping("/register")
    public ResponseEntity<String> registerCustomer(@Valid @RequestBody Customer customer,
                                                   BindingResult result) {
        Customer existingCustomer = customerService.getByCustomerEmail(customer.getCustomerEmail());

        if (existingCustomer != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("There is already an account registered with the same email");
        }

        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body("Invalid registration data");
        }

        customerService.saveCustomer(customer);
        return ResponseEntity.status(HttpStatus.CREATED).body("Customer registered successfully");
    }

    // Handler method to get list of customers
    @GetMapping("/customers")
    public ResponseEntity<List<Customer>> getCustomers() {
        List<Customer> customers = customerService.findAllCustomers();
        return ResponseEntity.ok(customers);
    }

    // Handler method to handle logout
    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        return ResponseEntity.ok("Logged out successfully");
    }

//    @GetMapping("/demo")
//    public ResponseEntity<List<Demo>> demo(){
//        List<Demo> demo = demoService.getAll();
//        return ResponseEntity.ok(demo);
//    }

//    @PostMapping("/demo/{usn}")
//    public ResponseEntity<Demo> getDemo(@PathVariable("usn") String usn){
//        Demo demo = demoService.getByUsn(usn);
//        return ResponseEntity.ok(demo);
//    }
}
