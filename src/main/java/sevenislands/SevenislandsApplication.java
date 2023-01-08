package sevenislands;

import java.io.FileNotFoundException;
import java.io.PrintStream;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication()
public class SevenislandsApplication {

	public static void main(String[] args) throws FileNotFoundException {
		System.out.println("Aplicación iniciada...");
		PrintStream printStream = new PrintStream("./logs/log.txt");
		System.setOut(printStream);
		SpringApplication.run(SevenislandsApplication.class, args);
	}

}
