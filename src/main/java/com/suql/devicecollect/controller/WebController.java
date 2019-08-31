package com.suql.devicecollect.controller;

import com.suql.devicecollect.request.RxDeviceCount;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/web")
public class WebController {

    @GetMapping("/index")
    public String index(Model model) {
        model.addAttribute("deviceCount", new RxDeviceCount());
        return "index";
    }

        @PostMapping("/print")
    @ResponseBody
    public String print(String username, String password) {
        return username + "-" + password;
    }
}
