#include <iostream>
#include <vector>
#include <algorithm>

using namespace std;
#define MAX_SIZE 7000
int R, C, K, A, B;
int M[MAX_SIZE][MAX_SIZE];

void solve(int t) {
    cin >> R >> C >> K >> A >> B;

    for (int i = 0; i < R; i++)
        for (int j = 0; j < C; j++)
            M[i][j] = 0;

    for (int i = 0; i < K; i++) {
        int r, c;
        cin >> r >> c;
        M[r][c]++;
    }
    
    for (int i = 0; i < R; i++) {
        for (int j = 1; j < C; j++) {
            M[i][j] += M[i][j-1];
        }
    }
    for (int j = 0; j < C; j++) {
        for (int i = 1; i < R; i++) {
            M[i][j] += M[i-1][j];
        }
    }

    int a = A - 1, b = B - 1, min_trees = K;
    while (a < R) {
        min_trees = min(min_trees, M[a][b] - (a >= A ? M[a - A][b] : 0) - (b >= B ? M[a][b - B] : 0) + (a >= A && b >= B ? M[a - A][b - B] : 0));
        if (++b == C) {
            if (min_trees == 0) break;
            b = B - 1;
            a++;
        }
    }

    cout << "Case #" << t << ": " << min_trees << endl;
}

int main() {
    // se preferisci leggere e scrivere da file
    // ti basta decommentare le seguenti due righe:

    freopen("input.txt", "r", stdin);
    freopen("output.txt", "w", stdout);

    int T;
    cin >> T;

    for (int t = 1; t <= T; t++) {
        solve(t);
    }

    return 0;
}
