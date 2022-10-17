#include <stdio.h>
#include <stdlib.h>

int encryptLine(char key[], char line[], int  index){
	char encrypted[128];
	int i = index;
	int j = 0;
	
	while((line[j] != '\0') && (line[j] != '\n')){
		if((key[i] == '\0') || (key[i] == '\n')){
			i = 0;
		}
		if(line[j] != 32){
			int char1 = key[i] - 64;
			int char2 = line[j] - 64;
			int encryptedAscii = char1 - char2 + 1;
			
			if(encryptedAscii < 1){
				encryptedAscii = encryptedAscii + 26;
			} else if(encryptedAscii > 26){
				encryptedAscii = encryptedAscii - 26;
			}
			encryptedAscii = encryptedAscii + 64;

			char encryptedChar = encryptedAscii;
			encrypted[j] = encryptedChar;
			
			i = i + 1;
			j = j + 1;
		} else{
			char encryptedChar = 32;
			encrypted[j] = encryptedChar;
			j = j + 1;
		}
	}
	encrypted[j] = '\0';
	printf("%s\n", encrypted);
	return i;
}


int main() {
	char key[128];
	char line[128];
	int i = 0;
	fgets(key, 127, stdin);

	while(fgets(line, 127, stdin) != NULL){
		i = encryptLine(key, line, i);	
	}
	printf("\n");

	return 0;
}
