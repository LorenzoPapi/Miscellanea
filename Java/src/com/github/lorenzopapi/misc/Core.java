package com.github.lorenzopapi.misc;

public class Core {
	static Thread t = new Thread(() -> {
		while (true);
	});
	public static void main(String[] args) {
		Thread t1 = new Thread(() -> {
			while (true);
		});
		Thread t2 = new Thread(() -> {
			while (true);
		});
		Thread t3 = new Thread(() -> {
			while (true);
		});
		Thread t4 = new Thread(() -> {
			while (true);
		});
		Thread t5 = new Thread(() -> {
			while (true);
		});
		Thread t6 = new Thread(() -> {
			while (true);
		});
		t1.start();
		t2.start();
		t3.start();
		t4.start();
		t5.start();
		t6.start();
		t.start();
		while (true) {


		}
	}
}
