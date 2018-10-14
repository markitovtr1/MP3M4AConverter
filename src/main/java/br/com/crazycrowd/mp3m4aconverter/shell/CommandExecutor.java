package br.com.crazycrowd.mp3m4aconverter.shell;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Singleton
public class CommandExecutor {

	private final Long waitTimeoutSeconds;

	@Inject
	public CommandExecutor(@Named("shellCommandsWaitSeconds") Long waitTimeoutSeconds) {
		this.waitTimeoutSeconds = waitTimeoutSeconds;
	}

	public void execute(ShellCommand command) throws IOException, InterruptedException, TimeoutException {
		log.debug("Starting execution of command {}", command);

		Process process = command.build().start();
		boolean ended = process.waitFor(waitTimeoutSeconds, TimeUnit.SECONDS);

		if (!ended) {
			throw new TimeoutException(String.format("Command %s did not end its execution on expected %d seconds.",
					command.getName(), waitTimeoutSeconds));
		}

		if (process.exitValue() != 0) {
			throw new IllegalArgumentException(
					String.format("Command %s ended with exit value %d.", command.getName(), process.exitValue()));
		}
	}

	public void execute(ShellCommand... commands) throws IOException, InterruptedException, TimeoutException {
		for (ShellCommand command : commands) {
			execute(command);
		}
	}

}
