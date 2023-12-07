//#include <stdio.h>
//
//#define MAXLINE 1000 /* maximum input line size */
//char line[MAXLINE];	/* current input line */
//char decommented[MAXLINE];	/* entabbed line saved here */
//
//int getline(void);
//void decomment(void);
//
//main() {
//	int len;	/* current line length */
//	while ((len = getline()) > 0) {
//		for (int i = 0; i < MAXLINE - 1; i++) {
//			decommented[i] = '\0';
//		}
//		decomment();
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
//// TESTITTY TESTSTS
////less see
////if this works
//
//void decomment() {
//	extern char line[], decommented[];  //even with
//	int i = 0; //this
//	//TODO whyyyyyyyyyyy
//	while (line[i] != '/' && line[i] != 10 && line[i++] != '/') { //EEE
//		i--;
//		//printf("%d\n", line[i]);
//		decommented[i] = line[i];
//		i++;
//	}
//	decommented[i++] = '\n';
//	decommented[i++] = '\0';
//	printf("%s", decommented);
//}