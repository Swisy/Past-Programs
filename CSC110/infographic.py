###
### Author: Saul Weintraub
### Course: CSc 110
### Description: This program will read a file and create an infographic that lists the name of the
###              file, the number of unique words, the most used small, medium, and large words,
###              and 3 bar charts that visualize the ratios of the most common words, the ratio of
###              capitalized and uncapitalized words, and the ratio of words with punctuation.
###
from graphics import graphics
def file_processing(file_name):
    '''
    This function opens the file and reads all the words in and puts those words into a list.
    The function then returns the list.
    file_name: A string containing the name of the file.
    '''
    file = open(file_name, 'r')
    words_list = []
    for line in file:
        line = line.strip('\n')
        line_list = line.split()
        for word in line_list:
            words_list.append(word)
    file.close()
    return words_list

def counting_words(words_list):
    '''
    This function puts all the words from the words_list into a dictionary, with the key
    being the word and the value being the number of times that word appears. The function
    then returns the dictionary.
    words_list: A list containing all the words from the file.
    '''
    word_count_dictionary = {}
    for word in words_list:
        if word not in word_count_dictionary:
            word_count_dictionary[word] = 1
        else:
            word_count_dictionary[word] += 1
    return word_count_dictionary

def most_used_words(word_count_dictionary):
    '''
    This file determines the most used words of 3 different sizes. Small words which are between
    0 and 4 characters long, medium words which are between 5 and 7 characters long, and large words
    which are 8 or more characters long. The function will then return the most used small, medium,
    and large words.
    word_count_dictionary: A dictionary of all the words used and the amount of times they appear.
    '''
    most_small = ''
    most_medium = ''
    most_large = ''
    small_counter = 0
    medium_counter = 0
    large_counter = 0
    for word in word_count_dictionary:
        if len(word) <= 4:
            if word_count_dictionary[word] > small_counter:
                most_small = word
                small_counter = word_count_dictionary[word]
        elif len(word) <= 7:
            if word_count_dictionary[word] > medium_counter:
                most_medium = word
                medium_counter = word_count_dictionary[word]
        else:
            if word_count_dictionary[word] > large_counter:
                most_large = word
                large_counter = word_count_dictionary[word]
    return most_small, most_medium, most_large

def unique_capitalized_words_counter(word_count_dictionary):
    '''
    This function counts how many unique words used are capitalized. The function then returns
    the number of capitalized words.
    word_count_dictionary: A dictionary of all the words used and the amount of times they appear.
    '''
    capital_count = 0
    for word in word_count_dictionary:
        if word[0].isupper():
            capital_count += 1
    return capital_count

def unique_punctuated_words_counter(word_count_dictionary):
    '''
    This function counts how many unique words are punctuated. The function then returns the
    number of punctuated words.
    word_count_dictionary: A dictionary of all the words used and the amount of times they appear.
    '''
    punctuated_count = 0
    for word in word_count_dictionary:
        if word[-1] in [',', '.', '?', '!']:
            punctuated_count += 1
    return punctuated_count

def information(word_count_dictionary, most_small, most_medium, most_large, capital_count,\
    punctuated_count):
    '''
    This function processes data and returns the information that will be used in the infographic.
    word_count_dictionary: A dictionary of all the words used and the amount of times they appear.
    most_small: A string. The most used small word.
    most_medium: A string. The most used medium word.
    most_large: A string. The most used large word.
    capital_count: An int. How many unique capital words there are.
    punctuated_count: An int. How many unique punctuated words there are.
    '''
    most_used_string = most_small, '(' + str(word_count_dictionary[most_small]) + 'x)',\
        most_medium, '(' + str(word_count_dictionary[most_medium]) + 'x)', most_large, '(' + \
        str(word_count_dictionary[most_large]) + 'x)'
    word_length_total = word_count_dictionary[most_small] + word_count_dictionary[most_medium]\
        +word_count_dictionary[most_large]
    small_height = int((450/word_length_total) * word_count_dictionary[most_small])
    medium_height = int((450/word_length_total) * word_count_dictionary[most_medium])
    total_unique = len(word_count_dictionary)
    capital_height = int((450/total_unique) * capital_count)
    punctuated_height = int((450/total_unique) * punctuated_count)
    return most_used_string, small_height, medium_height, capital_height, punctuated_height

def infographic_drawer(gui, word_count_dictionary, most_used_string, small_height, medium_height,\
    capital_height, punctuated_height, file_name):
    '''
    This function draws the infographic.
    gui: The module used for drawing.
    word_count_dictionary: A dictionary of all the words used and the amount of times they appear.
    most_used_string: A string that will be drawn. Contains the most used words.
    small_height: An int. The height of the small section of the first bar graph.
    medium_height: An int. The height of the medium section of the first bar graph.
    capital_height: An int. The height of the capital words section of the second graph.
    punctuated_height: An int. The height of the punctuated section of the third graph.
    file_name: A string. The name of the file.
    '''
    gui.rectangle(-1, -1, 651, 701, 'dim gray')
    gui.rectangle(50, 200, 150, 450, 'dodger blue')
    gui.rectangle(50, 200+small_height, 150, 450-small_height, 'chartreuse4')
    gui.rectangle(50, 200+small_height+medium_height, 150, 450-small_height-medium_height,
        'dodger blue')
    gui.rectangle(250, 200, 150, 450, 'dodger blue')
    gui.rectangle(250, 200 + capital_height, 150 , 450 - capital_height, 'chartreuse4')
    gui.rectangle(450, 200, 150, 450, 'dodger blue')
    gui.rectangle(450, 200 + punctuated_height, 150, 450 - punctuated_height, 'chartreuse4')
    gui.text(50, 30, file_name, 'cyan', 19)
    gui.text(50, 80, 'Total Unique Words: ' + str(len(word_count_dictionary)), 'white', 15)
    gui.text(50, 130, 'Most used words (s/m/l):', 'white', 11)
    gui.text(250, 130, most_used_string, 'cyan', 11)
    gui.text(50, 175, 'Word lengths', 'white', 14)
    gui.text(250, 175, 'Cap/Non-Cap', 'white', 14)
    gui.text(450, 175, 'Punct/Non-Punct', 'white', 14)
    gui.text(53, 203, 'small words', 'white', 10)
    gui.text(53, 203 + small_height, 'medium words', 'white', 10)
    gui.text(53, 203 + small_height + medium_height, 'large words', 'white', 10)
    gui.text(253, 203, 'Capitalized', 'white', 10)
    gui.text(253, 203 + capital_height, 'Non Capitalized', 'white', 10)
    gui.text(453, 203, 'Punctuated', 'white', 10)
    gui.text(453, 203 + punctuated_height, 'Non Punctuated', 'white', 10)
def main():
    gui = graphics(650, 700, 'infographic')
    file_name = input('Enter the name of the file:\n')
    words_list = file_processing(file_name)
    word_count_dictionary = counting_words(words_list)
    most_small, most_medium, most_large = most_used_words(word_count_dictionary)
    capital_count = unique_capitalized_words_counter(word_count_dictionary)
    punctuated_count = unique_punctuated_words_counter(word_count_dictionary)
    most_used_string, small_height, medium_height, capital_height, punctuated_height\
        = information(word_count_dictionary, most_small, most_medium, most_large, capital_count,\
            punctuated_count)
    infographic_drawer(gui, word_count_dictionary, most_used_string, small_height, medium_height,\
        capital_height, punctuated_height, file_name)

main()