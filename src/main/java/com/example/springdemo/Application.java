package com.example.springdemo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
    private static final Logger logger = LogManager.getLogger(Application.class);

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);

        logger.info("I AM INFO :||||||||");
        logger.debug("I AM DEBUG ;>>>>>>>>");
        logger.warn("I AM WARN :DDDDDDDD");
        logger.error("I AM ERROR :(((((((");
        logger.fatal("I AM FATAL :O");
    }

}
