###
### Author: Saul Weintraub
### Course: CSc 110
### Description: This program will run a game of 1D chess. This program will utilize
###              lists to store the positions of the game pieces. There will be 2 players.
###              Each player has 1 king and 2 knights. The goal of the game is to get rid of
###              the opposing player's king. The game ends when 1 of the kings is defeated.

from graphics import graphics

W_KNIGHT = 'WKn'
W_KING   = 'WKi'
B_KNIGHT = 'BKn'
B_KING   = 'BKi'
EMPTY    = '   '
WHITE    = 'White'
BLACK    = 'Black'
LEFT     = 'l'
RIGHT    = 'r'

def is_valid_move(board, position, player):
    '''
    This function will determine if the desired move is valid.
    It will check if the piece the player wants to move belongs to the player.
    If it is a valid move the function will return True, else it will return False.
    board: A list containing the location of the pieces.
    position: The index value of the piece trying to be moved. An int between 0 and 8.
    player: The player whose turn it is. Either Black or White.
    '''
    if position >= 0 and position <= 8:
        if player == 'White':
            if board[position] == 'WKn' or board[position] == 'WKi':
                return True
            else:
                return False
        else:
            if board[position] == 'BKn' or board[position] == 'BKi':
                return True
            else:
                return False
    else:
        return False

def move_knight(board, position, direction):
    '''
    This function will change the position of a Knight piece.
    board: A list containing the location of the pieces.
    position: The index value of the piece trying to be moved. An int between 0 and 8.
    direction: Either 'r' or 'l'. Determines if the piece will move left or right.
    '''
    if direction == 'r':
        if position + 2 <=8:
            board[position + 2] = board[position]
            board[position] = '   '
    else:
        if position - 2 >= 0:
            board[position - 2] = board[position]
            board[position] = '   '

def move_king(board, position, direction):
    '''
    This function will change the position of a King piece.
    board: A list containing the location of the pieces.
    position: The index value of the piece trying to be moved. An int between 0 and 8.
    direction: Either 'r' or 'l'. Determines if the piece will move left or right.
    '''
    i = 1
    if direction == 'r':
        while board[position + i] == '   ' and position + i <8:
            i += 1
        if position + i <9:
            board[position + i] = board[position]
            board[position] = '   '
    else:
        while board[position - i] == '   ' and position - i > 0:
            i += 1
        if position - i > -1:
            board[position - i] = board[position]
            board[position] = '   '

def print_board(board):
    '''
    This function will print out the game board with the positions of the pieces.
    board: A list containing the order of the game pieces.
    '''
    print('+' + '-' * 53 + '+')
    print('| ', end = '')
    i=0
    while i < 8:
        print(board[i] + ' | ', end = '')
        i+=1
    print(board[8] +' |')
    print('+' + '-' * 53 + '+')

def draw_board(board, gui):
    '''
    This function will draw the game board with the current positions of the pieces on the board.
    board: A list containing the order of the game pieces.
    gui: A variable used for drawing in the graphics module.
    '''
    gui.rectangle(0, 0, 700, 200, 'white')
    gui.rectangle(35, 100, 630, 70, 'black')
    gui.rectangle(36, 101, 628, 68, gui.get_color_string(180, 62, 59))
    x= 106
    while x < 597:
        gui.line(x, 100, x, 170, 'black', 1)
        x+= 70
    gui.text(200, 35, '1 Dimensional Chess', 'dark green', 23)
    x = 40
    i = 0
    while i < 9:
        if board[i] == 'WKn':
            gui.text(x, 125, 'Knight', 'white', 15)
        elif board[i] == 'WKi':
            gui.text(x, 125, 'King', 'white', 15)
        elif board[i] == 'BKn':
            gui.text(x, 125, 'Knight', 'black', 15)
        elif board[i] == 'BKi':
            gui.text(x, 125, 'King', 'black', 15)
        x+=70
        i+=1
    gui.update_frame(40)

def is_game_over(board):
    '''
    This function will determine if the game is over by testing what kings are present.
    If the white king is not present then the game is over and Black wins.
    If the black king is not present then the game is over and White wins.
    If one of the kings is missing then the function will return True.
    board: A list containing the position of the game pieces.
    '''
    if 'WKi' not in board:
        print_board(board)
        print('Black wins!')
        return True
    if 'BKi' not in board:
        print_board(board)
        print('White wins!')
        return True

def move(board, position, direction):
    '''
    This function determines the piece trying to be moved and calls upon the corresponding function.
    If the piece is a white or black king then the move_king function is called.
    If the piece is a white or black knight then the move_knight function is called.
    board: A list containing the position of the pieces in the game.
    position: The index value of the piece trying to be moved. An int value between 0-8
    direction: Either 'l' or 'r'. Determines if the piece will move to the left or right.
    '''
    if board[position] == 'WKn' or board[position] == 'BKn':
        move_knight(board, position, direction)
    else:
        move_king(board, position, direction)

def main():

    # Create the canvas
    gui = graphics(700, 200, '1 Dimensional Chess')

    # This is the starting board.
    # This board variable can and should be passed to other functions
    # and changed as moves are made.
    board = [W_KING, W_KNIGHT, W_KNIGHT, EMPTY, EMPTY, EMPTY, B_KNIGHT, B_KNIGHT, B_KING]

    # White typically starts in chess.
    # This will change between WHITE and BLACK as the turns progress.
    player = WHITE

    # This variable will be updated to be True if the game is over.
    # The game is over after one of the kings dies.
    is_game_won = False

    # This loop controls the repetitive nature of the turns of the game.
    while not is_game_won:

        print_board(board)

        # Draw the board
        draw_board(board, gui)

        position = int(input(player + ' enter index:\n'))
        direction = input(player + ' enter direction (l or r):\n')

        # If the desired move is valid, then call the move function.
        # Also, change the player variable.
        if is_valid_move(board, position, player):
            if player == WHITE:
                move(board, position, direction)
                player = BLACK
            else:
                move(board, position, direction)
                player = WHITE
            is_game_won = is_game_over(board)

main()