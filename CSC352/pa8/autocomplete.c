/* Name: Saul Weintraub
 * Course: CSC352 Sring Session 2022
 * Date: 4/12/2022
 *
 * This file will implement the functions defined in autocomplete.h.
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
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

/* This function will construct a blank LookupTreeNode with 2 fields.
 * One of the fields is an array of 26 children nodes and the other field is
 * a blank wordlist.
 */
LookupTreeNode *ltn_create(){
	LookupTreeNode *node = malloc(sizeof(LookupTreeNode));
	WordList *wl = wl_create();
	node->result_words = wl;
	for(int i = 0; i < 26; i++){
		node->children[i] = NULL;
	}

	return node;
}

/* This function will destroy a LookupTreeNode and will free the memory
 * allocated.
 */
void ltn_destroy(LookupTreeNode *list){
	for(int i = 0; i < 26; i++){
		if(list->children[i] != NULL){
			ltn_destroy(list->children[i]);
		}
	}
	wl_destroy(list->result_words);
	free(list);
}

/*
 * This function will add a specified result word to the tree by following the path
 * defined in the search_word parameter.
 */
void ltn_add_result_word(LookupTreeNode *ltn, char *search_word, char *result_word){
	if(search_word[0] == '\0'){
		wl_add(ltn->result_words, result_word);
		return;
	}

	char cur_letter = search_word[0];
	int child_index = (int)cur_letter - 65;
	char *new_search_word = calloc(strlen(search_word), sizeof(char));
	for(int i = 0; i < strlen(search_word) - 1; i++){
		new_search_word[i] = search_word[1 + i];
		new_search_word[i+1] = '\0';
	}

	if(ltn->children[child_index] == NULL){
		LookupTreeNode *child = ltn_create();
		ltn->children[child_index] = child;
	}

	ltn_add_result_word(ltn->children[child_index], new_search_word, result_word);

	free(new_search_word);
}

/* 
 * This function will count how many total nodes there are in the tree.
 */
int node_count(LookupTreeNode *root){
	int count = 1;
	for(int i = 0; i < 26; i++){
		if(root->children[i] != NULL){
			count += node_count(root->children[i]);
		}
	}
	return count;
}

/* This function will count how many result words are stored in the tree.
 */
int result_count(LookupTreeNode *root){
	int r_count = 0;
	r_count += root->result_words->count;
	for(int i = 0; i < 26; i++){
		if(root->children[i] != NULL){
			r_count += result_count(root->children[i]);
		}
	}
	return r_count;
}

/* This function will traverse the tree according to the path defined in
 * the search word and will return the node of the tree it lands on.
 */
LookupTreeNode *lookup(LookupTreeNode *root, char *search){	
	if(search[0] == '\0'){
		return root;
	}

	char cur_letter = to_upper(search[0]);
	int child_index = (int)cur_letter - 65;
	char *new_search = calloc(strlen(search), sizeof(char));
	for(int i = 0; i < strlen(search) - 1; i++){
		new_search[i] = search[1 + i];
		new_search[i+1] = '\0';
	}

	if(root->children[child_index] == NULL){
		return NULL;
	}

	LookupTreeNode *result = lookup(root->children[child_index], new_search);
	free(new_search);
	return result;
}

/* This function is a helper function to build_tree_from_words_helper. This function
 * will add a single word to the tree.
 */
void build_tree_from_words_helper(LookupTreeNode *root, char *word){
	int len = strlen(word);
	char *word_splice = calloc(len + 1, sizeof(char));
	for(int i = 1; i <= len; i++){
		int word_splice_index = 0;
		word_splice[0] = '\0';
		for(int j = len - i; j < len; j++){
			word_splice[word_splice_index] = word[j];
			word_splice[word_splice_index + 1] = '\0';
			word_splice_index += 1;
		}
		ltn_add_result_word(root, word_splice, word);
	}
	free(word_splice);
}

/* This function will create a Lookup Tree based on the words stored in the worlist.
 */
LookupTreeNode *build_tree_from_words(WordList *words){
	int word_count = words->count;
	LookupTreeNode *root = ltn_create();
	
	for(int i = 0; i < word_count; i++){
		build_tree_from_words_helper(root, words->words[i]);
	}
	return root;
}

/* This function will print out the words stored in the Lookup Tree.
 */
void print_words(LookupTreeNode *result, char *search){
	int word_count = result->result_words->count;
	for(int i = 0; i < word_count; i++){
		printf("The string '%s' was found inside the word ", search);
		printf("'%s'\n", result->result_words->words[i]);
	}
	
	for(int j = 0; j < 26; j++){
		if(result->children[j] != NULL){
			print_words(result->children[j], search);
		}
	}
}
