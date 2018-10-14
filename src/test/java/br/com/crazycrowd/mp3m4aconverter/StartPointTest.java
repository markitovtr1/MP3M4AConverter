package br.com.crazycrowd.mp3m4aconverter;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;

public class StartPointTest {

	private static final Path TEST_FILE = Paths.get("pom.xml");
	private static final Path TEST_DIRECTORY = Paths.get("src");

	@Test(expected = IllegalArgumentException.class)
	public void test_whenNoArgsIsPassed_IllegalArgumentExceptionIsThrown() throws Exception {
		StartPoint.main(new String[0]);
	}

	@Test(expected = IllegalArgumentException.class)
	public void test_whenMultipleArgsArePassed_IllegalArgumentExceptionIsThrown() throws Exception {
		StartPoint.main(new String[2]);
	}

	@Test(expected = IllegalArgumentException.class)
	public void test_whenSingleArgIsPassedAndIsNotADirectory_IllegalArgumentExceptionIsThrown() throws Exception {
		String[] args = { TEST_FILE.toString() };
		StartPoint.main(args);
	}

	@Test
	public void test_whenSingleArgIsPassedAndIsADirectory_NoExceptionIsThrown() throws Exception {
		String[] args = { TEST_DIRECTORY.toString() };
		StartPoint.main(args);
	}

}
