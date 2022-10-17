#include <stdio.h>
#include <stdlib.h>
#include "zstr.h"

int get_len(char* initial_data){
	int i = 0;
	while(initial_data[i] != '\0'){
		i = i + 1;
	}

	return i;
}

int get_allocated_size(int num_chars){
	int size_required = num_chars + (sizeof(int) * 2) + 1;
	if(size_required <= 16){
		return 16;
	} else if(size_required <= 32){
		return 32;
	} else if(size_required <= 256){
		return 256;
	} else if(size_required <= 1024){
		return 1024;
	} else if(size_required <= 2048){
		return 2048;
	} else{
		zstr_status = ZSTR_ERROR;
		return 0;
	}
}

zstr zstr_create(char* initial_data){
	int len = get_len(initial_data);

	int allocated_size = get_allocated_size(len);
	zstr new = malloc(allocated_size);
	if(new == NULL){
		zstr_status = ZSTR_ERROR;
		return new;
	}
	int* int_location = ((int*)new);
	*int_location = len;
	int* int_location2 = int_location+1;
	*int_location2 = allocated_size;
	zstr data = (zstr)(int_location2+1);
	for(int i = 0; i < len; i++){
		data[i] = initial_data[i];
	}
	data[len] = '\0';

	return data;	
}

void zstr_destroy(zstr to_destroy){
	free(to_destroy - 8);
}

void zstr_append(zstr* base, zstr to_append){
	zstr base_zstr = *base;
	int base_len = *((int*)base_zstr - 2);
	int base_alloc = *((int*)base_zstr - 1);
	int to_len = *((int*)to_append - 2);
	int len = base_len + to_len;

	if((to_len + base_len + 1 + 8) < base_alloc){
		int* int_location = ((int*)base_zstr - 2);
		*int_location = len;
		for(int i = 0; i <= to_len; i++){
			base_zstr[i + base_len] = to_append[i];
		}
		*base = base_zstr;
		return;
	}

	char* string  = malloc(sizeof(char) * (len + 1));
	for(int i = 0; i < len; i++){
		if(i < base_len){
			string[i] = base_zstr[i];
		} else{
			string[i] = to_append[i - base_len];
		}
	}
	string[len] = '\0';
	
	
	base_zstr = zstr_create(string);

	zstr_destroy(*base);
	//zstr new_zstr = zstr_create(string);
	*base = base_zstr;

}

int zstr_index(zstr base, zstr to_search){
	int base_len = *((int*)base - 2);
	int to_search_len = *((int*)to_search - 2);
	int base_index = 0;
	int search_index = 0;
	while(base_index < (base_len - to_search_len)){
		while(search_index < to_search_len){
			if(base[base_index + search_index] != to_search[search_index]){
				break;
			} else{
				search_index = search_index + 1;
				if(search_index == to_search_len){
					return base_index;
				}
			}
		}
		base_index = base_index + 1;
	}

	return -1;
}

int zstr_count(zstr base, zstr to_search){
	int base_len = *((int*)base - 2);
	int to_search_len = *((int*)to_search - 2);
	int base_index = 0;
	int search_index = 0;
	int count = 0;
	while(base_index < (base_len - to_search_len + 1)){
		search_index = 0;
		while(search_index < to_search_len){
			if(base[base_index + search_index] != to_search[search_index]){
				break;
			} else{
				search_index = search_index + 1;
				if(search_index == to_search_len){
					count = count + 1;
				}
			}
		}
		base_index = base_index + 1;
	}

	return count;

}

int zstr_compare(zstr x, zstr y){
	int x_len = *((int*)x - 2);
	int y_len = *((int*)y - 2);
	int x_index = 0;

	while((x_index < x_len) && (x_index < y_len)){
		if(x[x_index] > y[x_index]){
			return ZSTR_GREATER;
		} else if(x[x_index] < y[x_index]){
			return ZSTR_LESS;
		} else{
			x_index = x_index + 1;
		}
	}

	if(x_len > y_len){
		return ZSTR_GREATER;
	} else if(x_len < y_len){
		return ZSTR_LESS;
	} else{
		return ZSTR_EQUAL;
	}
}

zstr zstr_substring(zstr base, int begin, int end){
	int len = end - begin;
	char* sub = malloc(sizeof(char) * (len + 1));
	for(int i = 0; i < len; i++){
		sub[i] = base[begin + i];
	}
	sub[len] = '\0';

	zstr sub_string = zstr_create(sub);
	free(sub);
	return sub_string;
}

void zstr_print_detailed(zstr data){
	int len = *((int*)data - 2);
	int allocated = *((int*)data - 1);
	printf("STRLENGTH: %d\n", len);
	printf(" DATASIZE: %d\n", allocated);
	printf("   STRING: >");
	for(int i = 0; i < len; i++){
		printf("%c", data[i]);
	}
	printf("<\n");

}
