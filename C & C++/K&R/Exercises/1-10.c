#include <stdio.h>

ReplaceEscapes() {
	int c;

	while ((c = getchar()) != EOF) {
		if (c == '\t') {
			putchar('\\');
			putchar('t');
		}
		if (c == '\b') {
			putchar('\\');
			putchar('b');
		}
		if (c == '\\') {
			putchar('\\');
			putchar('\\');
		}
		if (c != '\\')
			if (c != '\b')
				if (c != '\t')
					putchar(c);
	}
}