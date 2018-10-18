package br.com.crazycrowd.mp3m4aconverter.shell;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeoutException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.ImmutableList;

@RunWith(MockitoJUnitRunner.class)
public class CommandExecutorTest {

	private static final Long WAIT_TIMEOUT_SECONDS = 1l;

	private static final List<String> INSTANT_COMMAND = ImmutableList.of("cmd.exe", "/c", "dir");
	private static final List<String> LONG_COMMAND = ImmutableList.of("ping", "localhost");
	private static final List<String> EXIT_COMMAND = ImmutableList.of("cmd.exe", "/c", "exit", "1");

	@Mock
	private ShellCommand shellCommandMock;

	@Mock
	private ShellCommandBuilder shellCommandBuilderMock;

	private CommandExecutor commandExecutor;

	@Before
	public void setUp() {
		commandExecutor = new CommandExecutor(WAIT_TIMEOUT_SECONDS, shellCommandBuilderMock);
	}

	@Test
	public void test_whenMultipleCommands_AllAreCalledInOrder() throws Exception {
		int numberCommands = 10;
		ShellCommand[] commands = new ShellCommand[numberCommands];
		for (int idx = 0; idx < numberCommands; idx++) {
			commands[idx] = mock(ShellCommand.class);
			when(shellCommandBuilderMock.build(commands[idx]))
					.thenReturn(new ProcessBuilder().command(INSTANT_COMMAND));
		}

		commandExecutor.execute(commands);

		InOrder orderVerifier = Mockito.inOrder(shellCommandBuilderMock);

		Arrays.stream(commands).forEach(c -> {
			orderVerifier.verify(shellCommandBuilderMock, times(1)).build(c);
		});
	}

	@Test(expected = TimeoutException.class)
	public void test_whenLongCommandExecuted_TimeoutExceptionIsThrown() throws Exception {
		ShellCommand command = mock(ShellCommand.class);
		when(shellCommandBuilderMock.build(command)).thenReturn(new ProcessBuilder().command(LONG_COMMAND));

		commandExecutor.execute(command);
	}

	@Test(expected = IllegalArgumentException.class)
	public void test_whenCommandExitsWithNonZeroCode_IllegalArgumentExceptionIsThrown() throws Exception {
		ShellCommand command = mock(ShellCommand.class);
		when(shellCommandBuilderMock.build(command)).thenReturn(new ProcessBuilder().command(EXIT_COMMAND));

		commandExecutor.execute(command);
	}

}
