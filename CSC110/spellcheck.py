###
### Author: Saul Weintraub
### Course: CSc 110
### Description:
###
###
###
def file_processer():
    misspellings_file = open('misspellings.txt', 'r')
    misspellings_dictionary = {}
    for line in misspellings_file:
        line = line.strip('\n')
        seperated_line = line.split(':')
        correct_word = seperated_line[0]
        misspelled_words_list = seperated_line[1].split(',')
        for word in misspelled_words_list:
            misspellings_dictionary[word] = correct_word
    misspellings_file.close()
    return misspellings_dictionary
def input_file_sorting():
    input_file_name = input('Enter input file:\n')
    input_file = open(input_file_name, 'r')
    input_file_list = input_file.readlines()
    input_file.close()
    return input_file_list
def replace_mode(input_file_list, misspellings_dictionary):
    print('\n--- OUTPUT ---')
    for line in input_file_list:
        line = line.strip('\n')
        words_list = line.split()
        for word in words_list:
            if word[0].isupper() == True:
                capital_replace_mode(input_file_list, misspellings_dictionary, word)
            elif word in misspellings_dictionary:
                print(misspellings_dictionary[word], end =' ')
            else:
                print(word, end = ' ')
        print('')
def suggest_mode(input_file_list, misspellings_dictionary):
    print('\n--- OUTPUT ---')
    legend_dictionary = {}
    legend_counter = 1
    for line in input_file_list:
        line = line.strip('\n')
        words_list = line.split()
        for word in words_list:
            if word[0].isupper() == True:
                lowercase_word = word.lower()
                if lowercase_word in misspellings_dictionary:
                    print(word, '('+str(legend_counter)+')', end = ' ')
                    legend_dictionary[legend_counter]
            elif word in misspellings_dictionary:
                print(word, '('+str(legend_counter)+')', end = ' ')
                legend_dictionary[legend_counter] = misspellings_dictionary[word]
                legend_counter += 1
            else:
                print(word, end = ' ')
        print('')
    print('\n--- LEGEND ---')
    i = 1
    while i < legend_counter:
        print('('+str(i)+')', legend_dictionary[i])
        i+=1
def capital_replace_mode(input_file_list, misspellings_dictionary, word):
    lowercase_word = word.lower()
    if lowercase_word in misspellings_dictionary:
        lowercase_correct_word = misspellings_dictionary[lowercase_word]
        uppercase_correct_word = lowercase_correct_word[0].upper()+lowercase_correct_word[1:]
        print(uppercase_correct_word, end = ' ')
    else:
        print(word, end = '')
def
def main():
    misspellings_dictionary = file_processer()
    input_file_list = input_file_sorting()
    mode = input('Enter spellcheck mode (replace or suggest):\n')
    if mode == 'replace':
        replace_mode(input_file_list, misspellings_dictionary)
    else:
        suggest_mode(input_file_list, misspellings_dictionary)
main()