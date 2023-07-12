package in.shelfpay.lobymaker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "in.shelfpay.lobymaker")
public class LobymakerApplication {

	public static void main(String[] args) {
		SpringApplication.run(LobymakerApplication.class, args);
	}

}
