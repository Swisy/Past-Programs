###
### Author: Saul Weintraub
### Course: CSc 110
### Description:
###
###
###
def main():
    encrypted_file_name = input('Enter the name of an encrypted text file:\n')
    index_file_name = input('Enter the name of the encryption index file:\n')
    encrypted_file = open(encrypted_file_name, 'r')
    index_file = open(index_file_name, 'r')
    encrypted_list = encrypted_file.readlines()
    index_list = index_file.readlines()
    decrypted_file = open('decrypted.txt', 'w')
    decrypted_list = []
    i = 1
    while i <= len(index_list):
        index = index_list.index(str(i) + '\n')
        decrypted_list.append(encrypted_list[index])
        i += 1
    for line in decrypted_list:
        decrypted_file.write(line)
    decrypted_file.close()
    index_file.close()
    encrypted_file.close()
main()