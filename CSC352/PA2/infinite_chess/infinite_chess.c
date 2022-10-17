#include <stdio.h>
#include <stdlib.h>



int main() {
	char piece;
	int moves, possible;
	
	printf("Enter piece type (k, b, p):\n");
	scanf("%c", &piece);

	printf("Enter number of moves:\n");
	scanf("%d", &moves);

	if(piece == 'p'){
		possible = moves + 1;
	} else if(piece == 'k'){
		possible = (2 * moves + 1);
		possible = possible * possible;
	} else if(piece == 'b'){
		int a = moves;
		int b = moves + 1;
		a = a * a;
		b = b * b;
		possible = a + b;
	}

	printf("possible locations: %d\n", possible);

	return 0;
}
