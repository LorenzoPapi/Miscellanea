#include <stdio.h>

/* copy to output */
copytooutput() {
	int c;

	while ((c = getchar()) != EOF) {
		putchar(c);
	}
}

/* count characters in input */
countchars() {
	double nc;

	for (nc = 0; getchar() != EOF; ++nc)
		; // null statement
	printf("%.0f\n", nc);
}

/* count lines in input */
countlines() {
	int c, nl;

	nl = 0;
	while ((c = getchar()) != EOF) {
		if (c == '\n') {
			++nl;
		}
	}
	printf("%d", nl);
}

/* count lines, words and characters in input */

#define IN 1
#define OUT 0

countlineswordschars() {
	int c, nl, nw, nc, state;

	state = OUT;
	nl = nw = nc = 0;
	while ((c = getchar()) != EOF) {
		++nc;
		if (c == '\n')
			++nl;
		if (c == ' ' || c == '\n' || c == '\t')
			state = OUT;
		else if (state == OUT) {
			state = IN;
			++nw;
		}
	}
	printf("%d %d %d\n", nl, nw, nc);
}
