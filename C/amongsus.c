#pragma pack(1)

#include <stdio.h>
#include <stdlib.h> 

#define BYTE unsigned char
#define DWORD unsigned int
#define FLOAT float

struct gho {
	BYTE unknown;
	BYTE maxP;
	DWORD chat;
	BYTE map;
	FLOAT speed;
	FLOAT cVision;
	FLOAT iVision;
	FLOAT cooldown;
	BYTE commonT;
	BYTE longT;
	BYTE shortT;
	DWORD meetings;
	BYTE impostors;
	BYTE distance;
	DWORD discussionT;
	DWORD votingT;
	BYTE settings;
	BYTE emergencyCooldown;
	BYTE confirmE;
	BYTE visualT;
} GHO;

void print(FILE * fp, struct gho GHO);
void choose(int i, FILE * fp, struct gho GHO);

int main() {
	FILE* fp = fopen("gameHostOptions", "rb");
	if (fp == NULL) {
		printf("File not found!!!1!!1!!1!1");
		return 1;
	} else {
		fread(&GHO, sizeof(struct gho), 1, fp);
		printf("What would you like to do? Read or modify?\n");
		printf("1)Read\n2)Modify\n");
		int chosen;
		scanf("%d", &chosen);
		choose(chosen, fp, GHO);
	}
	fclose(fp); 
	return 0;
}

void choose(int i, FILE * fp, struct gho GHO) {
	if (i == 1)
		print(fp, GHO);
	else if (i == 2)
		printf("Which option would you like to modify?\n");
		printf("0)Unkown byte (it doesn't matter what value you set it to, it will always go back to 3)\n");
		printf("1)Max Players (0-255, only integers)\n");
		printf("2)Room chat type\n");
		
	else {
		printf("Invalid choice! Must be 1 (read) or 2 (modify)\n");
		int newChosen;
		scanf("%d", &newChosen);
		choose(newChosen, fp, GHO);
	}
}

void print(FILE * fp, struct gho GHO) {
	printf("Useless and unkown byte: %02x\n", GHO.unknown);
	printf("Max players: %d\n", GHO.maxP);
	printf("Chat Type: %d\n", GHO.chat);
	printf("Map Type: %d\n", GHO.map);
	printf("Player Speed: %f\n", GHO.speed);
	printf("Crewmate Vision: %f\n", GHO.cVision);
	printf("Impostor Vision: %f\n", GHO.iVision);
	printf("Kill Cooldown: %f\n", GHO.cooldown);
	printf("Common Tasks: %d\n", GHO.commonT);
	printf("Long Tasks: %d\n", GHO.longT);
	printf("Short Tasks: %d\n", GHO.shortT);
	printf("Emergency Meetings: %d\n", GHO.meetings);
	printf("Impostors: %d\n", GHO.impostors);
	printf("Kill Distance: %d\n", GHO.distance);
	printf("Discusson Time: %d\n", GHO.discussionT);
	printf("Voting Time: %d\n", GHO.votingT);
	printf("Reccomended Settings: %d\n", GHO.settings);
	printf("Emergency Cooldown: %d\n", GHO.emergencyCooldown);
	printf("Confirm Ejects: %d\n", GHO.confirmE);
	printf("Visual Tasks: %d\n", GHO.visualT);
}


