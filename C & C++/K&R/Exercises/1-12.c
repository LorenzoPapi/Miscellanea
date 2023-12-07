#include <stdio.h>

#define IN 1
#define OUT 0

DivideWords() {
	int c, state;

	state = OUT;
	while ((c = getchar()) != EOF) {
		if (state == OUT) {
			state = IN;
			while (state == IN) {
				if (c == ' ' || c == '\n' || c == '\t')
					state = OUT;
				else {
					putchar(c);
					c = getchar();
				}
			}
			putchar('\n');
		}
	}
}