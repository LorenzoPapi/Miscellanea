#include <stdio.h>

WrongWhileLoop() {
	int c;

	while (c = (getchar() != EOF)) {
		putchar(c);
	}
}