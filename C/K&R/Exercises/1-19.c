//#include <stdio.h>
//#define MAXLINE 1000 /* maximum input line size */
//
//int getline(char line[], int maxline);
//void reverse(char to[], char from[]);
//
///* print longest input line */
//main() {
//	int len;	/* current line length */
//	char line[MAXLINE];	/* current input line */
//	char reversed[MAXLINE];	/* reversed line saved here */
//
//	while ((len = getline(line, MAXLINE)) > 0) {
//		reverse(reversed, line);
//		printf("%s", reversed);
//	}
//		
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
//void reverse(char to[], char from[]) {
//	int i, j;
//	i = j = 0;
//	while (from[i] != '\n')
//		++i;
//	--i;
//	while (i >= 0) {
//		to[j] = from[i];
//		--i;
//		++j;
//	}
//	to[j] = '\n';
//	++j;
//	to[j] = '\0';
//}