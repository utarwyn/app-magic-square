package fr.iut.mm161075.myapp;

import android.os.SystemClock;

import java.io.Serializable;
import java.util.Random;

/**
 * Classe métier du jeu.
 * Elle gère aussi le stockage des données pour
 * garder un cycle de vie d'application correct.
 * Représente une grille de carré magique.
 *
 * @version 1.0.0
 * @author Maxime MALGORN
 */
class CarreMagique implements Serializable {

	/**
	 * Ordre du carré magique
	 */
	static final int ORDRE = 3;

	/**
	 * Un pseudo-générateur (random)
	 */
	static final Random RND = new Random();

	/**
	 * Niveau de la grille
	 */
	private int niveau;

	/**
	 * Matrice stockant les résultats prégénérés de la grille
	 */
	private int[] res;

	/**
	 * Matrice stockant les valeurs de la grille
	 */
	private Integer[][] matrice;

	/**
	 * Stocke l'état d'aide de la grille de jeu
	 */
	private boolean[] valeursAide;

	/**
	 * Stocke l'état par défaut de la grille de jeu
	 */
	private boolean[] valeursDefaut;

	/**
	 * Heure de démarrage de la grille
	 */
	private Long tempsDemarrage;

	/**
	 * Temps de jeu sur la grille
	 */
	private Long tempsDeJeu;

	/**
	 * Construit une grille de carré magique
	 */
	CarreMagique() {
		this.res = new int[ORDRE * 2];
		this.matrice = new Integer[ORDRE][ORDRE];
		this.valeursAide = new boolean[ORDRE * ORDRE];
		this.valeursDefaut = new boolean[ORDRE * ORDRE];
		this.niveau = 1;
	}

	int getNiveau() {
		return this.niveau;
	}

	int getValeur(int indice) {
		return this.matrice[indice / ORDRE][indice % ORDRE];
	}

	int[] getResultats() {
		return this.res;
	}

	boolean[] getRemplissageValeursAide() {
		return this.valeursAide;
	}

	boolean[] getRemplissageValeursDefaut() {
		return this.valeursDefaut;
	}

	/**
	 * Retourne vrai si le temps de jeu n'a pas encore été calculé (grille non terminée)
	 * @return Vrai si la grille n'est pas terminée
	 */
	boolean jeuEnCours() {
		return this.tempsDemarrage != null && this.tempsDeJeu == null;
	}

	/**
	 * Retourne le score du joueur sur la grille courrante.
	 * (en fonction du niveau et du nombre d'aides)
	 * @return Score du joueur sur la grille
	 */
	int getScore() {
		return Math.max(0, (int) ((this.niveau - this.getNbAides()) * 1000D / 9 - this.tempsDeJeu/1000));
	}

	/**
	 * Retourne le temps de jeu en millisecondes
	 * @return Temps de jeu (ms)
	 */
	Long getTempsDeJeu() {
		return this.tempsDeJeu;
	}

	/**
	 * Rempli la grille (dans le métier)
	 * avec des chiffres de 1 à 9.
	 */
	void remplir() {
		int x, y;

		for (int i = 1; i <= ORDRE * ORDRE; i++) {
			do {
				y = RND.nextInt(this.matrice.length);
				x = RND.nextInt(this.matrice[y].length);
			} while (this.matrice[y][x] != null);

			this.matrice[y][x] = i;
		}

		this.genererResultats();
	}

	/**
	 * Défini le niveau de la grille
	 * @param niveau Niveau à définir
	 */
	void setNiveau(int niveau) {
		this.niveau = niveau;
	}

	/**
	 * Démarre la session de jeu
	 */
	void demarrer() {
		this.tempsDemarrage = SystemClock.elapsedRealtime();
	}

	/**
	 * Termine la session de jeu
	 */
	void terminer() {
		this.tempsDeJeu = SystemClock.elapsedRealtime() - this.tempsDemarrage;
	}

	void ajouterIndiceAideRempli(int indice) {
		this.valeursAide[indice] = true;
	}

	void ajouterIndiceDefautRempli(int indice) {
		this.valeursDefaut[indice] = true;
	}

	/**
	 * Genère les résultats à la création de la grille
	 */
	private void genererResultats() {
		int l1 = this.matrice[0][0] + this.matrice[0][1] + this.matrice[0][2];
		int l2 = this.matrice[1][0] + this.matrice[1][1] + this.matrice[1][2];
		int l3 = this.matrice[2][0] + this.matrice[2][1] + this.matrice[2][2];

		int c1 = this.matrice[0][0] + this.matrice[1][0] + this.matrice[2][0];
		int c2 = this.matrice[0][1] + this.matrice[1][1] + this.matrice[2][1];
		int c3 = this.matrice[0][2] + this.matrice[1][2] + this.matrice[2][2];

		this.res = new int[] { l1, l2, l3, c1, c2, c3 };
	}

	/**
	 * Retounre le nombre de cases qui ont été découvertes
	 * via le bouton "Aide".
	 * @return Nombre de cases
	 */
	private int getNbAides() {
		int nb = 0;

		for (boolean aide : this.valeursAide)
			if (aide)
				nb++;

		return nb;
	}

	/**
	 * Vérifie si une matrice correspond à la grille enregistrée en mémoire
	 * @param matrice Matrice à tester
	 * @return Vrai si la matrice correspond
	 */
	boolean verifier(int[][] matrice) {
		for (int x = 0; x < ORDRE; x++)
			for (int y = 0; y < ORDRE; y++)
				if (this.matrice[x][y] != matrice[x][y])
					return false;

		return true;
	}

}
