#include <stdio.h>

#define MAXLINE 1000 /* maximum input line size */
int max;	/* maximum length seen so far */
char line[MAXLINE];	/* current input line */
char entabbed[MAXLINE];	/* longest line saved here */

int getline(void);
void decomment(void);

/* print longest input line */
main() {
	int len;	/* current line length */
	extern int max;
	extern char entabbed[];
	
	max = 0;
	while ((len = getline()) > 0) {
		if (len > max) {
			max = len;
			decomment();
		}
	}
	if (max > 0) /* line present */
		printf("%s", entabbed);
	return 0;
}

/* getline: read a line into s, return lenght */
int getline(void) {
	int c, i;
	extern char line[];

	for (i = 0; i < MAXLINE - 1 && (c = getchar()) != EOF && c != '\n'; i++) {
		line[i] = c;
	}

	if (c == '\n') {
		line[i] = c;
		++i;
	}
	line[i] = '\0';
	return i;
}

void decomment(void) {
	int i;
	extern char line[], entabbed[];

	i = 0;
	while ((entabbed[i] = line[i]) != '\0')
		i++;
}