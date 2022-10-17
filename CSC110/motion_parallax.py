###
### Author: Saul Weintraub
### Course: CSc 110
### Description: This program creates a landscape that conveys the effects of motion parallax.
###              As the users cursor moves the landscape also moves. The different layers move
###              at different rates to immitate motion parallax. Birds will also fly across the
###              screen.
from graphics import graphics
import random

#EXTRA CREDIT: Instead of the sky being blue I have created functions that will make the sky
#              a gradient of 2 random colors
def gradient_color_value_randomizer():
    '''
    This function will use the random module to get 3 different integer values between 0 and
    255 to use as the values for red, green, and blue. The function returns the 3 integer values
    for red, green, and blue in said order.
    '''
    red = random.randint(0, 255)
    green = random.randint(0, 255)
    blue = random.randint(0, 255)
    return red, green, blue
def color_componant_calculations(color_value1, color_value2):
    '''
    This function will perform calculations to determine how much the color value should
    change each line and how often the color value should change to create a consistant gradient.
    The function will return the integer values of what the color should change by and the modulus
    that should be used.
    color_value1: An integer value between 0 and 255
    color_value2: An integer value between 0 and 255
    '''
    color_difference = color_value2 - color_value1
    if color_difference < 0:
        color_change = -1
    elif color_difference > 0:
        color_change = 1
    else:
        color_change = 0

    if color_difference != 0:
        modulus = int((1/(abs(color_difference)/700)))+1
    else:
        modulus = 1
    return color_change, modulus
def gradient_layer(gui, red1, green1, blue1, red2, green2, blue2):
    '''
    This function will draw the gradient backround.
    gui: A graphics object. Draw with this.
    red1, green1, blue1: 3 integer values that represent the red value, green value,
    and blue value of a color. Created from the gradient_color_value_randomizer function.
    red2, green2, blue2:3 integer values that represent the red value, green value,
    and blue value of a color. Created from the gradient_color_value_randomizer function.
    '''
    red_change, red_modulus = color_componant_calculations(red1, red2)
    green_change, green_modulus = color_componant_calculations(green1, green2)
    blue_change, blue_modulus = color_componant_calculations(blue1, blue2)
    y_gradient = 1
    while y_gradient <= 700:
        color = gui.get_color_string(red1, green1, blue1)
        gui.line(0, y_gradient, 700, y_gradient, color, 1)
        if y_gradient % red_modulus == 1:
            red1 += red_change
        if y_gradient % green_modulus == 1:
            green1 += green_change
        if y_gradient % blue_modulus == 1:
            blue1 += blue_change
        y_gradient += 1

#end of extra credit functions

def random_color(gui):
    '''
    The purpose of this function is to create and return a random color.
    This function will get 3 random integers and will use them as values for red,
    green, and blue. The values of red, green, and blue will then be used to create
    a color. The created color will then be returned.
    gui: A graphics object. Draw with this.
    '''
    red = random.randint(0, 255)
    green = random.randint(0, 255)
    blue = random.randint(0, 255)
    return gui.get_color_string(red, green, blue)
def sun_layer(gui):
    '''
    This function will animate a sun that moves slightly with the users mouse. The position
    of the sun changes depending on the location of the users cursor.
    gui: A graphics object. Draw with this.
    '''
    x = (gui.mouse_x//50)+343
    y = (gui.mouse_y//50)+343
    gui.ellipse(x+150, y-250, 100, 100, 'yellow')
def middle_mountain_layer(gui, middle_color):
    '''
    This function will animate the middle mountain that moves slightly with the users mouse.
    The position of the mountain changes depending on the location of the users cursor.
    gui: A graphics object. Draw with this.
    middle_color: The color of the mountain. Created by the random_color function.
    '''
    x = (gui.mouse_x//30)+338
    y = (gui.mouse_y//30)+338
    gui.triangle(x-250, y+350, x, y-150, x+250, y+350, middle_color)
def side_mountains_layer(gui, left_side_color, right_side_color):
    '''
    This function will create the 2 side mountains that move slightly with the users mouse.
    The position of the mountains changes depending on the location of the users cursor.
    gui: A graphics object. Draw with this.
    left_side_color: The color of the left mountain. Created by the random_color function.
    right_side_color: The color of the right mountain. Created by the random_color function.
    '''
    x = (gui.mouse_x//14)+325
    y = (gui.mouse_y//14)+325
    gui.triangle(x-600, y+412, x-200, y-100, x+200, y+412, left_side_color)
    gui.triangle(x-200, y+412, x+200, y-100, x+600, y+412, right_side_color)
def foreground_layer(gui):
    '''
    This function will create the foreground which includes a tree, blades of grass, and the ground.
    The position of the foreground changes depending on the location of the users cursor.
    gui: A graphics object. Draw with this.
    '''
    x = (gui.mouse_x//5)+280
    y = (gui.mouse_y//5)+280
    gui.rectangle(x-500, y+250, 1000, 500, 'spring green3')
    x_grass = -2
    while x_grass < 710:
        gui.line(x_grass, y+220, x_grass, y+250, 'spring green3', 3)
        x_grass += 7
    gui.rectangle(x+130, y+200, 30, 100, 'saddle brown')
    gui.ellipse(x+145, y+175, 100, 150, 'forest green')
def bird_layer(gui, x_bird):
    '''
    This function animates birds flying across the screen continuously.
    gui: A graphics object. Draw with this
    x_bird: An integer that represents the x coordinate of the rightmost bird.
    '''
    i = 0
    y_bird = 200
    while i < 5:
        gui.line(x_bird-25, y_bird-10, x_bird, y_bird, 'black', 4)
        gui.line(x_bird+25, y_bird-10, x_bird, y_bird, 'black', 4)
        i += 1
        x_bird -= 100
        y_bird -= 25
def main():
    gui = graphics(700, 700, 'Motion Parallax')
    red1, green1, blue1 = gradient_color_value_randomizer()
    red2, green2, blue2 = gradient_color_value_randomizer()
    middle_color = random_color(gui)
    left_side_color = random_color(gui)
    right_side_color = random_color(gui)
    x_bird = 450
    while True:
        gui.clear()
        gradient_layer(gui, red1, green1, blue1, red2, green2, blue2)
        sun_layer(gui)
        middle_mountain_layer(gui, middle_color)
        side_mountains_layer(gui, left_side_color, right_side_color)
        foreground_layer(gui)
        bird_layer(gui, x_bird)
        x_bird +=2
        if x_bird > 1125:
            x_bird = -50
        gui.update_frame(60)

main()