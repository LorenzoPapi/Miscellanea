package com.lorenzopapi.github.amongsus;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ChangeGameHostOptions {

	static File gho = new File(System.getenv("APPDATA") + "\\..\\LocalLow\\Innersloth\\Among Us\\gameHostOptions");

	static HashMap<Integer, Integer> idToOffset = new HashMap<>();
	static HashMap<Integer, ChangeGameHostOptions.Type> idToType = new HashMap<>();

	public static void main(String[] args) throws IOException {
		modifyGameHostOptions();
	}

	private static void modifyGameHostOptions() throws IOException {
		System.out.println("What would you like to do?\n1)Read\n2)Modify");
		Scanner input = new Scanner(System.in);
		String readOrNot = input.next();
		while (isNotNumeric(readOrNot)) {
			System.err.println("I've said 1 or 2, not " + readOrNot + "!!");
			readOrNot = input.next();
		}
		if (Integer.parseInt(readOrNot) == 1) {
			printOptions();
			System.exit(0);
		} else {
			fillMap();
			modifyValue(input);
		}
	}

	private static void modifyValue(Scanner input) throws IOException {
		System.out.println("Choose what to modify:");
		System.out.println("0)Unknown thing, will be set to 3 at start");
		System.out.println("1)Max players, can be from 0 to 255");
		System.out.printf("2)Chat Type, (other, english), can be from 0 to %d%n", 0xFFFFFFFFL);
		System.out.println("3)Map Type, (0=TheSkeld, 1=MiraHQ, 2=Polus), can be from 0 to 255");
		System.out.printf("4)Player speed, can be from 0. to %f. Putting 0 doesn't work, but 0.0000000000001 does%n", Float.MAX_VALUE);
		System.out.printf("5)Crewmate vision, can be from 0 to %f. Putting 0 doesn't work, but 0.0000000000001 does%n", Float.MAX_VALUE);
		System.out.printf("6)Impostor vision, can be from 0 to %f. Putting 0 doesn't work, but 0.0000000000001 does%n", Float.MAX_VALUE);
		System.out.printf("7)Kill cooldown, can be from 0 to %f. Putting 0 doesn't work, but 0.0000000000001 does%n", Float.MAX_VALUE);
		System.out.println("8)Common tasks, can be from 0 to 255");
		System.out.println("9)Long tasks, can be from 0 to 255");
		System.out.println("10)Short tasks, can be from 0 to 255");
		System.out.printf("11)Emergency Meetings, can be from 0 to %d%n", 0xFFFFFFFFL);
		System.out.println("12)Impostors, can be from 0 to 255");
		System.out.println("13)Kill Distance, (0=Short, 1=Medium, 2=Long), can be from 0 to 255");
		System.out.printf("14)Discussion time, can be from 0 to %d%n", 0xFFFFFFFFL);
		System.out.printf("15)Voting time, can be from 0 to %d%n", 0xFFFFFFFFL);
		System.out.println("16)Are recommended settings turned on? (0=false, non 0=true), can be from 0 to 255");
		System.out.println("17)Emergency meetings cooldown, can be from 0 to 255");
		System.out.println("18)Are ejects confirmed? (0=false, non 0=true), can be from 0 to 255");
		System.out.println("19)Are visual tasks on? (0=false, non 0=true), can be from 0 to 255");
		String modifyString = input.next();
		int modify = getModifyID(input, modifyString);
		for (Map.Entry<Integer, ChangeGameHostOptions.Type> entry : idToType.entrySet()) {
			if (modify == entry.getKey()) {
				System.out.println("You choose option " + entry.getKey() + ", with the type of " + entry.getValue().toString());
				System.out.println("Enter the value you want to put");
				String valueString = input.next();
				long value = parseValue(input, entry, valueString);
				ByteArrayOutputStream bout = new ByteArrayOutputStream();

				ByteBuffer valueBuffer = ByteBuffer.allocate(4);
				valueBuffer.order(ByteOrder.LITTLE_ENDIAN);
				byte[] valueInBytes;
				if (entry.getValue() == Type.INT)
					valueInBytes = valueBuffer.putInt((int) value).array();
				else if (entry.getValue() == Type.FLOAT)
					valueInBytes = valueBuffer.putInt((int) value).array();
				else
					valueInBytes = new byte[]{(byte) value};

				bout.write(valueInBytes);
				byte[] original = Files.readAllBytes(gho.toPath());
				Path backup = Paths.get(gho.toPath() + ".bak");
				Files.copy(gho.toPath(), backup, StandardCopyOption.REPLACE_EXISTING);
				System.out.println("Wrote backup at " + gho.getAbsolutePath() + ".bak");
				ByteBuffer mergeBuffer = ByteBuffer.wrap(original);
				mergeBuffer.position(idToOffset.get(entry.getKey()));
				mergeBuffer.put(valueInBytes);
				gho.delete();
				gho.createNewFile();
				FileOutputStream fos = new FileOutputStream(gho);
				fos.write(mergeBuffer.array());
				System.out.println("Modified options!\nNew options are:");
				printOptions();
				System.out.println("Would you like to modify something else?\nYes\nNo");
				String yesOrNo = input.next();
				if (yesOrNo.equalsIgnoreCase("yes")) {
					System.out.println("Ok!");
					modifyValue(input);
				} else if (yesOrNo.equalsIgnoreCase("no")) {
					System.out.println("Perfect, have a good day!");
					System.exit(0);
				} else {
					System.out.println("Interpreting vague answer as no.");
					System.out.println("Perfect, have a good day!");
					System.exit(0);
				}
			} else if (!idToType.containsKey(modify)) {
				System.err.println("Option not existing!! Must be between 0 (included) and 19 (included)");
			}
		}
	}

	private static long parseValue(Scanner input, Map.Entry<Integer, Type> entry, String value) {
		while (isNotNumeric(value)) {
			System.err.println("I've said a number, not " + value + "!!");
			value = input.next();
		}
		switch (entry.getValue()) {
			case BYTE: {
				int put = Integer.parseInt(value);
				if (put > 255 || put < 0) {
					System.err.println("This option has a maximum of 255 and a minimum of 0, it can't be " + put + "!!");
					parseValue(input, entry, input.next());
				} else {
					return put;
				}
				break;
			}
			case INT: {
				long put = Long.parseLong(value);
				if (put < 0 || put > 0xFFFFFFFFL) {
					System.err.println("This option has a maximum of " + 0xFFFFFFFFL + " and a minimum of " + 0 + ", it can't be " + put + "!!");
					parseValue(input, entry, input.next());
				} else {
					return put;
				}
				break;
			}
			case FLOAT: {
				float put = Float.parseFloat(value);
				return Float.floatToIntBits(put);
			}
			default:
				throw new RuntimeException("Le thonkkkkk");
		}
		return 0;
	}

	private static int getModifyID(Scanner input, String modifyString) {
		while (isNotNumeric(modifyString)) {
			System.err.println("I've said number from 0 to 19, not " + modifyString + "!!");
			modifyString = input.next();
		}
		int modify = Integer.parseInt(modifyString);
		if (modify < 0 || modify > 19) {
			System.err.println("I've said number from 0 to 19, not " + modifyString + "!!");
			getModifyID(input, input.next());
		}
		return modify;
	}

	private static void fillMap() {
		idToType.put(0, Type.BYTE);
		idToType.put(1, Type.BYTE);
		idToType.put(2, Type.INT);
		idToType.put(3, Type.BYTE);
		for (int i = 4; i <= 7; i++)
			idToType.put(i, Type.FLOAT);
		idToType.put(8, Type.BYTE);
		idToType.put(9, Type.BYTE);
		idToType.put(10, Type.BYTE);
		idToType.put(11, Type.INT);
		idToType.put(12, Type.BYTE);
		idToType.put(13, Type.BYTE);
		idToType.put(14, Type.INT);
		idToType.put(15, Type.INT);
		idToType.put(16, Type.BYTE);
		idToType.put(17, Type.BYTE);
		idToType.put(18, Type.BYTE);
		idToType.put(19, Type.BYTE);
		idToOffset.put(0, 0);
		idToOffset.put(1, 1);
		idToOffset.put(2, 2);
		idToOffset.put(3, 6);
		idToOffset.put(4, 7);
		idToOffset.put(5, 11);
		idToOffset.put(6, 15);
		idToOffset.put(7, 19);
		idToOffset.put(8, 23);
		idToOffset.put(9, 24);
		idToOffset.put(10, 25);
		idToOffset.put(11, 26);
		idToOffset.put(12, 33);
		idToOffset.put(13, 34);
		idToOffset.put(14, 35);
		idToOffset.put(15, 36);
		idToOffset.put(16, 40);
		idToOffset.put(17, 41);
		idToOffset.put(18, 42);
		idToOffset.put(19, 43);
	}

	private static void printOptions() throws IOException {
		GHO options = new GHO();
		System.out.println("Current Game Host Options:");
		System.out.printf("Unknown: %d%n", options.unknown);
		System.out.printf("Max players: %d%n", options.maxP);
		System.out.printf("Chat Type: %d%n", options.chat);
		System.out.printf("Map Type: %d%n", options.map);
		System.out.printf("Player Speed: %f%n", options.speed);
		System.out.printf("Crewmate Vision: %f%n", options.cVision);
		System.out.printf("Impostor Vision: %f%n", options.iVision);
		System.out.printf("Kill Cooldown: %f%n", options.cooldown);
		System.out.printf("Common Tasks: %d%n", options.commonT);
		System.out.printf("Long Tasks: %d%n", options.longT);
		System.out.printf("Short Tasks: %d%n", options.shortT);
		System.out.printf("Emergency Meetings: %d%n", options.meetings);
		System.out.printf("Impostors: %d%n", options.impostors);
		System.out.printf("Kill Distance: %d%n", options.distance);
		System.out.printf("Discussion Time: %d%n", options.discussionT);
		System.out.printf("Voting Time: %d%n", options.votingT);
		System.out.printf("Recommenced Settings: %d%n", options.settings);
		System.out.printf("Emergency Cooldown: %d%n", options.emergencyCooldown);
		System.out.printf("Confirm Ejects: %d%n", options.confirmE);
		System.out.printf("Visual Tasks: %d%n", options.visualT);
	}

	private static boolean isNotNumeric(String strNum) {
		if (strNum == null)
			return true;
		try {
			double i = Double.parseDouble(strNum);
		} catch (NumberFormatException nfe) {
			return true;
		}
		return false;
	}

	private static class GHO {
		int unknown;
		int maxP;
		long chat;
		int map;
		float speed;
		float cVision;
		float iVision;
		float cooldown;
		int commonT;
		int longT;
		int shortT;
		long meetings;
		int impostors;
		int distance;
		long discussionT;
		long votingT;
		int settings;
		int emergencyCooldown;
		int confirmE;
		int visualT;

		public GHO() throws IOException {
			RandomAccessFile in = new RandomAccessFile(ChangeGameHostOptions.gho, "r");
			unknown = in.readUnsignedByte();
			maxP = in.readUnsignedByte();
			chat = readIntLE(in);
			map = in.readUnsignedByte();
			speed = Float.intBitsToFloat(readIntLE(in));
			cVision = Float.intBitsToFloat(readIntLE(in));
			iVision = Float.intBitsToFloat(readIntLE(in));
			cooldown = Float.intBitsToFloat(readIntLE(in));
			commonT = in.readUnsignedByte();
			longT = in.readUnsignedByte();
			shortT = in.readUnsignedByte();
			meetings = readIntLE(in);
			impostors = in.readUnsignedByte();
			distance = in.readUnsignedByte();
			discussionT = readIntLE(in);
			votingT = readIntLE(in);
			settings = in.readUnsignedByte();
			emergencyCooldown = in.readUnsignedByte();
			confirmE = in.readUnsignedByte();
			visualT = in.readUnsignedByte();
			in.close();
		}

		public final int readIntLE(RandomAccessFile in) throws IOException {
			int ch1 = in.read();
			int ch2 = in.read();
			int ch3 = in.read();
			int ch4 = in.read();
			return ((ch4 << 24) + (ch3 << 16) + (ch2 << 8) + ch1);
		}
	}

	private enum Type {
		BYTE("byte"),
		FLOAT("float"),
		INT("int");

		String name;

		Type(String name) {
			this.name = name;
		}

		@Override
		public String toString() {
			return name;
		}
	}
}
