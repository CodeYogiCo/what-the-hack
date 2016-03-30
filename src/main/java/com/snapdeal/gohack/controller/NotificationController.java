package com.snapdeal.gohack.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.snapdeal.gohack.model.UserWebRegistration;

@RestController
@RequestMapping(value = "/web")
public class NotificationController {

    @Autowired
    NotificationService nService;

    @RequestMapping(value = "/user/register", method = RequestMethod.POST, headers = "application/json", produces = { "application/json" })
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody String doUserWebRegistration(@RequestBody UserWebRegistration userReg, HttpServletRequest request) throws Exception {

        return nService.doUserWebRegsitration(userReg);

    }

}
