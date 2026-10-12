package com.airtribe.ridewise;

import com.airtribe.ridewise.console.ConsoleMenu;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class RideWiseApplication {

    public static void main(String[] args) {
        SpringApplication.run(RideWiseApplication.class, args);
    }

    @Bean
    public CommandLineRunner runConsole(ConsoleMenu consoleMenu) {
        return args -> consoleMenu.run();
    }
}
