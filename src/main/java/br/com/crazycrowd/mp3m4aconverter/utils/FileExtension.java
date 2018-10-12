/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.crazycrowd.mp3m4aconverter.utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * File extensions used by application.
 * 
 * @author marcos.romero
 */
@RequiredArgsConstructor
@Getter
public enum FileExtension {
	WAVE("wav"), M4A("m4a"), MP3("mp3");

	private final String extension;
}
