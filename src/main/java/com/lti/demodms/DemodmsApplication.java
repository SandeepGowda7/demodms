package com.lti.demodms;

import com.lti.demodms.model.FileStorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({
    FileStorageProperties.class
})


public class DemodmsApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemodmsApplication.class, args);
	}

}
