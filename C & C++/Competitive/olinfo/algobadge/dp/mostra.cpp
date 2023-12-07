#include <iostream>
#include <vector>
#include <cstdio>

using namespace std;

int solve() {
    int N, M;
    cin >> N >> M;

    vector<int> V(N), G(M);
    vector<vector<int>> funzione(N + 1, vector<int>(M + 1));

    for (int i=0; i<N; i++) {
        cin >> V[i];
    }
    for (int i=0; i<M; i++) {
        cin >> G[i];
    }

    for (int i = 0; i < N; i++) {
        for (int j = 0; j < M; j++) {
            funzione[i + 1][j + 1] = max(funzione[i + 1][j], funzione[i][j + 1]);
            if (G[j] > V[i]) funzione[i + 1][j + 1] = max(funzione[i + 1][j + 1], funzione[i][j] + 1);
        }
    }

    return N + funzione[N][M];
}

int main() {
    // se preferisci leggere e scrivere da file
    // ti basta decommentare le seguenti due righe:

    freopen("mostra_input_3.txt", "r", stdin);
    freopen("output.txt", "w", stdout);

    int T, t;
    cin >> T;

    for (t = 1; t <= T; t++) {
        cout << "Case #" << t << ": " << solve() << endl;
    }
}