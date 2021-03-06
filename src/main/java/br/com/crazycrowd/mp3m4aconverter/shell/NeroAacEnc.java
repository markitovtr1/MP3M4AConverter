package br.com.crazycrowd.mp3m4aconverter.shell;

import java.nio.file.Path;
import java.util.List;

import com.google.common.collect.ImmutableList;

import lombok.Getter;
import lombok.NonNull;

public class NeroAacEnc implements ShellCommand {

	private static final String COMMAND = "neroAacEnc";

	@NonNull
	private final Path inputFilePath;

	@NonNull
	private final Path outputFilePath;

	private final Boolean ignoreLength;

	@Getter
	private final List<String> commandAndArguments;
	
	public NeroAacEnc(Path inputFilePath, Path outputFilePath, Boolean ignoreLength) {
		this.inputFilePath = inputFilePath;
		this.outputFilePath = outputFilePath;
		this.ignoreLength = ignoreLength;

		ImmutableList.Builder<String> commandAndArgumentsBuilder = ImmutableList.<String>builder() //
				.add(getName()) //
				.add("-if").add(inputFilePath.toAbsolutePath().toString()) //
				.add("-of").add(outputFilePath.toAbsolutePath().toString());

		if (Boolean.TRUE.equals(this.ignoreLength)) {
			commandAndArgumentsBuilder.add("-ignorelength");
		}

		this.commandAndArguments = commandAndArgumentsBuilder.build();
	}

	@Override
	public String getName() {
		return COMMAND;
	}

}
