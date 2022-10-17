###
###Author: Saul Weintraub
###Course: CSC 110
###Description: This program will print either a vertical or horizontal plot, depending on the
###             user's input. The user will input a string of even length that will be evaluated
###             in pairs. Each pair must start with either the letter 'u' or the letter 'd' and
###             must then be followed by a number. If the pair starts with a 'u' the plot will go up
###             and if the pair starts with a 'd' the plot will go down. This program utilizes while
###             statements, if statements, else statements, and a while statement nested in a while
###             statement.
###
category_test = False
while category_test != True:
    category = input('Enter stock plotter mode:\n')
    if category == 'horizontal' or 'vertical':
        category_test = True
plot_string = input('Enter stock plot input string:\n')
while len(plot_string) % 2 != 0:
    plot_string = input('Enter stock plot input string:\n')

###Vertical Plot
if category == 'vertical':
    index = 0
    left_space = 8
    right_space = 8
    print('#' * 19)
    while index < len(plot_string):
        if plot_string[index] == 'u':
            left_space += int(plot_string[index+1])
            right_space -= int(plot_string[index+1])
        else:
            left_space -= int(plot_string[index+1])
            right_space += int(plot_string[index+1])
        print('#'+' '*left_space+'*'+' '*right_space+'#')
        index += 2
    print('#' * 19)

###Horizontal Plot
else:
    horizontal_counter = 17
    print('###'+'#' * int(len(plot_string)/2)+'#')
    while horizontal_counter > 0:
        plot_level = 9
        index = 0
        row_print = '# '
        while index < len(plot_string):
            if plot_string[index] == 'u':
                plot_level += int(plot_string[index+1])
            else:
                plot_level -= int(plot_string[index+1])
            if plot_level == horizontal_counter:
                row_print += '*'
            else:
                row_print += ' '
            index += 2
        print(row_print + ' #')
        horizontal_counter -= 1
    print('###'+'#' * int(len(plot_string)/2)+'#')