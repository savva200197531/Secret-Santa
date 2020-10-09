package ru.cft.shift.santa;

import lombok.extern.java.Log;
import org.h2.tools.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import java.sql.SQLException;

@SpringBootApplication
@Log
public class SpringbootSampleApplication extends SpringBootServletInitializer {
    public static void main(String[] args) {
        startH2Server();
        SpringApplication.run(SpringbootSampleApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        startH2Server();
        return application.sources(SpringbootSampleApplication.class);
    }

    private static void startH2Server() {
        try {
            Server h2Server = Server.createTcpServer("-tcp", "-tcpAllowOthers", "-tcpPort", "6868").start();
            if (h2Server.isRunning(true)) {
                log.info("H2 server was started and is running.");
            } else {
                throw new RuntimeException("Could not start H2 server.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to start H2 server: ", e);
        }
    }
}