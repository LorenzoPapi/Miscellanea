#include <stdio.h>
#include <assert.h>
#include <numeric>
#include <algorithm>

#define MAXN 100000
using namespace std;
int V[MAXN];

int sfangate(int N) {
    int total = accumulate(V, V + N, 0);
    if (total > 0) return 0;

    sort(V, V + N);
    int minimum = 0, last_negative = lower_bound(V, V + N, 0) - V;
    while (minimum < last_negative) {
        if (total > 2 * V[minimum]) return minimum + 1;
        total -= 2 * V[minimum++];
    }
    return last_negative;
}

int main() {
    FILE *fr, *fw;
    int N, i;

    fr = fopen("input.txt", "r");
    fw = fopen("output.txt", "w");
    assert(1 == fscanf(fr, "%d", &N));
    for(i=0; i<N; i++)
        assert(1 == fscanf(fr, "%d", &V[i]));

    fprintf(fw, "%d\n", sfangate(N));
    fclose(fr);
    fclose(fw);
    return 0;
}