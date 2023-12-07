#include <vector>
#include <algorithm>
#include <numeric>
using namespace std;

vector<long long> calcola(int T, int M, vector<long long> S, vector<long long> P) {
    vector<long long> R(M);
    vector<long long> sum(S.size());
    vector<long long> num(S.size());
    num[0] = S[0];
    
    for (int i = 1; i < sum.size(); i++) sum[i] = sum[i - 1] + i * S[i];
    for (int i = 1; i < num.size(); i++) num[i] = num[i - 1] + S[i];
    
    for (int i = 0; i < M; i++) {
        if (P[i] > sum[T - 1]) R[i] = num[T - 1];
        else {
            auto lim = upper_bound(sum.begin(), sum.end(), P[i]);
            int dist = distance(sum.begin(), lim);
            if (dist) R[i] = num[dist - 1] + (P[i] - sum[dist - 1]) / dist;
            else R[i] = S[0];
        }
    }

    return R;
}
