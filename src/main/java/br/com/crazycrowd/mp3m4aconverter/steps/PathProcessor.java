/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.crazycrowd.mp3m4aconverter.steps;

import java.io.IOException;
import java.nio.file.Path;

/**
 *
 * @author marcos.romero
 */
public interface PathProcessor {

	void process(Path path) throws IOException, InterruptedException;

}
