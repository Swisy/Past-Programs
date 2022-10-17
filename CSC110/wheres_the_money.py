###
### Author: Saul Weintraub
### Description: This program will ask the user for financial information and will
###              then calculate annual spending, percents, and how much extra
###              money there is and will print this information in a table.
###
from os import _exit as exit
print('-----------------------------')
print('----- WHERE\'S THE MONEY -----')
print('-----------------------------')

##variables:
salary = input('What is your annual salary?\n')
if salary.isnumeric() != True:
    print('Must enter positive integer for salary.')
    exit(0)
salary = int(salary)

mortgage_or_rent = input('How much is your monthly mortgage or rent?\n')
if mortgage_or_rent.isnumeric() != True:
    print('Must enter positive integer for mortgage or rent.')
    exit(0)
mortgage_or_rent = int(mortgage_or_rent)

bills = input('What do you spend on bills monthly?\n')
if bills.isnumeric() != True:
    print('Must enter positive integer for bills.')
    exit(0)
bills = int(bills)

food = input('What are your weekly grocery/food expenses?\n')
if food.isnumeric() != True:
    print('Must enter positive integer for food.')
    exit(0)
food = int(food)

travel = input('How much do you spend on travel annually?\n')
if travel.isnumeric() != True:
    print('Must enter positive integer for travel.')
    exit(0)
travel= int(travel)

##calculations:
annual_mortgage_or_rent = mortgage_or_rent * 12
annual_bills = bills * 12
annual_food = food * 52

mortgage_or_rent_percent = (annual_mortgage_or_rent * 100)/salary
bills_percent = (annual_bills * 100)/salary
food_percent = (annual_food * 100)/salary
travel_percent = (travel * 100)/salary

#tax
if salary > 200000:
    tax_percent = 30.0
elif salary > 75000:
    tax_percent = 25.0
elif salary > 15000:
    tax_percent = 20.0
else:
    tax_percent = 10.0
annual_tax = salary * ( tax_percent / 100.0)
if annual_tax > 50000:
    annual_tax = 50000
    tax_percent = 5000000/salary

#extra money and max percent
annual_extra = (salary - annual_mortgage_or_rent - annual_bills - annual_food - travel - annual_tax)
extra_percent = 100 * annual_extra/salary

max_percent = int(max(mortgage_or_rent_percent, bills_percent, food_percent, tax_percent, extra_percent))

## table print
print()
print('-'*42 + '-'*max_percent)
print('See the financial breakdown below, based on a salary of $'+str(salary))
print('-'*42 + '-'*max_percent)

print('| mortgage/rent | $'+format(annual_mortgage_or_rent, '11,.2f')+' |'
+format(mortgage_or_rent_percent, '6,.1f')+'% | '
+'#'*int(mortgage_or_rent_percent))

print('|         bills | $'+format(annual_bills, '11,.2f')+' |'
+format(bills_percent, '6,.1f')+'% | '+'#'*int(bills_percent))

print('|          food | $'+format(annual_food, '11,.2f')+' |'
+format(food_percent, '6,.1f')+'% | '+'#'*int(food_percent))

print('|        travel | $'+format(travel, '11,.2f')+' |'
+format(travel_percent, '6,.1f')+'% | '+'#'*int(travel_percent))

print('|           tax | $'+format(annual_tax, '11,.2f')+' |'
+format(tax_percent, '6,.1f')+'% | '+'#'*int(tax_percent))

print('|         extra | $'+format(annual_extra, '11,.2f')+' |'
+format(extra_percent, '6,.1f')+'% | '+'#'*int(extra_percent))

print('-'*42 + '-'*max_percent)

if annual_tax == 50000:
    print('>>> TAX LIMIT REACHED <<<')
if annual_extra < 0:
    print('>>> WARNING: DEFICIT <<<')