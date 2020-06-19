package com.monika.rest_services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SquareControllerAPI {

    private static Logger LOG = LoggerFactory.getLogger(SquareControllerAPI.class);

    //URL: http://localhost:8080/square?num=10
    @GetMapping("/square")
    public int getSquare(@RequestParam(value = "num", defaultValue = "1") int num) {
        LOG.info("Square of " + num + ": " + num*num);
        return num*num;
    }
}
