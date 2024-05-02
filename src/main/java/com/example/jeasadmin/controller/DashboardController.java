package com.example.jeasadmin.controller;

import com.example.jeasadmin.model.JeasUser;
import com.example.jeasadmin.service.JeasUserService;
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
    JeasUserService jeasUserService;

    @GetMapping("/customer/{id}")
    public String getCustomer(@PathVariable String id, Model model) throws ExecutionException, InterruptedException, IOException {
        model.addAttribute("jeasUser", jeasUserService.getJeasUserDetails(id, "customers"));
        model.addAttribute("id_image", jeasUserService.getPhoto(id));
        model.addAttribute("type", "customers");
        return "jeasUser";
    }

    @PostMapping("/customer/{id}")
    public String updateCustomer(RedirectAttributes redirectAttributes,
                                 @PathVariable String id,
                                 @RequestParam("status") String status) throws ExecutionException, InterruptedException {
        JeasUser customer = jeasUserService.getJeasUserDetails(id, "customers");
        customer.setStatus(status);
        jeasUserService.updateJeasUserDetails(customer, "customers");
        redirectAttributes.addAttribute("id", id);
        return "redirect:/dashboard/customer/{id}";
    }

    @GetMapping("/worker/{id}")
    public String getWorker(@PathVariable String id, Model model) throws ExecutionException, InterruptedException, IOException {
        model.addAttribute("jeasUser", jeasUserService.getJeasUserDetails(id, "workers"));
        model.addAttribute("id_image", jeasUserService.getPhoto(id));
        model.addAttribute("type", "workers");
        return "jeasUser";
    }

    @PostMapping("/worker/{id}")
    public String updateWorker(RedirectAttributes redirectAttributes,
                                 @PathVariable String id,
                                 @RequestParam("status") String status) throws ExecutionException, InterruptedException {
        JeasUser worker = jeasUserService.getJeasUserDetails(id, "workers");
        worker.setStatus(status);
        jeasUserService.updateJeasUserDetails(worker, "workers");
        redirectAttributes.addAttribute("id", id);
        return "redirect:/dashboard/worker/{id}";
    }

    @GetMapping("/customer")
    public String getAllCustomers(Model model) throws ExecutionException, InterruptedException {
        model.addAttribute("jeasUsers", jeasUserService.getAllJeasUsers("customers"));
        model.addAttribute("type", "customers");
        return "jeasUsers";
    }

    @GetMapping("/worker")
    public String getAllWorkers(Model model) throws ExecutionException, InterruptedException {
        model.addAttribute("jeasUsers", jeasUserService.getAllJeasUsers("workers"));
        model.addAttribute("type", "workers");
        return "jeasUsers";
    }
}
