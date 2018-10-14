package br.com.crazycrowd.mp3m4aconverter.shell;

import java.util.Objects;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractShellCommand implements ShellCommand {

	private ProcessBuilder processBuilder;

	private synchronized ProcessBuilder getProcessBuilder() {
		if (processBuilder == null) {
			processBuilder = new ProcessBuilder().command(getCommandAndArguments());
		}
		return processBuilder;
	}

	@Override
	public ProcessBuilder build() {
		log.trace("Creating process builder for command {}", getName());
		if (processBuilder == null) {
			return getProcessBuilder();
		}
		return processBuilder;
	}

	@Override
	public String toString() {
		return getCommandAndArguments().toString();
	}

	@Override
	public boolean equals(Object other) {
		if (other == this) {
			return true;
		}

		if (!(other instanceof ShellCommand)) {
			return false;
		}

		ShellCommand that = (ShellCommand) other;
		return Objects.equals(this.getCommandAndArguments(), that.getCommandAndArguments());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getName(), getCommandAndArguments());
	}

}
