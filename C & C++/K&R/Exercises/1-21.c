//#include <stdio.h>
//
//#define MAXLINE 1000 /* maximum input line size */
//char line[MAXLINE];	/* current input line */
//char entabbed[MAXLINE];	/* entabbed line saved here */
//
//int getline(void);
//void entab(void);
//
//main() {
//	int len;	/* current line length */
//	extern char entabbed[];
//
//	while ((len = getline()) > 0) {
//		entab();
//		printf("%s", entabbed);
//		for (int i = 0; i < MAXLINE - 1; i++) {
//			entabbed[i] = '\0';
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
//void entab(void) {
//	int linesize = 0;
//	int foundSpaces = 0;
//	int entabbedSize = 0;
//	extern char line[], entabbed[];
//
//	while (line[linesize] != '\0') {
//		char current = line[linesize];
//		if (current == ' ') {
//			if (line[linesize + 1] == ' ' && line[linesize + 2] == ' ' && line[linesize + 3] == ' ') {
//				entabbed[entabbedSize] = '\t';
//				foundSpaces = 1;
//				linesize += 3;
//				
//			} else {
//				entabbed[entabbedSize] = line[linesize];
//			}
//		} else {
//			foundSpaces = 0;
//			entabbed[entabbedSize] = line[linesize];
//		}
//		++linesize;
//		++entabbedSize;
//	}
//	++linesize;
//	entabbed[entabbedSize] = '\0';
//	/*while (entabbed[entabbedSize] != '\0') {
//		printf("%d ", entabbed[entabbedSize]);
//		++entabbedSize;
//	}
//	printf("\n");*/
//}
