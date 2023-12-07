#include <iostream>
#include <fstream>
#include <vector>
#include <tuple>
#include <queue>

using namespace std;
#define INF 1e7

int N, M, T;
// vector<int> A, B, C, E;

int main() {
//  uncomment the following lines if you want to read/write from files
//  ifstream cin("input.txt");
//  ofstream cout("output.txt");

    cin >> N >> M >> T;
    // A.resize(M);
    // B.resize(M);
    // C.resize(N);
    // E.resize(M);
    // for (int i=0; i<M; i++)
        // cin >> A[i] >> B[i] >> C[i] >> E[i];
    int dist[N];
    bool visited[N];
    vector<tuple<int,int,int>> adj[N];
    priority_queue<tuple<int,int>> q;
    

    for (int i = 0; i < M; i++) {
        int a, b, c, e;
        cin >> a >> b >> c >> e;
        adj[a].push_back({b, c, e});
    }

    for (int i = 0; i < N; i++) {
        dist[i] = INF;
        visited[i] = false;
    }
    dist[0] = 0;
    q.push({0,0});
    while (!q.empty()) {
        int curr = get<1>(q.top()); q.pop();
        if (!visited[curr]) {
            visited[curr] = true;
            for (auto n : adj[curr]) {
                int next = get<0>(n), w = get<1>(n);
                if (dist[curr] + w > T && get<2>(n)) continue;
                if (dist[curr] + w < dist[next]) {
                    dist[next] = dist[curr] + w;
                    q.push({-dist[next], next});
                }
            }
        }
    }
    
    cout << (dist[N - 1] == INF ? -1 : dist[N-1]) << endl;
    return 0;
}
