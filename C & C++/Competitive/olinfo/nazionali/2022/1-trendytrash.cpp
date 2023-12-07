#include <iostream>
#include <vector>
#include <string>
using namespace std;

int pulisci(int N, int M, vector<string> S);

int main()
{
	int N, M;

	cin >> N >> M;

	vector<string> S(N);

	for (int i = 0; i < N; i++)
	{
		cin >> S[i];
	}

	cout << pulisci(N, M, S) << endl;

	return 0;
}

#include <string>
#include <vector>
#include <algorithm>

using namespace std;

int clean(int N, int M, vector<string> &S) {
	int new_N = N, new_M = M, i = 0, k = 0;
	
	for (i = 0; i < S.size(); i++) {
		string s = S[i];
		if (s == string(M, '0') || s == string(M, '1')) {
			S.erase(S.begin() + i);
			new_N--;
			i--;
		}
	}

	if (S.empty()) return 0;

	for (k = 0; k < S[0].size(); k++) {
		i = 0;
		while (i < S.size() - 1 && S[i][k] == S[i + 1][k]) i++;
		if (i == S.size() - 1) {
			for (i = 0; i < S.size(); i++) {
				S[i].erase(S[i].begin() + k);
				if (S[i].size() == 0) {
					S.erase(S.begin() + i);
					new_N--;
					i--;
				}
			}
			new_M--;
			k--;
		}
	}
	
	return ((new_N == N && new_M == M || N == 0 || M == 0) ? (N*M) : clean(new_N, new_M, S));
}

int pulisci(int N, int M, vector<string> S) {
	return clean(N, M, S);
}