#include <stdio.h>
#include <assert.h>
#include <string>
#include <iostream>
#include <cmath>

#define MAX(a,b) ((a > b) ? (a) : (b))
#define MIN(a,b) ((a < b) ? (a) : (b))

using namespace std;

long long occulta(int N, long long M, string num, long long remainder) {
    long long l = 0;
    for (int i = 3; i < 10; i+=3) {
        if (num.empty() || num.at(num.size() - 1) != char(i + 0x30)) {
            long long mult = (num.empty() ? 0 : (atoll(num.c_str()) * 10)) + i;
            remainder = MAX(remainder, mult % M);
            if (num.size() + 1 < N) {
                l = occulta(N, M, num + char(i + 0x30), remainder);
                remainder = MAX(remainder, l);
                if (remainder == M - 1) break;
            }
        }
    }
    return remainder;
}

int main() {
    FILE *fr, *fw;
    int T, N, i;
    long long M;

    fr = fopen("input.txt", "r");
    fw = fopen("output.txt", "w");
    assert(1 == fscanf(fr, "%d", &T));
    for (i=0; i<T; i++) {
        assert(2 == fscanf(fr, "%d %lld", &N, &M));
        fprintf(fw, "%lld ", occulta(N, M, "", 0));
    }

    fprintf(fw, "\n");
    fclose(fr);
    fclose(fw);
    return 0;
}