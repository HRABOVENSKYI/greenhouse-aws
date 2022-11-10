package ua.lviv.iot.greenhouse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.aws.autoconfigure.context.ContextStackAutoConfiguration;

@SpringBootApplication(exclude = {ContextStackAutoConfiguration.class})
public class GreenHouseApplication {

    public static void main(String[] args) {
        SpringApplication.run(GreenHouseApplication.class, args);
    }

}
