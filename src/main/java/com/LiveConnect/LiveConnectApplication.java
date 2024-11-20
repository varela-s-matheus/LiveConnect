package com.LiveConnect;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.LiveConnect")
public class LiveConnectApplication {

	public static void main(String[] args) {
		SpringApplication.run(LiveConnectApplication.class, args);
	}

}
