package com.lorenzopapi.github.amongsus;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Scanner;

public class ChangePlayerPrefs {

	static File prefs = new File(System.getenv("APPDATA") + "\\..\\LocalLow\\Innersloth\\Among Us\\playerPrefs");

	static HashMap<String, Integer> nameToID = new HashMap<>();

	public static void main(String[] args) throws IOException, InterruptedException {
		fillMap();
		byte[] prefsBytes = Files.readAllBytes(prefs.toPath());
		String prefString = new String(prefsBytes);
		int petID = 0;
		int count = 0;
		for (String split : prefString.split(",")) {
			if (count == 16) {
				System.out.println(split + " " + count);
				petID = Integer.parseInt(split);
			}
			count++;
		}
		System.out.println("Tempo di cambiare il tuo personaggio :)");
		System.out.println("Iniziamo col tuo piccolo pet");
		if (petID == 0)
			System.out.println("Sembra che tu non abbia proprio un pet!\nChe tragedia!!\nLascia che ti aiuti...");
		else
			System.out.println("Sembra che tu abbia già un pet, " + ""/*nameToID.get(petID)*/ + " !\nVuoi cambiarlo eh...\nLascia che ti aiuti...");
		System.out.println("Inserisci il nome del pet.\nCi sono:\nVuoto\nAlieno\nBaby,\nCane");
		Scanner sc = new Scanner(System.in);
		String got = sc.next();
		while (!nameToID.containsKey(got.toUpperCase())) {
			System.out.println("Pet non esistente!");
			System.out.println("Riprova!");
		}
		System.out.println("Hai scelto " + got + ".");
		Path backup = Paths.get(prefs.toPath() + ".bak");
		Files.copy(prefs.toPath(), backup, StandardCopyOption.REPLACE_EXISTING);
		System.out.println("Backup creato!");
		System.out.println("Modificando i salvataggi...");
		StringBuilder newPrefs = new StringBuilder();
		count = 0;
		for (String split : prefString.split(",")) {
			if (count == 16) {
				newPrefs.append(nameToID.get(got.toUpperCase())).append(",");
			} else {
				newPrefs.append(split).append(count == 19 ? "" : ",");
			}
			count++;
		}
		prefs.delete();
		prefs.createNewFile();
		FileOutputStream bout = new FileOutputStream(prefs);
		bout.write(newPrefs.toString().getBytes());
		Thread.sleep(500);
		System.out.println("Fatto!");
		System.out.println("Ciao!");
	}

	private static void fillMap() {
		nameToID.put("VUOTO", 0);
		nameToID.put("ALIENO", 1);
		nameToID.put("BABY", 2);
		nameToID.put("CANE", 3);
	}

}
