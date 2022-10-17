/* Name: Saul Weintraub
 * Course: CSC352 Sring Session 2022
 * Date: 3/30/2022
 *
 * This file will implement the functions defined in autocomplete.h.
 */

#include <stdio.h>
#include <stdlib.h>
#include "autocomplete.h"

/* This function will create a blank WordList object and initialize the count to 0,
 * the alloc to 4, and the words buffer to a buffer that can hold 4 pointers.
 * 
 * returns an empty WordList object 
 */
WordList *wl_create(){
	WordList *wl = malloc(sizeof(WordList));
	char **defaultWords = malloc(4 * sizeof(char*));
	wl->words = defaultWords;
	wl->count = 0;
	wl->alloc = 4;
	return wl;
}

/* This function will free the memory of a WordList object. Specifically the buffer
 * for the struct, and the buffer for the array of pointers stored in the words field.
 */
void wl_destroy(WordList *list){
	free(list->words);
	free(list);
}

/* This function will add a word to the WordList object by adding the pointer to the
 * word to the words buffer and will then increment the count field by one. If there
 * is not enough room remaining in the buffer to add a word then the allocated size of
 * the words buffer will be doubled and the alloc field will be updated to reflect
 * this.
 */
int wl_add(WordList *list, char *word){
	if(list->count == list->alloc){
		//resize
		list->words = realloc(list->words, list->alloc * 2 * sizeof(char*));
		list->alloc = list->alloc * 2;
	}
	list->words[list->count] = word;
	list->count += 1;
	return 0;
}

/* This function will return the uppercase version of an alphabetic character if that
 * character is a lowercase letter, otherwise this function will return the original
 * character.
 */
char to_upper(char letter){
	if((letter >= 97) && (letter <= 122)){
		return letter -32;
	}
	return letter;
}

/* This function will create and return a wordlist based on the words in the passed
 * FILE*. The file will have a max of one word per line and there will be no whitespace
 * in the file other than newlines. The words will be converted to uppercase.
 */
WordList *build_wordlist_from_file(FILE *fp){
	WordList *list = wl_create();
	char line[128];
	
	while(fgets(line, 127, fp) != NULL){
		if(line[0] != '\n'){	
			char *word = malloc(sizeof(line));
			int i = 0;
			while((line[i] != '\0') && (line[i] != '\n')){
				word[i] = to_upper(line[i]);
				i += 1;
			}
			word[i] = '\0';
			wl_add(list, word);
		}
	}
	return list;
}

