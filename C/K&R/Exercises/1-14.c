#include <stdio.h>

#define SIZE 128

HistogramOfChars() {
	int c, i, j;
	int lenghts[SIZE];

	for (i = 0; i < SIZE; i++) {
		lenghts[i] = 0;
	}

	while ((c = getchar()) != EOF) {
		++lenghts[c];
	}

	for (i = 0; i < SIZE; i++) {
		if (lenghts[i] > 0) {
			putchar(i);
			printf(" (%3d):", i);
			for (j = 0; j < lenghts[i]; j++) {
				printf("=");
			}
			printf("\n");
		}
	}
}