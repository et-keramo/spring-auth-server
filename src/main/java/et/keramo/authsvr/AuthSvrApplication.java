package et.keramo.authsvr;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Slf4j
@EnableEurekaClient
@SpringBootApplication
//@ComponentScan(
//		basePackages = { "et.keramo.authsvr" },
//		excludeFilters = @Filter(type = FilterType.ASPECTJ, pattern = "et.keramo.authsvr.service.user.*")
//)
public class AuthSvrApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(AuthSvrApplication.class, args);

		String[] names = context.getBeanDefinitionNames();
		String packageName = AuthSvrApplication.class.getPackage().toString().split(" ")[1];

		for (String name : names) {
			if (context.getBean(name).toString().contains(packageName)) {
				log.info("Registered Bean : " + name + " :: " + context.getBean(name));
			}
		}
	}

}
