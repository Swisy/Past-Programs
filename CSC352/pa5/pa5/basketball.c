/* Name: Saul Weintraub
 * Course: CSC352
 *
 * This file will take the name of a file as a command line argument and will calculate
 * and print out the most consistent scorer, rebounder, and assister, the least
 * consistent scorer, rebounder, and assister, the best scorer, rebounder, and assister,
 * and finally the worst scorer, rebounder, and assister according to the basketball
 * information in the file.
 *
 */

#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <string.h>
#include "arrayz.h"

int read_file(char file[], char players[100][50], long scores[100][101],
	       	long rebounds[100][101], long assists[100][101]){
	FILE* data_file;
       	char line[1050];
	data_file = fopen(file, "r");

	int player_num = 0;

	while (fgets(line, 1049, data_file) != NULL){
		int game = 1;
		int i = 0;
		char name[50];
		char num[3];
		int num_index = 0;
		int name_index = 0;
		bool get_name = true;
		bool get_score = false;
		bool get_rebound = false;
		bool get_assists = false;
		while(line[i] != '\0'){
			if(get_name){
				if(line[i] == '['){
					strcpy(players[player_num], name);
					get_name = false;
					get_score = true;
				} else{
					name[name_index] = line[i];
					name[name_index + 1] = '\0';
					name_index = name_index + 1;
				}
			} else if(get_score){
				if(line[i] == ','){
					scores[player_num][game] = (long) atoi(num);
					num_index = 0;
					get_score = false;
					get_rebound = true;
				} else if(line[i] != '['){
					num[num_index] = line[i];
					num[num_index + 1] = '\0';
					num_index = num_index + 1;
				}
			} else if(get_rebound){
				if(line[i] == ','){
					rebounds[player_num][game] = (long) atoi(num);
					num_index = 0;
					get_rebound = false;
					get_assists = true;
				} else{
					num[num_index] = line[i];
					num[num_index + 1] = '\0';
					num_index = num_index + 1;
				}
			} else if(get_assists){
				if(line[i] == ']'){
					assists[player_num][game] = (long) atoi(num);
					num_index = 0;
					get_assists = false;
					get_score = true;
					game = game + 1;
				} else{
					num[num_index] = line[i];
					num[num_index + 1] = '\0';
					num_index = num_index + 1;
				}
			}
			i = i + 1;	
		}
		scores[player_num][0] = (long) game - 1;
		rebounds[player_num][0] = (long) game - 1;
		assists[player_num][0] = (long) game - 1;
		player_num = player_num + 1;
	}
	fclose(data_file);
	return player_num;
}

void print_data(int player_count, char players[100][50], long scores[100][101], 
		long rebounds[100][101], long assists[100][101]){
	double stdev_scores[player_count + 1];
	stdev_scores[0] = player_count;
	double stdev_rebounds[player_count + 1];
	stdev_rebounds[0] = player_count;
	double stdev_assists[player_count + 1];
	stdev_assists[0] = player_count;
	double average_scores[player_count + 1];
	average_scores[0] = player_count;
	double average_rebounds[player_count + 1];
	average_rebounds[0] = player_count;
	double average_assists[player_count + 1];
	average_assists[0] = player_count;
	
	for(int i = 0; i < player_count; i++){
		stdev_scores[i+1] = sdev(scores[i]);
		stdev_rebounds[i+1] = sdev(rebounds[i]);
		stdev_assists[i+1] = sdev(assists[i]);
		average_scores[i+1] = mean(scores[i]);
		average_rebounds[i+1] = mean(rebounds[i]);
		average_assists[i+1] = mean(assists[i]);
	}

	int con_scorer = (int) minid(stdev_scores) - 1;
	int inc_scorer = (int) maxid(stdev_scores) - 1;
	int con_rebounder = (int) minid(stdev_rebounds) - 1;
	int inc_rebounder = (int) maxid(stdev_rebounds) - 1;
	int con_assister = (int) minid(stdev_assists) - 1;
	int inc_assister = (int) maxid(stdev_assists) - 1;

	int best_scorer = (int) maxid(average_scores) - 1;
	int worst_scorer = (int) minid(average_scores) - 1;
	int best_rebounder = (int) maxid(average_rebounds) - 1;
	int worst_rebounder = (int) minid(average_rebounds) - 1;
	int best_assister = (int) maxid(average_assists) - 1;
	int worst_assister = (int) minid(average_assists) - 1;

	printf("most consistent scorer: %s\n", players[con_scorer]);
	printf("most inconsistent scorer: %s\n", players[inc_scorer]);
	printf("highest scorer: %s\n", players[best_scorer]);
	printf("lowest scorer: %s\n", players[worst_scorer]);

	printf("most inconsistent rebounder: %s\n", players[inc_rebounder]);
	printf("most consistent rebounder: %s\n", players[con_rebounder]);
	printf("highest rebounder: %s\n", players[best_rebounder]);
	printf("lowest rebounder: %s\n", players[worst_rebounder]);

	printf("most inconsistent assister: %s\n", players[inc_assister]);
	printf("most consistent assister: %s\n", players[con_assister]);
	printf("lowest assister: %s\n", players[worst_assister]);
	printf("highest assister: %s\n", players[best_assister]);
}


int main(int argc, char * argv[]){
	if(argc != 2){
		fprintf(stderr, "expects 1 command line argument\n");
		return -1;
	}
	
	char players[100][50];
	long scores[100][101];
	long rebounds[100][101];
	long assists[100][101];
	
	int player_count = read_file(argv[1], players, scores, rebounds, assists);
	
	print_data(player_count, players, scores, rebounds, assists);

	return 0;
}
