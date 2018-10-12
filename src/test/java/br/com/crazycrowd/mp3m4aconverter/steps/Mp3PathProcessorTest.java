/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.crazycrowd.mp3m4aconverter.steps;

import br.com.crazycrowd.mp3m4aconverter.utils.ShellCommands;
import br.com.crazycrowd.mp3m4aconverter.utils.TagWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Matchers.any;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 *
 * @author marcos.romero
 */
@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.management.*")
@PrepareForTest({Mp3PathProcessor.class})
public class Mp3PathProcessorTest {
	
	private static final Path PARENT_PATH = Paths.get("C:");
	private static final Path SAMPLE_MP3_PATH = PARENT_PATH.resolve("file.mp3");
	private static final Path SAMPLE_WAV_PATH = PARENT_PATH.resolve("file.wav");
	private static final Path SAMPLE_M4A_PATH = PARENT_PATH.resolve("file.m4a");
	private static final Path SAMPLE_ARTWORK_PATH = PARENT_PATH.resolve("folder.jpg");
	
	@Mock
	private ShellCommands shellCommandsMock;
	
	@Mock
	private TagWriter tagWriterMock;
	
	private Mp3PathProcessor processor;
	
	@Before
	public void setUp() throws Exception {
		PowerMockito.mockStatic(Files.class);
		when(Files.deleteIfExists(SAMPLE_WAV_PATH)).thenReturn(Boolean.TRUE);
		when(shellCommandsMock.ffmpeg(SAMPLE_MP3_PATH)).thenReturn(SAMPLE_WAV_PATH);
		when(shellCommandsMock.neroAacEnc(SAMPLE_WAV_PATH)).thenReturn(SAMPLE_M4A_PATH);
		doNothing().when(tagWriterMock).addArtwork(SAMPLE_MP3_PATH, SAMPLE_ARTWORK_PATH);
		doNothing().when(tagWriterMock).copyTags(SAMPLE_MP3_PATH, SAMPLE_M4A_PATH);
		processor = new Mp3PathProcessor(shellCommandsMock, tagWriterMock);
	}

	@Test
	public void test_whenM4AFileExists_nothingIsDone() throws Exception {
		when(Files.exists(SAMPLE_M4A_PATH)).thenReturn(Boolean.TRUE);
		
		processor.process(SAMPLE_MP3_PATH);
		
		verify(shellCommandsMock, never()).ffmpeg(any(Path.class));
		verify(shellCommandsMock, never()).neroAacEnc(any(Path.class));
		verify(tagWriterMock, never()).copyTags(any(Path.class), any(Path.class));
		verify(tagWriterMock, never()).addArtwork(any(Path.class), any(Path.class));
	}
	
	@Test
	public void test_whenM4AFileDoesntExistAndArtworkExists_convertAndCopyTagsAndAddArtwork() throws Exception {
		when(Files.exists(SAMPLE_M4A_PATH)).thenReturn(Boolean.FALSE);
		when(Files.exists(SAMPLE_ARTWORK_PATH)).thenReturn(Boolean.TRUE);
		
		processor.process(SAMPLE_MP3_PATH);
		
		verify(shellCommandsMock, times(1)).ffmpeg(SAMPLE_MP3_PATH);
		verify(shellCommandsMock, times(1)).neroAacEnc(SAMPLE_WAV_PATH);
		verify(tagWriterMock, times(1)).copyTags(SAMPLE_MP3_PATH, SAMPLE_M4A_PATH);
		verify(tagWriterMock, times(1)).addArtwork(SAMPLE_M4A_PATH, SAMPLE_ARTWORK_PATH);
		
	}
	
	@Test
	public void test_whenM4AFileDoesntExistAndArtworkDoesntExist_convertAndCopyTagsOnly() throws Exception {
		when(Files.exists(SAMPLE_M4A_PATH)).thenReturn(Boolean.FALSE);
		when(Files.exists(SAMPLE_ARTWORK_PATH)).thenReturn(Boolean.FALSE);
		
		processor.process(SAMPLE_MP3_PATH);
		
		verify(shellCommandsMock, times(1)).ffmpeg(SAMPLE_MP3_PATH);
		verify(shellCommandsMock, times(1)).neroAacEnc(SAMPLE_WAV_PATH);
		verify(tagWriterMock, times(1)).copyTags(SAMPLE_MP3_PATH, SAMPLE_M4A_PATH);
		verify(tagWriterMock, never()).addArtwork(SAMPLE_M4A_PATH, SAMPLE_ARTWORK_PATH);
	}
}
