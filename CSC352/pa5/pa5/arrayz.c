/* Name: Saul Weintraub
 * Course: csc352
 *
 * This file implements the functions described in arrayz.h
 *
 */

#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include "arrayz.h"

long sum(long values[]){
	long len = values[0];
	long total = 0;

	for(long i = 1; i < len + 1; i++){
		total = total + values[i];
	}

	return total;
}

long minil(long values[]){
	long len = values[0];
	long min = values[1];
	long index = 1;

	for(long i = 1; i < len + 1; i++){
		if((min > values[i]) && (min != values[i])){
			min = values[i];
			index = i;
		}
	}

	return index;
}

long minid(double values[]){
	long len = (long) values[0];
	double min = values[1];
	long index = 1;

	for(long i = 1; i < len + 1; i++){
		if((min > values[i]) && (min != values[i])){
			min = values[i];
			index = i;
		}
	}

	return index;
}

long maxil(long values[]){
	long len = values[0];
	long max = values[1];
	long index = 1;

	for(long i = 1; i < len + 1; i++){
		if((max < values[i]) && (max != values[i])){
			max = values[i];
			index = i;
		}
	}

	return index;
}

long maxid(double values[]){
	long len = (long) values[0];
	double max = values[1];
	long index = 1;

	for(long i = 1; i < len + 1; i++){
		if((max < values[i]) && (max != values[i])){
			max = values[i];
			index = i;
		}
	}

	return index;
}

void printal(long values[]){
	long len = values[0];

	printf("length %ld array: ", len);

	for(long i = 1; i < len; i++){
		printf("%ld, ", values[i]);
	}

	printf("%ld", values[len]);

	printf("\n");
}

void printad(double values[]){
	long len = (long) values[0];

	printf("length %ld array: ", len);

	for(long i = 1; i < len; i++){
		printf("%lf, ", values[i]);
	}

	printf("%lf", values[len]);

	printf("\n");
}

double mean(long values[]){
	long len = values[0];
	long total = sum(values);
	return ((double) total / len);
}

double sdev(long values[]){
	long len = values[0];
	double average = mean(values);
	double squareTotal = 0;

	for(long i = 1; i < len + 1; i++){
		double subtract = values[i] - average;
		squareTotal = squareTotal + (subtract * subtract);
	}

	double squareMean = squareTotal / len;
	
	return sqrt(squareMean);
}
