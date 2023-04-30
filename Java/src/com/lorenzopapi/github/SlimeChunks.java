package com.lorenzopapi.github;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SlimeChunks {

	long seed;
	static List<Pair<Integer, Integer>> chunks = new ArrayList<>();

	public SlimeChunks(long seed) {
		this.seed = seed;
	}

	public static void main(String[] args) {
		System.out.println("Start!");
		long start = System.nanoTime();
		new SlimeChunks(0).findSlimeChunksNearCoordinates(0, 0, 10000, 10000);
		long end = System.nanoTime();
		System.out.println("Took: " + (end - start) / 1000 + " ms.");
		System.out.println(chunks);
	}

	public void findSlimeChunksNearCoordinates(int startX, int startZ, int endX, int endZ) {
		chunks.clear();
		for (int x = startX; x <= endX; x++) {
			for (int z = startZ; z <= endZ; z++) {
				Random r = new Random(seed + (x * x * 4987142) + (x * 5947611) + (z * z) * 4392871L + (z * 389711) ^ 987234911L);
				if (r.nextInt(10) == 0) {
					chunks.add(new Pair<>(x, z));
				}
			}
		}
	}

	//TODO: multi-threading.
	public void findAllSlimeChunks() {
		chunks.clear();
		Thread thread = new Thread(() -> {
			for (int x = 0; x <= 1875000; x++) {
				for (int z = 0; z <= 1875000; z++) {
					Random r = new Random(seed + (x * x * 4987142) + (x * 5947611) + (z * z) * 4392871L + (z * 389711) ^ 987234911L);
					if (r.nextInt(10) == 0) {
						chunks.add(new Pair<>(x, z));
					}
				}
			}

		}, "Thread");
		Thread thread2 = new Thread(() -> {
			for (int x = -1875000; x < 0; x++) {
				for (int y = -1875000; y < 0; y++) {
					Random r = new Random(seed + (x * x * 4987142) + (x * 5947611) + (y * y) * 4392871L + (y * 389711) ^ 987234911L);
					if (r.nextInt(10) == 0) {
						chunks.add(new Pair<>(x, y));
					}
				}
			}
		});
		thread.start();
		thread2.start();
		while (thread.isAlive() && thread2.isAlive());
	}
}
