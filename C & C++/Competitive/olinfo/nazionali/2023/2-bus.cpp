#include <iostream>
#include <vector>

using namespace std;

int pianifica(int N, int L, vector<vector<int>> F);

int main() {
    ios_base::sync_with_stdio(false);
    // se preferisci leggere e scrivere da file ti basta decommentare le seguenti due righe:
    // freopen("input.txt", "r", stdin);
    // freopen("output.txt", "w", stdout);

    int N, L;
    cin >> N >> L;

    vector<vector<int>> F(L);
    for (int i = 0; i < L; i++) {
        int K;
        cin >> K;
        F[i].resize(K);
        for (int j = 0; j < K; j++) {
            cin >> F[i][j];
        }
    }

    cout << pianifica(N, L, F) << endl;

    return 0;
}

#include <vector>
#include <deque>
#include <queue>
#include <unordered_set>
using namespace std;

int pianifica(int N, int L, vector<vector<int>> F) {
    vector<deque<int>> F_deque(L);

    vector<unordered_multiset<int>> bus_lines(N);

    queue<int> bfs;

    vector<int> distances(N, -1);

    for (int i = 0; i < L; i++) {
        for (int k = 0; k < F[i].size(); k++) {
            bus_lines[F[i][k]].insert(i);
            F_deque[i].push_back(F[i][k]);
        }
    }

    bfs.push(0);

    while (!bfs.empty() && distances[N-1] == -1) {
        int stop = bfs.front(); bfs.pop();
        int dist = distances[stop];
        
        while(bus_lines[stop].size() > 0) {
            auto line = *(bus_lines[stop].begin());
            while (bus_lines[stop].count(line) > 0) {
                int next_stop = F_deque[line].back();
                F_deque[line].pop_back();

                auto it = bus_lines[next_stop].find(line);
                if (it != bus_lines[next_stop].end()) bus_lines[next_stop].erase(it);

                if (distances[next_stop] != -1) continue;

                distances[next_stop] = dist + 1;
                bfs.push(next_stop);
            }
        }
    }

    return distances[N-1];
}