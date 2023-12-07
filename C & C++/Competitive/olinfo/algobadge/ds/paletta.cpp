#include <vector>
#include <algorithm>
using namespace std;
#define LSB(i) ((i) & (-i))
vector<int> tree;

long long mergeInv(vector<int>& arr) {
    long long inv_count = 0;
    for (int i = 0; i < arr.size() - 1; i++)
        for (int j = i + 1; j < arr.size(); j++)
            if (arr[i] > arr[j])
                inv_count++;

    return inv_count;
}

long long sum(int index) {
    long s = 0;
    while (index > 0) {
        s += tree[index];
        index -= LSB(index); // parent
    }
    return s;
}

void updateTree(int index, int value) {
    while (index < tree.size() ) {
        tree[index] += value;
        index += LSB(index); //CHILD
    }
}

void convert(vector<int>& vec) {
    vector<int> sorted;
    for (int v : vec) sorted.push_back(v);
    sort(sorted.begin(), sorted.end());

    for (int i = 0; i < vec.size(); i++) vec[i] = distance(sorted.begin(), lower_bound(sorted.begin(), sorted.end(), vec[i])) + 1;
}

long long inversions(vector<int>& vec) {
    convert(vec);
    tree.clear();
    tree.resize(vec.size() + 1);    
    long long c = 0;
    for (int i = vec.size() - 1; i >= 0; i--) {
        c += sum(vec[i]);
        updateTree(vec[i], 1);
    }
    return c;
}

long long paletta_sort(int N, int V[]) {
	vector<int> odd, even;
	for (int i = 0; i < N; i++) {
		if (i % 2 != V[i] % 2) return -1;
		if (i % 2 == 1) odd.push_back(V[i]);
		else even.push_back(V[i]);
	}
    if (N <= 50000) return mergeInv(odd) + mergeInv(even);
    return inversions(odd) + inversions(even);
}
