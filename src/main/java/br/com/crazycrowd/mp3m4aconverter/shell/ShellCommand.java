package br.com.crazycrowd.mp3m4aconverter.shell;

import java.util.List;

public interface ShellCommand {

	String getName();

	List<String> getCommandAndArguments();

}
