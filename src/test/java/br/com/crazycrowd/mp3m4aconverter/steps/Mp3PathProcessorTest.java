package br.com.crazycrowd.mp3m4aconverter.steps;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import br.com.crazycrowd.mp3m4aconverter.shell.CommandExecutor;
import br.com.crazycrowd.mp3m4aconverter.shell.Ffmpeg;
import br.com.crazycrowd.mp3m4aconverter.shell.NeroAacEnc;
import br.com.crazycrowd.mp3m4aconverter.shell.ShellCommand;
import br.com.crazycrowd.mp3m4aconverter.utils.FileExtension;
import br.com.crazycrowd.mp3m4aconverter.utils.FileHelper;
import br.com.crazycrowd.mp3m4aconverter.utils.TagWriter;

/**
 *
 * @author marcos.romero
 */
@RunWith(MockitoJUnitRunner.class)
public class Mp3PathProcessorTest {

	private static final Path PARENT_PATH = Paths.get("C:");
	private static final Path SAMPLE_MP3_PATH = PARENT_PATH.resolve("file.mp3");
	private static final Path SAMPLE_WAV_PATH = PARENT_PATH.resolve("file.wav");
	private static final Path SAMPLE_M4A_PATH = PARENT_PATH.resolve("file.m4a");
	private static final Path SAMPLE_ARTWORK_PATH = PARENT_PATH.resolve("folder.jpg");

	@Mock
	private CommandExecutor commandExecutorMock;

	@Mock
	private TagWriter tagWriterMock;

	@Mock
	private FileHelper fileHelperMock;

	private Mp3PathProcessor processor;

	@Before
	public void setUp() throws Exception {
		when(fileHelperMock.deleteIfExists(any(Path.class))).thenReturn(true);
		doNothing().when(commandExecutorMock).execute(buildExpectedFfmpegObject(), buildExpectedNeroAacObject());
		doNothing().when(tagWriterMock).addArtwork(SAMPLE_MP3_PATH, SAMPLE_ARTWORK_PATH);
		doNothing().when(tagWriterMock).copyTags(SAMPLE_MP3_PATH, SAMPLE_M4A_PATH);
		processor = new Mp3PathProcessor(fileHelperMock, commandExecutorMock, tagWriterMock);
	}

	@Test
	public void test_whenM4AFileExists_nothingIsDone() throws Exception {
		when(fileHelperMock.exists(SAMPLE_M4A_PATH)).thenReturn(Boolean.TRUE);

		processor.process(SAMPLE_MP3_PATH);

		verify(commandExecutorMock, never()).execute(any(ShellCommand.class), any(ShellCommand.class));
		verify(tagWriterMock, never()).copyTags(any(Path.class), any(Path.class));
		verify(tagWriterMock, never()).addArtwork(any(Path.class), any(Path.class));
	}

	@Test
	public void test_whenM4AFileDoesntExistAndArtworkExists_convertAndCopyTagsAndAddArtwork() throws Exception {
		when(fileHelperMock.exists(SAMPLE_M4A_PATH)).thenReturn(Boolean.FALSE);
		when(fileHelperMock.exists(SAMPLE_ARTWORK_PATH)).thenReturn(Boolean.TRUE);

		processor.process(SAMPLE_MP3_PATH);

		verify(commandExecutorMock, times(1)).execute(buildExpectedFfmpegObject(), buildExpectedNeroAacObject());
		verify(tagWriterMock, times(1)).copyTags(SAMPLE_MP3_PATH, SAMPLE_M4A_PATH);
		verify(tagWriterMock, times(1)).addArtwork(SAMPLE_M4A_PATH, SAMPLE_ARTWORK_PATH);

	}

	@Test
	public void test_whenM4AFileDoesntExistAndArtworkDoesntExist_convertAndCopyTagsOnly() throws Exception {
		when(fileHelperMock.exists(SAMPLE_M4A_PATH)).thenReturn(Boolean.FALSE);
		when(fileHelperMock.exists(SAMPLE_ARTWORK_PATH)).thenReturn(Boolean.FALSE);

		processor.process(SAMPLE_MP3_PATH);

		verify(commandExecutorMock, times(1)).execute(buildExpectedFfmpegObject(), buildExpectedNeroAacObject());
		verify(tagWriterMock, times(1)).copyTags(SAMPLE_MP3_PATH, SAMPLE_M4A_PATH);
		verify(tagWriterMock, never()).addArtwork(SAMPLE_M4A_PATH, SAMPLE_ARTWORK_PATH);
	}

	private Ffmpeg buildExpectedFfmpegObject() {
		return new Ffmpeg(SAMPLE_MP3_PATH, SAMPLE_WAV_PATH, FileExtension.WAVE.getExtension());
	}

	private NeroAacEnc buildExpectedNeroAacObject() {
		return new NeroAacEnc(SAMPLE_WAV_PATH, SAMPLE_M4A_PATH, true);
	}
}
