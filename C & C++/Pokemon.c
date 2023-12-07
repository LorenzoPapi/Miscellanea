#include <stdio.h>
#include <string.h>

void calcHP(char name[], int lvl) {
	int HPBase, HPIV, HPEV;
	int totHP;
	
	printf("Immettere HP Base del Pokemon");
	scanf("%d", &HPBase);
	printf("Immettere Punti Individuali HP del Pokemon (crearli con un randomizer)\n");
	scanf("%d", &HPIV);
	printf("Immettere Punti Base HP del Pokemon\n");
	scanf("%d", &HPEV);
	totHP = ((2 * HPBase + HPIV + (HPEV / 4)) * lvl)/100 + lvl + 10;
	printf("Il Pokemon %s, al livello %d ha %d HP\n\n", name, lvl, totHP);
}

void calcAtk(char name[], int lvl, float atkMulti) {
	int atkBase, atkIV, atkEV;
	int totAtk;
	
	printf("Immettere Attacco Base\n");
	scanf("%d", &atkBase);
	printf("Immettere Punti Individuali Attacco del Pokemon\n");
	scanf("%d", &atkIV);
	printf("Immettere Punti Base Attacco del Pokemon\n");
	scanf("%d", &atkEV);
	totAtk = (((2 * atkBase + atkIV + (atkEV / 4)) * lvl)/100 + 5) * atkMulti;
	printf("Il Pokemon %s, al livello %d ha %d di Attacco\n\n", name, lvl, totAtk);
}

void calcDef(char name[], int lvl, float defMulti) {
	int defBase, defIV, defEV;
	int totDef;
	
	printf("Immettere Difesa Base\n");
	scanf("%d", &defBase);
	printf("Immettere Punti Individuali Difesa del Pokemon\n");
	scanf("%d", &defIV);
	printf("Immettere Punti Base Difesa del Pokemon\n");
	scanf("%d", &defEV);
	totDef = (((2 * defBase + defIV + (defEV / 4)) * lvl)/100 + 5) * defMulti;
	printf("Il Pokemon %s, al livello %d ha %d di Difesa\n\n", name, lvl, totDef);
}

void calcSpeed(char name[], int lvl, float speedMulti) {
	int speedBase, speedIV, speedEV;
	int totSpeed;
	
	printf("Immettere Velocita' Base\n");
	scanf("%d", &speedBase);
	printf("Immettere Punti Individuali Velocita' del Pokemon\n");
	scanf("%d", &speedIV);
	printf("Immettere Punti Base Velocita' del Pokemon\n");
	scanf("%d", &speedEV);
	totSpeed = (((2 * speedBase + speedIV + (speedEV / 4)) * lvl)/100 + 5) * speedMulti;
	printf("Il Pokemon %s, al livello %d ha %d di Velocita'\n\n", name, lvl, totSpeed);
}

void calcSpAtk(char name[], int lvl, float spAtkMulti) {
	int spAtkBase, spAtkIV, spAtkEV;
	int totSpAtk;
	
	printf("Immettere Attacco Speciale Base\n");
	scanf("%d", &spAtkBase);
	printf("Immettere Punti Individuali Attacco Speciale del Pokemon\n");
	scanf("%d", &spAtkIV);
	printf("Immettere Punti Base Attacco Speciale del Pokemon\n");
	scanf("%d", &spAtkEV);
	totSpAtk = (((2 * spAtkBase + spAtkIV + (spAtkEV / 4)) * lvl)/100 + 5) * spAtkMulti;
	printf("Il Pokemon %s, al livello %d ha %d di Attacco Speciale\n\n", name, lvl, totSpAtk);
}

void calcSpDef(char name[], int lvl, float spDefMulti) {
	int spDefBase, spDefIV, spDefEV;
	int totSpDef;
	
	printf("Immettere Difesa Speciale Base\n");
	scanf("%d", &spDefBase);
	printf("Immettere Punti Individuali Difesa Speciale del Pokemon\n");
	scanf("%d", &spDefIV);
	printf("Immettere Punti Base Difesa Speciale del Pokemon\n");
	scanf("%d", &spDefEV);
	totSpDef = (((2 * spDefBase + spDefIV + (spDefEV / 4)) * lvl)/100 + 5) * spDefMulti;
	printf("Il Pokemon %s, al livello %d ha %d di Difesa Speciale\n\n", name, lvl, totSpDef);
}

main() {
	int lvl;
	char name[30];
	char nature[15];
	float atkMulti = 1;
	float spAtkMulti = 1;
	float defMulti = 1;
	float spDefMulti = 1;
	float speedMulti = 1;
	
	printf("Immettere nome del Pokemon\n");
	scanf("%s", &name);
	printf("Immettere Livello del Pokemon\n");
	scanf("%d", &lvl);
	printf("Immettere Natura del Pokemon\n");
	scanf("%s", &nature);
	
	if (strcasecmp(nature[15], "Schiva") || strcasecmp(nature[15], "Audace") || strcasecmp(nature[15], "Decisa" || strcasecmp(nature[15], "Birbona"))) {
		atkMulti = 1.1;
		if (strcasecmp(nature[15], "Schiva")) {
			defMulti = 0.9;
		} else if (strcasecmp(nature[15], "Audace")) {
			speedMulti = 0.9;
		} else if (strcasecmp(nature[15], "Decisa")) {
			spAtkMulti = 0.9;
		} else if (strcasecmp(nature[15], "Birbona")) {
			spDefMulti = 0.9;
		}
	} else if (strcasecmp(nature[15], "Sicura") || strcasecmp(nature[15], "Placida") || strcasecmp(nature[15], "Scaltra" || strcasecmp(nature[15], "Fiacca"))) {
		defMulti = 1.1;
		if (strcasecmp(nature[15], "Sicura")) {
			atkMulti = 0.9;
		} else if (strcasecmp(nature[15], "Placida")) {
			speedMulti = 0.9;
		} else if (strcasecmp(nature[15], "Scaltra")) {
			spAtkMulti = 0.9;
		} else if (strcasecmp(nature[15], "Fiacca")) {
			spDefMulti = 0.9;
		}
	} else if (strcasecmp(nature[15], "Timida") || strcasecmp(nature[15], "Lesta") || strcasecmp(nature[15], "Allegra" || strcasecmp(nature[15], "Ingenua"))) {
		speedMulti = 1.1;
		if (strcasecmp(nature[15], "Timida")) {
			atkMulti = 0.9;
		} else if (strcasecmp(nature[15], "Lesta")) {
			defMulti = 0.9;
		} else if (strcasecmp(nature[15], "Allegra")) {
			spAtkMulti = 0.9;
		} else if (strcasecmp(nature[15], "Ingenua")) {
			spDefMulti = 0.9;
		}
	} else if (strcasecmp(nature[15], "Modesta") || strcasecmp(nature[15], "Mite") || strcasecmp(nature[15], "Quieta" || strcasecmp(nature[15], "Ardente"))) {
		spAtkMulti = 1.1;
		if (strcasecmp(nature[15], "Modesta")) {
			atkMulti = 0.9;
		} else if (strcasecmp(nature[15], "Mite")) {
			defMulti = 0.9;
		} else if (strcasecmp(nature[15], "Quieta")) {
			speedMulti = 0.9;
		} else if (strcasecmp(nature[15], "Ardente")) {
			spDefMulti = 0.9;
		}
	} else if (strcasecmp(nature[15], "Calma") || strcasecmp(nature[15], "Gentile") || strcasecmp(nature[15], "Vivace" || strcasecmp(nature[15], "Cauta"))) {
		spDefMulti = 1.1;
		if (strcasecmp(nature[15], "Calma")) {
			atkMulti = 0.9;
		} else if (strcasecmp(nature[15], "Gentile")) {
			defMulti = 0.9;
		} else if (strcasecmp(nature[15], "Vivace")) {
			speedMulti = 0.9;
		} else if (strcasecmp(nature[15], "Cauta")) {
			spAtkMulti = 0.9;
		}
	} else {
		atkMulti = defMulti = speedMulti = spAtkMulti = spDefMulti = 1.0;
	}
	
	calcHP(name, lvl);
	calcAtk(name, lvl, atkMulti);
	calcDef(name, lvl, defMulti);
	calcSpeed(name, lvl, speedMulti);
	calcSpAtk(name, lvl, spAtkMulti);
	calcSpDef(name, lvl, spDefMulti);
	printf("Calcoli finiti :D");
	return 0;
}

