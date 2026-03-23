package co.edu.udistrital.mdp.pets;

import java.net.BindException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
public class MainApplication {
	private static final String DEFAULT_PORT = "8080";
	private static final String SERVER_PORT_PREFIX = "--server.port=";

	public static void main(String[] args) {
		try {
			SpringApplication.run(MainApplication.class, ensureDefaultPortArg(args));
		} catch (RuntimeException exception) {
			if (isPortConflict(exception)) {
				log.warn("Puerto {} ocupado. Se inicia en puerto dinámico y se debe revisar el log de arranque para el puerto asignado.",
						DEFAULT_PORT);
				SpringApplication.run(MainApplication.class, withPort(args, "0"));
				return;
			}
			throw exception;
		}
	}

	private static String[] ensureDefaultPortArg(String[] args) {
		for (String arg : args) {
			if (arg != null && arg.startsWith(SERVER_PORT_PREFIX)) {
				return args;
			}
		}
		return withPort(args, DEFAULT_PORT);
	}

	private static String[] withPort(String[] args, String port) {
		List<String> finalArgs = new ArrayList<>();
		for (String arg : args) {
			if (arg == null || !arg.startsWith(SERVER_PORT_PREFIX)) {
				finalArgs.add(arg);
			}
		}
		finalArgs.add(SERVER_PORT_PREFIX + port);
		return finalArgs.toArray(String[]::new);
	}

	private static boolean isPortConflict(Throwable throwable) {
		Throwable current = throwable;
		while (current != null) {
			if (current instanceof BindException) {
				return true;
			}
			current = current.getCause();
		}
		return false;
	}

}
