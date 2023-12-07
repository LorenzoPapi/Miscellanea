#include <stdio.h>

RemoveBlanks() {
	int c;

	while ((c = getchar()) != EOF) {
		if (c != ' ') {
			putchar(c);
		}
		if (c == ' ') {
			int cn;
			while ((cn = getchar()) == ' ');
			putchar(' ');
			putchar(cn);
		}
	}
}