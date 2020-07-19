package com.udacity.jwdnd.course1.cloudstorage.controller;

import org.slf4j.Logger;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/error")
public class ErrorHandlerController implements ErrorController {

    private Logger logger;

    @GetMapping
    public String handleError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        logger.error("error status ", status);
        return "home";
    }

    @PostMapping
    public String handlePostError(HttpServletRequest request) {
        return "home";
    }

    @Override
    public String getErrorPath() {
        return null;
    }
}
