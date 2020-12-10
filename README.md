Excercise 1: - Write a program to design Vending Machine using Java
====================

Scope:
---------
This example provides an implementation of the coin vending capabilities of a real-life vending machine. The project is maven based, using 'visual studio code'. Below you will find the key aspects, the assignment and how to execute.

Key aspects
---------

* Accepts coins of 1,5,10,25 Cents i.e. penny, nickel, dime, and quarter.
* Allow user to select products Coke(25), Pepsi(35), Soda(45)
* Allow user to take refund by cancelling the request.
* Return selected product and remaining change if any.
* Allow reset operation for vending machine supplier.

The assignment
---------
1. Vending machine contains products.
2. Products can be of different types (i.e. Coke(25), Pepsi(35), Soda(45))
3. Products are on shelves.
4. One shelve can contain only one type of product (but multiple products).
5. Each product type has its own price.
6. Machine has a display.
7. If we select shelve number, display should show product price.
8. You can buy products by putting money into machine. Machine accepts denominations 5, 2, 1, 0.5, 0.2, 0.1.
9. After inserting a coin, display shows amount that must be added to cover product price.
10. After selecting a shelve and inserting enough money we will get the product and the change (but machine has to have money to be able to return the change).
11. After selecting a shelve and inserting insufficient money to buy a product, user has to press "Cancel" to get their money back.
12. If machine does not have enough money to give the change it must show a warning message and return the money user has put, and it should not give the product.
13. Machine can return change using only money that was put into it (or by someone at start or by people who bought goods). Machine cannot create it's own money!

How to execute
---------

Project can be cloned from github and ran locally through terminal console using
command `mvn clean install`, note that, java jdk 1.8 and maven 3.6.3 was used.
