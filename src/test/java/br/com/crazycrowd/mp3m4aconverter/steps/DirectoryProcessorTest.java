package br.com.crazycrowd.mp3m4aconverter.steps;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import br.com.crazycrowd.mp3m4aconverter.utils.FileExtension;
import br.com.crazycrowd.mp3m4aconverter.utils.FileHelper;
import br.com.crazycrowd.mp3m4aconverter.utils.PathUtils;

@RunWith(MockitoJUnitRunner.class)
public class DirectoryProcessorTest {

	private static final Path TEST_AUDIO_DIR = Paths.get("src", "test", "resources", "audio");

	@Mock
	private FileHelper fileHelperMock;

	@Mock
	private Mp3PathProcessor mp3PathProcessorMock;

	@Mock
	private DirectoryListStrategy directoryListStrategyMock;

	private DirectoryProcessor processor;

	@Before
	public void setUp() {
		processor = new DirectoryProcessor(fileHelperMock, mp3PathProcessorMock, directoryListStrategyMock);
	}

	@Test(expected = IllegalArgumentException.class)
	public void test_whenPathIsNotADirectory_throwsIllegalArgumentException() throws Exception {
		Path file = TEST_AUDIO_DIR.resolve("dir1").resolve("01-01 Test01.mp3");

		processor.process(file);
	}

	@Test
	public void test_whenPathIsAValidDirectory_ProcessFilesAccordingly() throws Exception {
		List<Path> filePathMocks = buildListPaths("testOk.mp3", "test.txt", "testTimeout.mp3", "testIo.mp3",
				"testInterrupted.mp3");
		when(directoryListStrategyMock.list(TEST_AUDIO_DIR)).thenReturn(filePathMocks.stream());

		doNothing().when(mp3PathProcessorMock).process(filePathMocks.get(0));
		mockExceptionForMp3PathProcessing(filePathMocks.get(2), new TimeoutException());
		mockExceptionForMp3PathProcessing(filePathMocks.get(3), new IOException());
		doThrow(new InterruptedException()).when(mp3PathProcessorMock).process(filePathMocks.get(4));
		doThrow(new IOException()).when(fileHelperMock)
				.deleteIfExists(PathUtils.changeFileExtension(filePathMocks.get(4), FileExtension.M4A));

		processor.process(TEST_AUDIO_DIR);

		verify(mp3PathProcessorMock, times(4)).process(any(Path.class));
		verify(mp3PathProcessorMock, never()).process(filePathMocks.get(1));
		verify(fileHelperMock, times(3)).deleteIfExists(any(Path.class));
	}

	private void mockExceptionForMp3PathProcessing(Path mp3Path, Exception exception) throws Exception {
		doThrow(exception).when(mp3PathProcessorMock).process(mp3Path);
		when(fileHelperMock.deleteIfExists(PathUtils.changeFileExtension(mp3Path, FileExtension.M4A))).thenReturn(true);
	}

	private List<Path> buildListPaths(String... paths) {
		return Arrays.stream(paths).map(Paths::get).collect(Collectors.toList());
	}

}
