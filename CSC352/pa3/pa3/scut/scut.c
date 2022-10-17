#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <string.h>

int find_sequence_size(char sequence[]){
	int sequence_size = 0;
	int i = 0;
	char temp[100];
	int temp_i = 0;
	bool calculate_range = false;
	int range_start = 0;
	int range_end = 0;

	if((sequence[0] < 48) || (sequence[0] > 57)){
		return -1;
	}

	while(sequence[i] != '\0'){
		if(sequence[i] == ','){
			if(calculate_range == false){
				sequence_size = sequence_size + 1;
			} else{
				range_end = atoi(temp);
				sequence_size = sequence_size + range_end - range_start + 1;
				calculate_range = false;
			}
			temp[0] = '\0';
			temp_i = 0;
		} else if(sequence[i] == '-'){
			calculate_range = true;
			range_start = atoi(temp);
			temp[0] = '\0';
			temp_i = 0;	
		} else if((sequence[i] >= 48) && (sequence[i] <= 57)){
			temp[temp_i] = sequence[i];
			temp[temp_i + 1] = '\0';
			temp_i = temp_i + 1;
		} else{
			return -1;
		}
		i = i + 1;
	}

	if((sequence[i - 1] < 48) || (sequence[i - 1] > 57)){
		return -1;
	}

	if(calculate_range == false){
		sequence_size = sequence_size + 1;
	} else{
		range_end = atoi(temp);
		sequence_size = sequence_size + range_end - range_start + 1;
		calculate_range = false;
	}
	
	return sequence_size;
}

void fill_sequence_array(char sequence[], int *sequence_array){
	int sequence_index = 0;

	int i = 0;
	char temp[100];
	int temp_i = 0;
	bool calculate_range = false;
	int range_start = 0;
	int range_end = 0;
	while(sequence[i] != '\0'){
		if(sequence[i] == ','){
			if(calculate_range == false){
				sequence_array[sequence_index] = atoi(temp);
				sequence_index = sequence_index + 1;
			} else{
				range_end = atoi(temp);
				for(int x = range_start; x <= range_end; x++){
					sequence_array[sequence_index] = x;
					sequence_index = sequence_index + 1;
				}
				calculate_range = false;
			}
			temp[0] = '\0';
			temp_i = 0;
		} else if(sequence[i] == '-'){
			calculate_range = true;
			range_start = atoi(temp);
			temp[0] = '\0';
			temp_i = 0;	
		} else if((sequence[i] >= 48) && (sequence[i] <= 57)){
			temp[temp_i] = sequence[i];
			temp[temp_i + 1] = '\0';
			temp_i = temp_i + 1;
		}
		i = i + 1;
	}
	
	if(calculate_range == false){
		sequence_array[sequence_index] = atoi(temp);
		sequence_index = sequence_index + 1;
	} else{
		range_end = atoi(temp);
		for(int x = range_start; x <= range_end; x++){
			sequence_array[sequence_index] = x;
			sequence_index = sequence_index + 1;
		}
	}
}

int process_line(char line[], char flag, char split_line[][100]){
	char slice[100];
	int slices = 0;
	int end = 0;
	int cur = 0;
	bool add_slice = false;
	if(flag == 'w'){
		while((line[cur] != '\0') && (line[cur] != '\n')){
			if((line[cur] != 9) && (line[cur] != 32)){
				if(add_slice){
					strcpy(split_line[slices], slice);
					slices = slices + 1;
					add_slice = false;
					slice[0] = '\0';
					end = 0;
				}
				slice[end] = line[cur];
				slice[end + 1] = '\0';
				end = end + 1;
				cur = cur + 1;
			} else{
				add_slice = true;
				cur = cur + 1;
			}
		}
		if((line[cur - 1] != 9) && (line[cur - 1] != 32)){
			strcpy(split_line[slices], slice);
			slices = slices + 1;
		}
	} else if(flag == 'c'){
		while((line[cur] != '\0') && (line[cur] != '\n')){
			if(line[cur] != 44){
				if(add_slice){
					strcpy(split_line[slices], slice);
					slices = slices + 1;
					add_slice = false;
					slice[0] = '\0';
					end = 0;
				}
				slice[end] = line[cur];
				slice[end + 1] = '\0';
				end = end + 1;
				cur = cur + 1;
			} else{
				add_slice = true;
				cur = cur + 1;
			}
		}
		if(line[cur - 1] != 44){
			strcpy(split_line[slices], slice);
			slices = slices + 1;
		}
	}
	return slices;
}

void cut_letter(char line[], int sequence_array[], int sequence_size){
	int line_length = strlen(line);

	for(int i = 0; i < sequence_size; i++){
		int sequence = sequence_array[i];
		if(sequence <= line_length){
			sequence = sequence - 1;
			printf("%c ", line[sequence]);
		}
	}
	printf("\n");
}

void cut(char line[], int sequence_array[], int sequence_size,  char flag){
	if(flag == 'l'){
		cut_letter(line, sequence_array, sequence_size);
		return;
	}

	char split_line[100][100];
	int slices = process_line(line, flag, split_line);
	
	for(int i = 0; i < sequence_size; i++){
		int sequence = sequence_array[i];
		if(sequence <= slices){
			sequence = sequence - 1;
			printf("%s ", split_line[sequence]);
		}
	}
	printf("\n");

}


int main(int argc, char * argv[]){
	if(argc != 3){
		fprintf( stderr, "expected 2 command line arguments.\n");
		return -1;
	}
	
	char flag;
	if(strcmp(argv[1], "-l") == 0){
		flag = 'l';
	} else if(strcmp(argv[1], "-w") == 0){
		flag = 'w';
	} else if(strcmp(argv[1], "-c") == 0){
		flag = 'c';
	} else{
		fprintf( stderr, "Invalid delimiter type.\n");
		return -2;
	}

	int sequence_size = find_sequence_size(argv[2]);
	if(sequence_size < 0){
		fprintf( stderr, "Invalid selection.\n");
		return -3;
	}

	int sequence_array[sequence_size];
	fill_sequence_array(argv[2], sequence_array);

	char line[101];
	while(fgets(line, 100, stdin) != NULL){
		cut(line, sequence_array, sequence_size, flag);
	}

	return 0;
}
