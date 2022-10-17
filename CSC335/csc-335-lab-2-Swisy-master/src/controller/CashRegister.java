package controller;

import java.util.Scanner;

import model.LoyaltyProgram;
import model.Menu;
import model.PunchCard;

public class CashRegister {

	public static void main(String[] args) {
		Menu menu = new Menu();
		LoyaltyProgram loyalty = new LoyaltyProgram();

		Scanner input = new Scanner(System.in);

		do {

			System.out.println("Welcome to NickDonalds, what's your name? ");

			String customerName = input.next();

			System.out.print(menu);

			PunchCard userCard = loyalty.getPunchCard(customerName);

			// add visit to punch card
			userCard.addVisit();

			System.out.print("What would you like to order? ");
			String userInput = input.next();

			// keeps track of the running total of orders for this transaction
			double currentTab = 0;

			while (!userInput.equalsIgnoreCase("no")) {

				try {
					System.out.println(menu.getQuip(userInput));
					currentTab += menu.getPrice(userInput);
				} catch (NullPointerException npe) {
					System.out.println("Item not on menu. Check spelling and capitalization.");
				}

				System.out.print("Anything else? (enter item name or \"no\" to ring out) ");
				userInput = input.next();
			}

			// check if it's this customer's Nth visit, and apply discount if it is
			if(userCard.isNthVisit(loyalty.VISITS_FOR_DISCOUNT)) {
				currentTab = currentTab - (currentTab * loyalty.DISCOUNT);
			}

			System.out.printf("Cool, that will be $%.2f, take care!\n", currentTab);

			System.out.println("Enter \"exit\" to close down the store or anything else for the next customer.");
		} while (!input.next().toLowerCase().equals("exit"));
		input.close();
	}
}
