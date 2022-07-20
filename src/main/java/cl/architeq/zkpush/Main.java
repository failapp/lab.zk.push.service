package cl.architeq.zkpush;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.TimeZone;

@EnableAsync
@EnableScheduling
@SpringBootApplication
public class Main {

	public static void main(String[] args) {
		//TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
		SpringApplication.run(Main.class, args);
	}

}
