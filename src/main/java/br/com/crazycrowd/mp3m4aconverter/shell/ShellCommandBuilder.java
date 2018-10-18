package br.com.crazycrowd.mp3m4aconverter.shell;

import java.lang.ProcessBuilder.Redirect;

import javax.inject.Singleton;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Singleton
public class ShellCommandBuilder {

	public ProcessBuilder build(ShellCommand command) {
		log.trace("Creating process builder for command {}", command.getName());
		return new ProcessBuilder().command(command.getCommandAndArguments())
				// This is a fix needed for NeroAacEnc command to finish accordingly
				.redirectError(Redirect.INHERIT);
	}

}
