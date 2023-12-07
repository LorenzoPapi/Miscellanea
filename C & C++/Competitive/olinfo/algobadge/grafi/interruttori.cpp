#include <iostream>
#include <fstream>
#include <utility>
#include <vector>
#include <queue>
#include <algorithm>

using namespace std;
#define INF 99999999
int N, A, B;

int breadth_first_search(vector<int> adj[], int dist[], bool visited[], vector<int> Z) {
    queue<int> q;
    int node = 0;
    for (int z : Z) q.push(z);
    while (!q.empty()) {
        int curr = q.front();
        q.pop();
        if (!visited[curr]) {
            visited[curr] = true;
            node = curr;
            for (int next : adj[curr]) {
                dist[next] = min(dist[next], dist[curr] + 1);
                q.push(next);
            }
        }
    }
    return node;
}

void solve(int t) {
    cin >> N >> A >> B;

    vector<int> Z(A);
    vector<int> adj[N];
    bool visited[N];
    int dist[N];
    for (int i = 0; i < N; i++) {
        dist[i] = INF;
        visited[i] = false;
    }

    for (int i = 0; i < A; i++) {
        cin >> Z[i];
        dist[Z[i]] = 1;
    }

    for (int i = 0; i < B; i++) {
        int x, y; cin >> x >> y;
        adj[x].push_back(y);
        adj[y].push_back(x);
    }

    int idx = breadth_first_search(adj, dist, visited, Z);

    cout << "Case #" << t << ": " << idx << " " << dist[idx] << "\n";
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
