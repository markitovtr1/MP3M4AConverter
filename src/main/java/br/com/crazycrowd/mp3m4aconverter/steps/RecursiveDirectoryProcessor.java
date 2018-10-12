/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.crazycrowd.mp3m4aconverter.steps;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 *
 * @author marcos.romero
 */
@Singleton
public class RecursiveDirectoryProcessor extends AbstractDirectoryProcessor {

	@Inject
	public RecursiveDirectoryProcessor(Mp3PathProcessor mp3PathProcessor) {
		super(mp3PathProcessor);
	}

	@Override
	protected Stream<Path> getDirectoryStream(Path directory) throws IOException {
		return Files.walk(directory);
	}

}
