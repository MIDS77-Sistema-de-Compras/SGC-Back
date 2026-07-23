package net.centroweg.gerenciamentocompras;

import java.util.TimeZone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GerenciamentocomprasApplication {

	public static void main(String[] args) {
		// Fixa o fuso padrão da JVM em UTC para que LocalDateTime.now()/@CreationTimestamp
		// gravem sempre o mesmo instante independente de onde o backend roda (máquina local
		// em BRT vs. container de produção em UTC) — o frontend converte esse valor pro fuso
		// do navegador assumindo que ele já está em UTC.
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
		SpringApplication.run(GerenciamentocomprasApplication.class, args);
	}


}



