###
### Author: Saul Weintraub
### Course: CSc 110
### Description: This program will act as a contact manager. The user will be continually asked to
###              input commands that will cause the program to either show contacts, add a contact,
###              or exit. This program utilizes sets and tuples to store data and the only time a
###              list was used is when the program read in the contacts.txt file.
###
def contacts_loading():
    '''
    This function will open and read in the contacts from contacts.txt into a set of tuples, with
    each tuple containing the contact name, email, and phone number. The function will then close
    the contacts.txt file and return the set of tuples.
    '''
    contacts_file = open('contacts.txt', 'r')
    contacts_set = set()
    for line in contacts_file:
        line = line.strip('\n')
        line_list = line.split(' | ')
        contact_tuple = (line_list[0], line_list[1], line_list[2])
        contacts_set.add(contact_tuple)
    contacts_file.close()
    return contacts_set
def contact_search(contacts_set, command):
    '''
    This function will search through the existing contacts and find the contacts that contain
    the specified name, email, or phone number. The function will then print out the contacts that
    contain the specified name, email, or phone number.
    contacts_set: A set containing tuples, with each tuple representing one contact and containing a
    name, email, and phone number.
    command: A string containing the command inputted by the user. Used to determine the search term
    and search index.
    '''
    match_set = set()
    if 'show contacts with name' in command:
        search_term = command.replace('show contacts with name ', '')
        search_index = 0
    elif 'show contacts with email' in command:
        search_term = command.replace('show contacts with email ', '')
        search_index = 1
    elif 'show contacts with phone' in command:
        search_term = command.replace('show contacts with phone ', '')
        search_index = 2
    for contact_tuple in contacts_set:
        if search_term == contact_tuple[search_index]:
            match_set.add(contact_tuple)
    if len(match_set) == 0:
        print('None')
    else:
        for contact in sorted(match_set):
            print(contact[0] + "'s contact info:")
            print('  email: ' + contact[1])
            print('  phone: ' + contact[2])

def contact_add(contacts_set):
    '''
    This function will prompt the user for a name, email, and phone number. The function will then
    attempt to add a contact to the contacts set with the specified name, email, and phone number.
    If the contact already exists the function will print "Contact already exists!".
    contacts_set: A set containing tuples, with each tuple representing one contact and containing a
    name, email, and phone number.
    '''
    name = input('name:\n')
    email = input('email:\n')
    phone = input('phone:\n')
    new_contact_tuple = (name, email, phone)
    if new_contact_tuple in contacts_set:
        print('Contact already exists!')
    else:
        contacts_set.add(new_contact_tuple)
        print('Contact added!')

def main():
    print('Welcome to the contacts app!')
    contacts_set = contacts_loading()
    while True:
        command = input('>\n')
        if 'show contacts with' in command:
            contact_search(contacts_set, command)
        elif command == 'add contact':
            contact_add(contacts_set)
        elif command == 'exit':
            print('Goodbye!')
            break
        else:
            print('Huh?')

main()