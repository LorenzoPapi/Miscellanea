/*
 * This template is valid both in C and in C++,
 * so you can expand it with code from both languages.
 * NOTE: it is recommended to use this even if you don't
 * understand the following code.
 */

#include <stdio.h>
#include <assert.h>
#include <queue>
using namespace std;
// constraints
#define MAXN 1000000

// input data

int main() {
    int H[MAXN], N, P, i, m, destroyed = 0;
    queue<int> q;
//  uncomment the following lines if you want to read/write from files
//  freopen("input.txt", "r", stdin);
//  freopen("output.txt", "w", stdout);

    assert(2 == scanf("%d %d", &N, &P));
    for(i=0; i<N; i++)
        assert(1 == scanf("%d", &H[i]));

    for (i = 0; i < N; i++) {
        if ((i == 0 && H[i] > H[i+1]) || (H[i] > H[i-1] && H[i] > H[i+1]) || (i == N - 1 && H[i] > H[i - 1])) q.push(i); 
    }

    for (i = 0; i < P; i++) {
        queue<int> q2;
        destroyed += q.size();
        while (!q.empty()) {
            m = q.front();
            q.pop();
            H[m] = 0;

            if (((m == 1) || (m > 1 && H[m - 1] > H[m - 2])) && H[m - 1] != 0) q2.push(m - 1);
            if (((m == N - 2) || (m < N - 2 && H[m + 1] > H[m + 2])) && H[m + 1] != 0) q2.push(m + 1);
        }
        if (q2.empty())
            break;
        q = q2;
    }
    
    printf("%d\n", destroyed); // print the result
    return 0;
}
