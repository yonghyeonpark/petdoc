package yong.petdoc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class PetdocApplication {

    public static void main(String[] args) {
        SpringApplication.run(PetdocApplication.class, args);
    }

}
