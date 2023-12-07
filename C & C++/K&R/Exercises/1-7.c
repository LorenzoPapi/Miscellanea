#include <stdio.h>

PrintEOF() {
	int c;
	while (c = getchar()) {
		printf("%c", EOF);
	}
}