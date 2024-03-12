package fontys.emergencywebapps;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import fontys.emergencywebapps.configurations.AppProperties;

@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
public class EmergencyWebAppsApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmergencyWebAppsApplication.class, args);
	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}
}
