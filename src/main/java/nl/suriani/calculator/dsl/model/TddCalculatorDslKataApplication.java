package nl.suriani.calculator.dsl.model;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TddCalculatorDslKataApplication {
    public static void main(String[] args) {
        var application = new SpringApplication(TddCalculatorDslKataApplication.class);
        application.run(args);
    }
}
