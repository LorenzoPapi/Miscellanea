#include <stdio.h>

#define IN 1
#define OUT 0
#define SIZE 100

Histogram() {
	int c, state, i, j, cl;
	int lenghts[SIZE];

	for (i = 0; i < SIZE; i++) {
		lenghts[i] = 0;
	}

	state = OUT;
	cl = 0;
	while ((c = getchar()) != EOF) {
		if (state == OUT) {
			state = IN;
			while (state == IN) {
				if (c == ' ' || c == '\n' || c == '\t')
					state = OUT;
				else {
					++cl;
					c = getchar();
				}
			}
			if (cl < SIZE)
				++lenghts[cl];
			cl = 0;
		}
	}

	for (i = 0; i < SIZE; i++) {
		if (lenghts[i] > 0) {
			printf("%2d:", i);
			for (j = 0; j < lenghts[i]; j++) {
				printf("=");
			}
			printf("\n");
		}
	}
}