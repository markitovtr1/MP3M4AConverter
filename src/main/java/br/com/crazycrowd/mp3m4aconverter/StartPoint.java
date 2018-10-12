/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.crazycrowd.mp3m4aconverter;

import br.com.crazycrowd.mp3m4aconverter.config.EnvironmentModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import br.com.crazycrowd.mp3m4aconverter.steps.PathProcessor;
import java.io.File;

/**
 *
 * @author marcos.romero
 */
public class StartPoint {

	public static void main(final String[] args) throws Exception {
		Injector injector = Guice.createInjector(new EnvironmentModule());
		PathProcessor processor = injector.getInstance(PathProcessor.class);
		processor.process(new File(args[0]).toPath());
	}

}
