//#include <stdio.h>
//
//#define MAXLINE 1000 /* maximum input line size */
//
//int getline(char line[], int maxline);
//void trim(char to[], char from[]);
//
///* print longest input line */
//main() {
//	int len;	/* current line length */
//	char line[MAXLINE];	/* current input line */
//	char trimmed[MAXLINE];	/* longest line saved here */
//
//	while ((len = getline(line, MAXLINE)) > 0) {
//		trim(trimmed, line);
//		printf("%s", trimmed);
//	}
//}
//
///* getline: read a line into s, return lenght */
//int getline(char s[], int lim) {
//	int c, i;
//
//	for (i = 0; i < lim - 1 && (c = getchar()) != EOF && c != '\n'; i++) {
//		s[i] = c;
//	}
//
//	if (c == '\n') {
//		s[i] = c;
//		++i;
//	}
//	s[i] = '\0';
//	return i;
//}
//
//#define SPACE_F 1
//#define SPACE_NF 0
//
//void trim(char to[], char from[]) {
//	int linesize, trimmedsize, i, found, trimindex;
//	linesize = trimmedsize = trimindex = 0;
//	found = SPACE_NF;
//	
//	while (from[linesize] != '\n')
//		linesize++;
//
//	if (from[linesize - 1] == ' ' || from[linesize - 1] == '\t') {
//		for (i = linesize - 1; i >= 0; i--) {
//			if ((from[i] == ' ' || from[i] == '\t')  && found == SPACE_NF) {
//				trimindex = i;
//			} else {
//				found = SPACE_F;
//			}
//		}
//		for (i = 0; i < trimindex; i++) {
//			to[i] = from[i];
//		}
//		printf("%d, %d\n", linesize - 1, trimindex - 1);
//		to[trimindex] = '\n';
//		to[++trimindex] = '\0';
//	} else {
//		for (i = 0; i <= linesize; i++) {
//			to[i] = from[i];
//		}
//		to[i] = '\0';
//	}
//	
//	
//}