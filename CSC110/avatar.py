###
### Author: Saul Weintraub
### Course: CSc 110
### Description: This program will print out different avatars. The user can choose between
###              3 pre defined avatars or create their own avatar. The customizable features
###              include hat style, eye character, hair, arm style, torso length, leg length,
###              and shoe style.
###
def print_hat(direction):
    '''
    This function will print out a hat of 4 different styles depending on the direction entered.
    The four styles are right, left, both, and front. If the entered direction is not any of
    the four styles then the hat printed out will default to the front style.
    direction: Can be any string
    '''
    print('       ~-~\n     /-~-~-\\')
    if direction == 'left' or direction == 'both':
        left_of_hat = ' ___'
    else:
        left_of_hat = '    '
    if direction == 'right' or direction == 'both':
        right_of_hat = '___'
    else:
        right_of_hat = '   '
    print(left_of_hat+'/_______\\'+right_of_hat)

def print_face(hair, eyes):
    '''
    This function will print out a face with a custom eye character and one of 2 hair styles.
    hair: Can be the string 'True' or the string 'False'.
    eyes: Can be any single character.
    '''
    if hair == 'True':
        print('    |"""""""|')
    elif hair == 'False':
        print("    |'''''''|")
    print('    | '+eyes+'   '+eyes+' |')
    print('    |   V   |\n    |  ~~~  |\n     \\_____/')

def print_arms(style):
    '''
    This function will print out 1 line of arms.
    style: Can be any one character string.
    '''
    print(' 0'+style+style+style+style+'|---|'+style+style+style+style+'0')

def print_torso(height):
    '''
    This function will print out a torso of varying heights.
    height: Can be any positive integer.
    '''
    index = 0
    while index < height:
        print('      |-X-|')
        index += 1

def print_legs_and_shoe(height, shoe):
    '''
    This function will print out legs of varying lengths and a custom shoe.
    height: Can be any integer between 1 and 4.
    shoe: Can be any 5 character long string.
    '''
    print('      HHHHH')
    side_space = 5
    middle_space = ' '
    index = 0
    while index < height:
        space_counter = 0
        while space_counter < side_space:
            print(' ', end ='')
            space_counter += 1
        print('///'+middle_space+'\\\\\\')
        index += 1
        side_space -= 1
        middle_space += '  '
    print(shoe+'       '+shoe)

def print_jeff():
    '''
    This function will print the Jeff avatar defined in the assignment details.
    '''
    print()
    print_hat('both')
    print_face('True', '0')
    print_arms('=')
    print_torso(2)
    print_legs_and_shoe(2, '#HHH#')

def print_adam():
    '''
    This function will print the Adam avatar defined in the assignment details.
    '''
    print()
    print_hat('right')
    print_face('False', '*')
    print_torso(1)
    print_arms('T')
    print_torso(3)
    print_legs_and_shoe(3, '<|||>')

def print_chris():
    '''
    This function will print the Chris avatar defined in the assignment details.
    '''
    print()
    print_hat('front')
    print_face('True', 'U')
    print_torso(1)
    print_arms('W')
    print_torso(1)
    print_legs_and_shoe(4, '<>-<>')

def print_custom():
    '''
    This function will ask the user different customization options and will then print out an
    avatar with the user's selected features.
    '''
    print('Answer the following questions to create a custom avatar')
    hat_style = input('Hat style ?\n')
    eye_character = input('Character for eyes ?\n')
    hair = input('Shaggy hair (True/False) ?\n')
    arm_character = input('Arm style ?\n')
    torso_length = int(input('Torso length ?\n'))
    leg_length = int(input('Leg length (1-4) ?\n'))
    shoes = input('Shoe look ?\n')
    print()
    print_hat(hat_style)
    print_face(hair, eye_character)
    print_arms(arm_character)
    print_torso(torso_length)
    print_legs_and_shoe(leg_length, shoes)

def main():
    print('----- AVATAR -----')
    correct_input = 'False'
    while correct_input != 'True':
        avatar = input('Select an Avatar or create your own:\n')
        if (avatar == 'custom' or avatar == 'exit' or avatar == 'Jeff'
            or avatar == 'Adam' or avatar == 'Chris'):
            correct_input = 'True'
    if avatar == 'Jeff':
        print_jeff()
    elif avatar == 'Adam':
        print_adam()
    elif avatar == 'Chris':
        print_chris()
    elif avatar == 'custom':
        print_custom()

main()