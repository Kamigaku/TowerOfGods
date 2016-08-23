package com.kamigaku.towerofgods.log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.kamigaku.towerofgods.GameLauncher;

public class Log {
	
	public static void writeLog(String nomFichier, String message) {
		Date date = new Date();
		DateFormat dateFormat = new SimpleDateFormat("dd_MM_yyyy");
		String completeRoad = GameLauncher.PATH_FILE + "log/" + nomFichier + "_" + dateFormat.format(date) + ".txt";
		try {
			FileWriter file = new FileWriter(new File(completeRoad), true);
			file.write("" + date.toString() + " : " + message + "\r\n");
			file.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
