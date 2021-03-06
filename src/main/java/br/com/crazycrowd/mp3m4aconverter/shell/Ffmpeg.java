package br.com.crazycrowd.mp3m4aconverter.shell;

import java.nio.file.Path;
import java.util.List;

import com.google.common.collect.ImmutableList;

import lombok.Getter;
import lombok.NonNull;

public class Ffmpeg implements ShellCommand {

	private static final String COMMAND = "ffmpeg";

	@NonNull
	private final Path inputFilePath;

	@NonNull
	private final Path outputFilePath;

	@NonNull
	private final String outputFormat;

	@Getter
	private final List<String> commandAndArguments;

	public Ffmpeg(Path inputFilePath, Path outputFilePath, String outputFormat) {
		this.inputFilePath = inputFilePath;
		this.outputFilePath = outputFilePath;
		this.outputFormat = outputFormat;

		this.commandAndArguments = ImmutableList.of( //
				getName(), "-i", inputFilePath.toAbsolutePath().toString(), //
				"-f", outputFormat, //
				outputFilePath.toAbsolutePath().toString() //
		);
	}

	@Override
	public String getName() {
		return COMMAND;
	}

}
