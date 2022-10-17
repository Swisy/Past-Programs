###
### Author: Saul Weintraub
### Course: CSc 110
### Description: This program will combine 2 ppm images. An image with a red, blue, or green screen
###              and an image that will act as the backround. The program will prompt the user for
###              the color channel, the color channel difference, the names of the 2 images, and the
###              name you want the combined image to be.
###
from os import _exit as exit

def get_image_dimensions_string(file_name):
    '''
    Given the file name for a valid PPM file, this function will return the
    image dimensions as a string. For example, if the image stored in the
    file is 150 pixels wide and 100 pixels tall, this function should return
    the string '150 100'.
    file_name: A string. A PPM file name.
    '''
    image_file = open(file_name, 'r')
    image_file.readline()
    return image_file.readline().strip('\n')

def load_image_pixels(file_name):
    ''' Load the pixels from the image saved in the file named file_name.
    The pixels will be stored in a 3d list, and the 3d list will be returned.
    Each list in the outer-most list are the rows of pixels.
    Each list within each row represents and individual pixel.
    Each pixels is representd by a list of three ints, which are the RGB values of that pixel.
    '''
    pixels = []
    image_file = open(file_name, 'r')

    image_file.readline()
    image_file.readline()
    image_file.readline()

    width_height = get_image_dimensions_string(file_name)
    width_height = width_height.split(' ')
    width = int(width_height[0])
    height = int(width_height[1])

    for line in image_file:
        line = line.strip('\n ')
        rgb_row = line.split(' ')
        row = []
        for i in range(0, len(rgb_row), 3):
            pixel = [int(rgb_row[i]), int(rgb_row[i+1]), int(rgb_row[i+2])]
            row.append(pixel)
        pixels.append(row)

    return pixels

def greenscreen_algorithm(channel, channel_difference, gs_pixels, fi_pixels):
    '''
    This function will analyze each pixel of the greenscreen image and will determine if the
    combined image will use the pixel from the greenscreen image or the pixel from the fill image.
    If the color channel value of the greenscreen image pixel is greater than the color channel
    difference times the other 2 rgb values, then the pixel from the fill image will be used,
    otherwise the pixel from the greenscreen image will be used. The function will organize the
    chosen pixels into a 3 dimensional list and will then return that list.
    channel: A string that represents the color of the screen. 'r' for red, 'g' for green, and 'b'
     for blue.
    channel_difference: A float value between 1.0 and 10.0. Determines how prevelant the screen has
     to be in order to be replaced.
    gs_pixels: A 3 dimensional list containing the pixels of the greenscreen image.
    fi_pixels: A 3 dimensional list containing the pixels of the fill image.
    The format for the 3 dimensional lists is explained on lines 25-28.
    '''
    out_pixels = []
    row_index = 0
    pixel_index = 0
    if channel == 'r':
        rgb_index = 0
    elif channel == 'g':
        rgb_index = 1
    else:
        rgb_index = 2
    for row in gs_pixels:
        out_pixels.append([])
        for pixel in row:
            if int(pixel[rgb_index]) > channel_difference * int(pixel[rgb_index-1]) \
              and int(pixel[rgb_index]) > channel_difference * int(pixel[rgb_index-2]):
                out_pixels[row_index].append(fi_pixels[row_index][pixel_index])
            else:
                out_pixels[row_index].append(pixel)
            pixel_index += 1
        row_index += 1
        pixel_index = 0
    return out_pixels

def out_file_writer(out_pixels, out_file, gs_dimensions):
    '''
    This function will write the output ppm file.
    out_pixels: A 3 dimensional list containing the pixels of the output file.
    out_file: A string containing the name of the output file.
    gs_dimensions: A string containing the dimensions of the greenscreen file which has the same
     dimensions as the output file.
    '''
    out_ppm_file = open(out_file, 'w')
    out_ppm_file.write('P3\n')
    out_ppm_file.write(gs_dimensions + '\n')
    out_ppm_file.write('255\n')
    for row in out_pixels:
        for pixel in row:
            for rgb in pixel:
                out_ppm_file.write(str(rgb) + ' ')
        out_ppm_file.write('\n')
    out_ppm_file.close()
    print('Output file written. Exiting.')
def main():
    channel = input('Enter color channel\n')
    if channel not in ['r', 'g', 'b']:
        print('Channel must be r, g, or b. Will exit.')
        exit(0)
    channel_difference = float(input('Enter color channel difference\n'))
    if channel_difference < 1.0 or channel_difference > 10.0:
        print('Invalid channel difference. Will exit.')
        exit(0)
    gs_file = input('Enter greenscreen image file name\n')
    gs_dimensions = get_image_dimensions_string(gs_file)
    fi_file = input('Enter fill image file name\n')
    if gs_dimensions != get_image_dimensions_string(fi_file):
        print('Images not the same size. Will exit.')
        exit(0)
    out_file = input('Enter output file name\n')

    gs_pixels = load_image_pixels(gs_file)
    fi_pixels = load_image_pixels(fi_file)
    out_pixels = greenscreen_algorithm(channel, channel_difference, gs_pixels, fi_pixels)
    out_file_writer(out_pixels, out_file, gs_dimensions)

main()
