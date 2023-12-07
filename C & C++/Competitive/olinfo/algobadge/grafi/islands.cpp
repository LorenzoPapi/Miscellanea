#include <iostream>
using namespace std;
#define MAXN 1000

int R, C, i, j;
bool M[MAXN + 1][MAXN + 1];
int d[] = {1, -1, 0, 0};

bool dfs_island(int x, int y) {
    M[x][y] = false;
    bool isl = true;
    for (int i = 0; i < 4; i++) {
        int dx = x + d[i], dy = y - d[3 - i];
        if (dx >= 0 && dx < R && dy >= 0 && dy < C && M[dx][dy]) isl &= dfs_island(dx, dy);
    }
    return x != 0 && x != R - 1 && y != 0 && y != C - 1 && isl;
}

int main() {
    cin >> R >> C;
    for(i=0; i<R; i++)
        for (j=0; j<C; j++)
            cin >> M[i][j];
    
    int connections = 0;
    for (i = 1; i < R - 1; i++)
        for (j = 1; j < C - 1; j++)
            if (M[i][j])
                connections += dfs_island(i, j);
        
    cout << connections << endl;
    return 0;
}
