#include <vector>
using namespace std;

#define MAX(a,b) ((a) > (b) ? (a) : (b))
int trova_massimo(int N, vector<int> V) {
    int m = -1001;
    for (int i : V) m = MAX(m, i);
    return m;
}
