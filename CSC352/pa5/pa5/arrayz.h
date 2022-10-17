/* Name: Saul Weintraub
 * Course: CSC352
 *
 * This file includes function declaration that will be implemented in arrayz.c and 
 * will be used by basketball.c
 *
 */


/*
 * This function will return the sum of every element in the array values[].
 *
 * long values[]: An array of long values
 *
 * returns: The sum of every value in the array
 */
long sum(long values[]);

/*
 * This function will find the minimum value in the array values[] and return
 * the index of that value.
 *
 * long values: An array of long values
 *
 * returns: The Index of the minimum value in the array
 */
long minil(long values[]);

/*
 * This function will find the minimum value in the array values[] and return
 * the index of that value.
 *
 * double values: An array of double values
 *
 * returns: The Index of the minimum value in the array
 */
long minid(double values[]);

/*
 * This function will find the maximum value in the array values[] and return
 * the index of that value.
 *
 * long values: An array of long values
 *
 * returns: The Index of the maximum value in the array
 */
long maxil(long values[]);

/*
 * This function will find the maximum value in the array values[] and return
 * the index of that value.
 *
 * double values: An array of double values
 *
 * returns: The Index of the maximum value in the array
 */
long maxid(double values[]);

/*
 * This function will print the elements in the array values.
 *
 * Format: length ARRAY_LENGTH array: EL1, EL2, ..., ELN
 *
 * long values: An array of long values
 */
void printal(long values[]);

/*
 * This function will print the elements in the array values.
 *
 * Format: length ARRAY_LENGTH array: EL1, EL2, ..., ELN
 *
 * double values: An array of long values
 */

void printad(double values[]);

/*
 * This function will calculate and return the mean of the elements in the 
 * array values.
 *
 * long values: An array of long values
 *
 * returns: The average of every element in the array
 */
double mean(long values[]);

/*
 * This function will calculate and return the standard deviation of the
 * elements in the array values/
 *
 * long values: And array of long values
 *
 * returns: The standard deviation of the values in the array
 */
double sdev(long values[]);
