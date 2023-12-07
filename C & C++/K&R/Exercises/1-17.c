//#include <stdio.h>
//#define MAXLINE 1000
//#define MINLINE 10
//
//int getline(char line[], int maxline);
//int copy(char to[], char from[], int i);
//
//main() {
//	int len, i;
//	char line[MAXLINE];
//	char longest[MAXLINE * 10];
//
//	i = 0;
//	while ((len = getline(line, MAXLINE)) > 0) {
//		if (len > MINLINE) {
//			i = copy(longest, line, i);
//		}
//	}
//	longest[i] = '\0';
//	printf("Longest lines:\n%s", longest);
//}
//
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
//int copy(char to[], char from[], int i) {
//	int j = 0;
//	while (from[j] != '\0') {
//		to[i] = from[j];
//		++j;
//		++i;
//	}
//	return i;
//}