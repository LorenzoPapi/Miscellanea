//#include <stdio.h>
//
//#define MAXLINE 1000 /* maximum input line size */
//char line[MAXLINE];	/* current input line */
//char detabbed[MAXLINE];	/* longest line saved here */
//
//int getline(void);
//void detab(void);
//
///* print longest input line */
//main() {
//	int len;	/* current line length */
//	extern char detabbed[];
//
//	while ((len = getline()) > 0) {
//		detab();
//		printf("%s", detabbed);
//	}
//	return 0;
//}
//
///* getline: read a line into s, return lenght */
//int getline(void) {
//	int c, i;
//	extern char line[];
//
//	for (i = 0; i < MAXLINE - 1 && (c = getchar()) != EOF && c != '\n'; i++) {
//		line[i] = c;
//	}
//
//	if (c == '\n') {
//		line[i] = c;
//		++i;
//	}
//	line[i] = '\0';
//	return i;
//}
//
//void detab(void) {
//	int linesize, detabbedsize, i;
//	extern char line[], detabbed[];
//
//	linesize = detabbedsize = i = 0;
//
//	while (line[linesize] != '\n') {
//		if (line[linesize] == '\t') {
//			for (i = 0; i < 4; i++) {
//				detabbed[detabbedsize] = ' ';
//				++detabbedsize;
//			}
//		} else {
//			detabbed[detabbedsize] = line[linesize];
//			++detabbedsize;
//		}
//		++linesize;
//	}
//	detabbed[detabbedsize] = '\n';
//	detabbed[++detabbedsize] = '\0';
//	printf("%d, %d\n", linesize, detabbedsize);
//}