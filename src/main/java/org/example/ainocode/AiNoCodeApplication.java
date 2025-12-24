package org.example.ainocode;

import dev.langchain4j.community.store.embedding.redis.spring.RedisEmbeddingStoreAutoConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@SpringBootApplication(exclude = RedisEmbeddingStoreAutoConfiguration.class)
@MapperScan("org.example.ainocode.mapper")
public class AiNoCodeApplication {

    public static void main(String[] args) {
        SpringApplication.run(AiNoCodeApplication.class, args);
    }

}
