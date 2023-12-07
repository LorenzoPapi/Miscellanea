#include <iostream>
#include <vector>
using namespace std;

long long riciclo(int N, int M, vector<int> T, vector<int> P);

int main()
{
	int N, M;

	cin >> N >> M;

	vector<int> T(N), P(M);

	for (int i = 0; i < N; i++)
	{
		cin >> T[i];
	}
	for (int i = 0; i < M; i++)
	{
		cin >> P[i];
	}

	cout << riciclo(N, M, T, P) << endl;

	return 0;
}

#include <vector>
#include <algorithm>

using namespace std;

long long total = 0;

void add_truck(long long w, vector<long long> &c)
{
	for (int j = 0; j < 31; j++)
		if (1 & (w >> j))
			c[j]++;
}

void collapse(long long req, int base, int pos, vector<long long> &c, vector<int> &p)
{
	if (pos == 31) return;
	// q: portata misurata con la stessa potenza di req
	long long q = c[pos] << (pos - base);
	c[pos] = 0;
	if (req > q)
	{
		// se la richiesta non supera la portata allora si ricorre
		// a una potenza superiore, togliendo dalla richiesta il disponibile
		req -= q;
		total += q;
		collapse(req, base, pos + 1, c, p);
	}
	else
	{
		// la disponibilita' corrente e' azzerata
		// cio' che rimane dopo aver tolto la req e' reintrodotto come altro truck
		total += req;
		long long x = (q - req) << base;
		long long div = x >> pos;
		c[pos] = div;
		long long spez = x % (1 << pos);
		add_truck(spez, c);
	}
}

long long riciclo(int N, int M, vector<int> T, vector<int> P)
{
	vector<long long> c(31, 0);
	for (int i = 0; i < N; i++)
		add_truck(T[i], c);
	for (int j = 0; j < M; j++)
		collapse(P[j], j, j, c, P);
	return total;
}