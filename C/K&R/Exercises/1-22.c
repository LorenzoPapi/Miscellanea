//#include <stdio.h>
//
//#define MAXLINE 1000 /* maximum input line size */
//#define COLUMN 20 /* lines greater than or this will be split */
//char line[MAXLINE];	/* current input line */
//char foldtemp[MAXLINE];	/* entabbed line saved here */
//
//int getline(void);
//void fold(int n);
//
//main() {
//	int len;	/* current line length */
//
//	while ((len = getline()) > 0) {
//		if (len > COLUMN) {
//			for (int i = 0; i <= (len / COLUMN); i++) {
//				for (int i = 0; i < MAXLINE - 1; i++) {
//					foldtemp[i] = '\0';
//				}
//				fold(i);
//			}
//		} else {
//			printf("%s", line);
//		}
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
//void fold(int n) {
//	int c = 0;
//	extern char line[], foldtemp[];
//
//	while (line[c] != '\n' && c < COLUMN) {
//		foldtemp[c] = line[c + (n * COLUMN)];
//		c++;
//	}
//	printf("%s\n", foldtemp);
//}
