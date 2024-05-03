package com.example.jeasadmin.controller;

import com.example.jeasadmin.model.User;
import com.example.jeasadmin.model.NotificationRequest;
import com.example.jeasadmin.model.Worker;
import com.example.jeasadmin.service.FCMService;
import com.example.jeasadmin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {
    @Autowired
    UserService userService;

    @Autowired
    private FCMService fcmService;

    @GetMapping("/customer/{id}")
    public String getCustomer(@PathVariable String id, Model model) throws ExecutionException, InterruptedException, IOException {
        model.addAttribute("user", userService.getUserDetails(id, "customers"));
        model.addAttribute("id_image", userService.getPhoto(id));
        model.addAttribute("type", "customers");
        return "user";
    }

    @PostMapping("/customer/{id}")
    public String updateCustomer(RedirectAttributes redirectAttributes,
                                 @PathVariable String id,
                                 @RequestParam("status") String status) throws ExecutionException, InterruptedException {
        User customer = (User) userService.getUserDetails(id, "customers");
        customer.setStatus(status);
        userService.updateUserDetails(customer, "customers");
        fcmService.sendMessageToToken(new NotificationRequest("Account Status Update", "Your account with email: " + customer.getEmail() + " status has been changed to " + status, "", customer.getFcmToken()));
        redirectAttributes.addAttribute("id", id);
        return "redirect:/dashboard/customer/{id}";
    }

    @GetMapping("/worker/{id}")
    public String getWorker(@PathVariable String id, Model model) throws ExecutionException, InterruptedException, IOException {
        model.addAttribute("user", userService.getUserDetails(id, "workers"));
        model.addAttribute("id_image", userService.getPhoto(id));
        model.addAttribute("type", "workers");
        return "user";
    }

    @PostMapping("/worker/{id}")
    public String updateWorker(RedirectAttributes redirectAttributes,
                                 @PathVariable String id,
                                 @RequestParam("status") String status) throws ExecutionException, InterruptedException {
        Worker worker = (Worker) userService.getUserDetails(id, "workers");
        worker.setStatus(status);
        userService.updateUserDetails(worker, "workers");
        fcmService.sendMessageToToken(new NotificationRequest("Account Status", "Your account with email: " + worker.getEmail() + " status has been changed to " + status, "", worker.getFcmToken()));
        redirectAttributes.addAttribute("id", id);
        return "redirect:/dashboard/worker/{id}";
    }

    @GetMapping("/customer")
    public String getAllCustomers(Model model) throws ExecutionException, InterruptedException {
        model.addAttribute("users", userService.getAllUsers("customers"));
        model.addAttribute("type", "customers");
        return "users";
    }

    @GetMapping("/worker")
    public String getAllWorkers(Model model) throws ExecutionException, InterruptedException {
        model.addAttribute("users", userService.getAllUsers("workers"));
        model.addAttribute("type", "workers");
        return "users";
    }
}
