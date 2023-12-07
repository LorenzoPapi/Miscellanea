#include <stdio.h>

CountTabsBlanksNewlines() {
	int c, nb, nt, nn;
	nb = 0;
	nt = 0;
	nn = 0;

	while ((c = getchar()) != EOF) {
		if (c == '\n') {
			++nn;
		}
		if (c == '\t') {
			++nt;
		}
		if (c == ' ') {
			++nb;
		}
	}
	printf("Blanks: %d\n", nb);
	printf("Tabs: %d\n", nt);
	printf("Newlines: %d\n", nn);
}