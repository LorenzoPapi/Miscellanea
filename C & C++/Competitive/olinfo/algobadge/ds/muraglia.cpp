#include <vector>
#include <cmath>
using namespace std;
vector<int> tree;
int height = 0, half_size = 0, towers = 0;

pair<int, int> chiedi(int x) {
    pair<int, int> answer = {0, towers - 1};
    int index = x + half_size, value = tree[index];
    int i = 1, h = 1;

    if (x != 0) {
        while (i < half_size) {
            if (value >= tree[i]) break;
            if (i == (index >> (height - h))) {
                i = index >> (height - (++h));
            } else {
                i <<= 1;
                i++;
                h++;
            }
            while (i > (1 << (h - 1)) && value >= tree[i]) i--;
        }
        answer.first = max(i - half_size, 0);
    }

    i = 1, h = 1;
    if (x != towers - i) {
        while (i < half_size) {
            if (value >= tree[i]) break;
            if (i == (index >> (height - h)))
                i = index >> (height - (++h));
            else {
                i <<= 1;
                h++;
            }
            while (i < ((1 << h) - 1) && value >= tree[i]) i++;
        }
        if (i >= half_size) answer.second = min(i - half_size, towers - 1);
    }
    return answer;
}

void cambia(int x, int h) {
    int index = x + half_size;
    tree[index] = h;
    while (index /= 2) tree[index] = max(tree[index * 2], tree[index * 2 + 1]);
}

void inizializza(int N, vector<int> H) {
    height = ceil(log2(N)) + 1;
    towers = N;
    half_size = 1 << (height - 1);
    int size = half_size << 1;
    tree.resize(size);
    for (int i = 0; i < N; i++) {
    	tree[i + half_size] = H[i];
    }
    for (int i = 2; i < size; i <<= 1) {
        for (int j = 0; j < size / i; j += 2) {
            tree[j / 2 + size / i / 2 ] = max(tree[j + size / i], tree[j + size / i + 1]);
        }
    }
}
