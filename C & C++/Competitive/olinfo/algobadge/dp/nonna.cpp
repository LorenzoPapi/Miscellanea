#include <stdio.h>
#include <assert.h>
#include <iostream>
#define MAXN 5001
#define MAXK 5001
#define MAXP 1000001

using namespace std;

int N, K, DP[MAXN][MAXK], P[MAXN];

int mangia(int pietanza, int peso) {
    if (peso < 1) return 0;
    if (pietanza == N) return MAXP;
    if (DP[pietanza][peso]) return DP[pietanza][peso];
    DP[pietanza][peso] = min(mangia(pietanza + 1, peso), mangia(pietanza + 1, peso - P[pietanza]) + P[pietanza]);
    return DP[pietanza][peso];
}

int main() {
    FILE *fr, *fw;
    
    fr = fopen("input.txt", "r");
    fw = fopen("output.txt", "w");
    assert(2 == fscanf(fr, "%d %d", &N, &K));
    for(int i = 0; i < N; i++) assert(1 == fscanf(fr, "%d", &P[i]));
    fprintf(fw, "%d\n", mangia(0, K));
    fclose(fr);
    fclose(fw);
    return 0;
}
