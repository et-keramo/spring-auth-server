package et.keramo.authsvr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class AuthSvrApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthSvrApplication.class, args);
	}

}
