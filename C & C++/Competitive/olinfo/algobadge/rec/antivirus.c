#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define MAXN 1000
//#define MIN(a,b) ((a) < (b) ? a : b)

void solve(int t) {
    int N1, N2, N3, N4;
    int M;
    char F1[MAXN+1], F2[MAXN+1], F3[MAXN+1], F4[MAXN+1];

    // scrivi in queste variabili la risposta
    int p1, p2, p3, p4 = 0;

    scanf("%d %d %d %d", &N1, &N2, &N3, &N4);
    scanf("%d", &M);
    scanf("%s %s %s %s", F1, F2, F3, F4);

    char * files[3] = {F2, F3, F4};
    char * p[3] = {0, 0, 0};

    for (int i=0; i <= (N1 - M); i++) {
        char* virus = (char *) malloc(M *sizeof(char));
        strncpy(virus, F1 + i, M);
        int found = 0;
        for (int i = 0; i < 3; i++) {
            p[i] = strstr(files[i], virus); 
            if (p[i]) {
                found++;
            } else break;
        }
        if (found == 3) {
            p1 = i;
            p2 = p[0] - F2;
            p3 = p[1] - F3;
            p4 = p[2] - F4;
            break;
        }
    }

    printf("Case #%d: %d %d %d %d\n", t, p1, p2, p3, p4);
}

int main() {
    // le seguenti due righe possono essere utili per leggere e scrivere da file

    freopen("input.txt", "r", stdin);
    freopen("output.txt", "w", stdout);

    int T, t;
    scanf("%d", &T);

    for (t = 1; t <= T; t++) {
        solve(t);
    }
}
