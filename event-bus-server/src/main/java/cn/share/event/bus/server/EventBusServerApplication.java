package cn.share.event.bus.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan({"cn.share.event.bus"})
@SpringBootApplication
public class EventBusServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(EventBusServerApplication.class, args);
    }

}
