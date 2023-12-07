#include <iostream>
#include <vector>
#include <queue>
#include <algorithm>

using namespace std;
#define MAX_NODES 1000
#define INF 99999999

vector<int> adjl[MAX_NODES];
bool visited[MAX_NODES];
bool matr[MAX_NODES][MAX_NODES];

void depth_first_search(int current) {
    visited[current] = true;
    for (int next : adjl[current]) {
        if (!visited[next]) depth_first_search(next);
    }
}

void breadth_first_search(int source, int nodes) {
    int dist[MAX_NODES];
    for (int i = 0; i < nodes; i++) dist[i] = INF;
    dist[source] = 0;
    queue<int> q;
    q.push(source);
    while (!q.empty()) {
        int curr = q.front();
        q.pop();
        for (int next : adjl[curr]) {
            if (dist[next] == INF) {
                dist[next] = dist[curr] + 1;
                q.push(next);
            }
        }
    }
}

int d[] = {1, -1, 0, 0};
void dfs(int x, int y, int n, int m) {
    matr[x][y] = true;
    for (int i = 0; i < 4; i++) {
        int dx = x + d[i], dy = y - d[3 - i];
        if (dx >= 0 && dx < n && dy >= 0 && dy < m && !matr[dx][dy]) dfs(dx, dy, n, m);
    }
}

int main() {
    int N, E;
    cin >> N >> E;
    for (int i = 0; i < E; i++) {
        int s, d;
        cin >> s >> d;
        adjl[s].push_back(d);
        //adjl[d].push_back(s);
    }
    for (vector<int> v : adjl) {
        for (int i : v) {
            cout << i << ", ";
        }
        cout << endl;
    }
    for (int i = 1; i <= N; i++) {
        if (!visited[i]) {
            depth_first_search(i);
        }
    }
}

int esercizio_3() {
    int n, m;
    cin >> n >> m;
    for (int i = 0; i < n; i++) {
        for (int j = 0; j < m; j++) {
            char c;
            cin >> c;
            matr[i][j] = (c == '#');
        }
    }
    int connections = 0;
    for (int i = 0; i < n; i++) {
        for (int j = 0; j < m; j++) {
            if (!matr[i][j]) {
                dfs(i, j, n, m);
                connections++;
            }
        }
    }
    cout << "Connections: " << connections << endl;
    return 0;
}

int esercizio_1() {
    // Adjacency matrix
    // vector<vector<bool>> adj(nodes, vector<bool>(nodes));
    // adj[0][0] = false; adj[0][1] = true;  adj[0][2] = false;  adj[0][3] = false; adj[0][4] = true;
    // adj[1][0] = true;  adj[1][1] = false; adj[1][2] = true;   adj[1][3] = true;  adj[1][4] = true;
    // adj[2][0] = false; adj[2][1] = true;  adj[2][2] = false;  adj[2][3] = true;  adj[2][4] = false;
    // adj[3][0] = false; adj[3][1] = true;  adj[3][2] = true;   adj[3][3] = false; adj[3][4] = true;
    // adj[4][0] = true;  adj[4][1] = true;  adj[4][2] = false;  adj[4][3] = true;  adj[4][4] = false;

    // Adjacency list
    int N, E;
    cin >> N >> E;
    for (int i = 0; i < E; i++) {
        int s, d;
        cin >> s >> d;
        adjl[s].push_back(d);
        adjl[d].push_back(s);
    }
    int connections = 0;
    for (int i = 1; i <=N; i++) {
        if (!visited[i]) {
            depth_first_search(i);
            connections++;
        }
    }
    cout << "Connections: " << connections << endl;

    return 0;
}
