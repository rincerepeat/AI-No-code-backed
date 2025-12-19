package org.example.ainocode;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@MapperScan("org.example.ainocode.mapper")
@SpringBootApplication
public class AiNoCodeApplication {

    public static void main(String[] args) {
        SpringApplication.run(AiNoCodeApplication.class, args);
    }

}
