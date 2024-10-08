package com.testcoin.demo;

import com.testcoin.demo.task.InitialCoinImport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class TestCoinApplication implements ApplicationRunner {

	@Autowired
	InitialCoinImport initialCoinImport;

	public static void main(String[] args) {
		SpringApplication.run(TestCoinApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {

		initialCoinImport.run();
	}

}
