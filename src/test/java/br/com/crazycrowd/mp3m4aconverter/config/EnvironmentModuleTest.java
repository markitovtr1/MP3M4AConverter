package br.com.crazycrowd.mp3m4aconverter.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import javax.inject.Inject;

import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.carlosbecker.guice.GuiceModules;
import com.carlosbecker.guice.GuiceTestRunner;

import br.com.crazycrowd.mp3m4aconverter.steps.PathProcessor;

@RunWith(GuiceTestRunner.class)
@GuiceModules(EnvironmentModule.class)
public class EnvironmentModuleTest {

	@Inject
	private PathProcessor pathProcessor;
	
	@Inject
	private AudioFileIO audioFileIO;
	
	@Inject
	private List<FieldKey> supportedAudioTags;

	private EnvironmentModule environmentModule;

	@Before
	public void setUp() {
		environmentModule = new EnvironmentModule();
	}
	
	@After
	public void tearDown() {
		System.clearProperty(EnvironmentModule.SHELL_COMMANDS_WAIT_SECONDS_PROPERTY);
	}

	@Test
	public void test_sanity_injectedObjects() {
		assertNotNull(pathProcessor);
		assertNotNull(audioFileIO);
		assertNotNull(supportedAudioTags);
	}
	
	@Test
	public void test_sanity_configure() {
		environmentModule.configure();
	}

	@Test
	public void test_whenShellCommandsWaitSecondsPropertyNotSet_DefaultValueIsReturned() {
		System.clearProperty(EnvironmentModule.SHELL_COMMANDS_WAIT_SECONDS_PROPERTY);

		Long waitSeconds = environmentModule.shellCommandsWaitSeconds();

		assertEquals(EnvironmentModule.DEFAULT_WAIT_SECONDS, waitSeconds);
	}

	@Test
	public void test_whenShellCommandsWaitSecondsPropertyIsSetToValidNumber_SetValueIsReturned() {
		Long expectedValue = 100L;
		System.setProperty(EnvironmentModule.SHELL_COMMANDS_WAIT_SECONDS_PROPERTY, expectedValue.toString());

		Long waitSeconds = environmentModule.shellCommandsWaitSeconds();

		assertEquals(expectedValue, waitSeconds);
	}

	@Test
	public void test_whenShellCommandsWaitSecondsPropertyIsSetAsNotALong_DefaultValueIsReturned() {
		System.setProperty(EnvironmentModule.SHELL_COMMANDS_WAIT_SECONDS_PROPERTY, "invalid_number");

		Long waitSeconds = environmentModule.shellCommandsWaitSeconds();

		assertEquals(EnvironmentModule.DEFAULT_WAIT_SECONDS, waitSeconds);
	}

}
