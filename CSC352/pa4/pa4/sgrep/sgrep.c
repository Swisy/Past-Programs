#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <string.h>

bool valid_pattern(char pattern[]){
	int pattern_len = strlen(pattern);
	if(pattern[0] == '#'){
		return false;
	}
	if(pattern[pattern_len - 1] == '#'){
		return false;
	}
	
	bool letter_before = false;
	bool hashtag_before = false;

	for(int i = 0; i < pattern_len; i++){
		char a = pattern[i];
		if(a == '.'){
			if(hashtag_before){
				return false;
			}
			letter_before = false;
			hashtag_before = false;
		} else if(a == '#'){
			hashtag_before = true;
			if(!letter_before){
				return false;
			}
			letter_before = false;
		} else if((a >= 65) && (a <= 90)){
			letter_before = true;
			hashtag_before = false;
		} else if((a >= 97) && (a <= 122)){
			letter_before = true;
			hashtag_before = false;
		} else{
			return false;
		}

	}
	
	return true;
}

char other_case(char original){
	if((original >= 65) && (original <= 90)){
		return original + 32;
	} else if((original >= 97) && (original <= 122)){
		return original - 32;
	} else{
		return original;
	}
}

bool compare_char(char a, char b, bool case_enabled){
	if(a == b){
		return true;
	} else if(case_enabled){
		char other_a = other_case(a);
		char other_b = other_case(b);
		if((a == other_b) || (other_a == b) || (other_a == other_b)){
			return true;
		}
	}
	return false;
}

void grep(char line[], bool exact_enabled, bool case_enabled, char pattern[]){
	int line_len = strlen(line);
	int pattern_len = strlen(pattern);
	char slice[201];
	int slice_end = 0;

	for(int i = 0; i < (line_len - pattern_len); i++){
		int x = 0;
		slice_end = 0;
		slice[0] = '\0';
		while((x < pattern_len) && compare_char(line[i + x], pattern[x],case_enabled)){
			slice[slice_end] = line[i + x];
			slice[slice_end + 1] = '\0';
			slice_end = slice_end + 1;
			x = x + 1;
		}
		if(x == pattern_len){
			if(!exact_enabled){
				printf("%s", line);
				return;
			}
			printf("%s\n", slice);
		}
	}
}

void pattern_grep(char line[], bool exact_enabled, bool case_enabled, char pattern[]){
	int line_len = strlen(line);
	int pattern_len = strlen(pattern);
	char slice[201];
	int slice_end = 0;
	
	for(int i = 0; i < line_len; i++){
		int x = 0;
		int pattern_index = 0;
		slice[0] = '\0';
		slice_end = 0;
		bool skip_more = false;

		bool first_match = false;
		if(compare_char(line[i], pattern[0], case_enabled)){
			first_match = true;
		}
		if(pattern[0] == '.'){
			first_match = true;
		}

		while((pattern_index < pattern_len) && (x + i < line_len) && first_match){
			if(pattern[pattern_index] == '.'){
				slice[slice_end] = line[i+x];
				slice[slice_end + 1] = '\0';
				slice_end = slice_end + 1;
				pattern_index = pattern_index + 1;
			} else if(pattern[pattern_index] == '#'){
				skip_more = true;
				slice[slice_end] = line[i+x];
				slice[slice_end + 1] = '\0';
				slice_end = slice_end + 1;
				pattern_index = pattern_index + 1;
			} else{
				if(compare_char(line[i + x], pattern[pattern_index],case_enabled)){
					slice[slice_end] = line[i+x];
					slice[slice_end + 1] = '\0';
					slice_end = slice_end + 1;
					pattern_index = pattern_index + 1;
					skip_more = false;
				} else if(skip_more){
					slice[slice_end] = line[i+x];
					slice[slice_end + 1] = '\0';
					slice_end = slice_end + 1;
				} else{
					break;
				}
			}
			x = x + 1;

			if(pattern_index  == pattern_len){
				if(!exact_enabled){
					printf("%s", line);
					return;
				}
				printf("%s\n", slice);
			}

		}
	}
}

int main(int argc, char * argv[]){
	if(argc < 2){
		fprintf(stderr, "Requires more command-line arguments.\n");
		return -1;
	}

	bool exact_enabled = false, case_enabled = false, pattern_enabled = false;
	char pattern[201];
	for(int i = 1; i < argc; i++){
		if(strcmp(argv[i], "-o") == 0){
			exact_enabled = true;
		} else if(strcmp(argv[i], "-i") == 0){
			case_enabled = true;
		} else if(strcmp(argv[i], "-e") == 0){
			pattern_enabled = true;
		} else{
			strcpy(pattern, argv[i]);
		}
	}

	if(!valid_pattern(pattern)){
		fprintf(stderr, "Invalid search term.\n");
		return -2;
	}

	char line[201];
	while(fgets(line, 200, stdin) != NULL){
		if(pattern_enabled){
			pattern_grep(line, exact_enabled, case_enabled, pattern);
		} else{
			grep(line, exact_enabled, case_enabled, pattern);
		}
	}
	

	return 0;

}
