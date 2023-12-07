int potenza(long long N, long long c, long long d) {
	long long dec = 1LL;
	while (d > 0) {
		if (d & 1) dec = (dec * c) % N;
		c = (c * c) % N;
		d /= 2;
	}
	return dec;
}

void decifra(int N, int d, int L, int* messaggio, char* plaintext) {
    plaintext[L] = '\0';
	for (int i = 0; i < L; i++) plaintext[i] = potenza(N, messaggio[i], d);
}
