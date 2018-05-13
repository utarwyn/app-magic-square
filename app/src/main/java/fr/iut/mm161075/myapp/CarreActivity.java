package fr.iut.mm161075.myapp;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.TextView;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Activité du jeu.
 * Gère la partie graphique du jeu "Carré magique".
 *
 * @version 1.0.0
 * @author Maxime MALGORN
 */
public class CarreActivity extends AppCompatActivity {

	/**
	 * Lien qui mène vers les règles du jeu
	 */
	private final static String RULES_URL = "https://fr.m.wikipedia.org/wiki/Carré_magique_(mathématiques)";

	/**
	 * Liste des champs d'édition de la grille
	 */
	private List<EditText> champs;

	/**
	 * Libellé du status de jeu
	 */
	private TextView status;

	/**
	 * Chronomètre du jeu
	 */
	private Chronometer chrono;

	/**
	 * Objet (métier) pour gérer la grille de jeu
	 */
	private CarreMagique carreMagique;

	@Override
	protected void onCreate(Bundle saveBundleState) {
		super.onCreate(saveBundleState);
		setContentView(R.layout.activity_carre);

		this.status = this.findViewById(R.id.status);
		this.chrono = this.findViewById(R.id.chrono);

		// Enregistrement des champs éditables en mémoire
		this.champs = new ArrayList<>();

		this.champs.add((EditText) this.findViewById(R.id.t1));
		this.champs.add((EditText) this.findViewById(R.id.t2));
		this.champs.add((EditText) this.findViewById(R.id.t3));
		this.champs.add((EditText) this.findViewById(R.id.t4));
		this.champs.add((EditText) this.findViewById(R.id.t5));
		this.champs.add((EditText) this.findViewById(R.id.t6));
		this.champs.add((EditText) this.findViewById(R.id.t7));
		this.champs.add((EditText) this.findViewById(R.id.t8));
		this.champs.add((EditText) this.findViewById(R.id.t9));

		Bundle bundle = getIntent().getExtras();

		if (bundle != null && bundle.containsKey("niveau")) {
			this.creationCarreMagique(bundle.getInt("niveau"));
		} else {
			// Par défaut on cache la grille & les boutons
			this.findViewById(R.id.grille).setVisibility(View.INVISIBLE);
			this.findViewById(R.id.soumettre).setEnabled(false);
			this.findViewById(R.id.aide).setEnabled(false);

			this.status.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
			this.status.setText(R.string.new_game);
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle bundle) {
		super.onSaveInstanceState(bundle);

		// Sauvegarde de l'objet Carré magique
		if (this.carreMagique != null) {
			try {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ObjectOutputStream oos = new ObjectOutputStream(baos);

				oos.writeObject(this.carreMagique);
				bundle.putByteArray("carre_magique", baos.toByteArray());

				// Ecriture du status
				bundle.putInt("status_bgcolor", ((ColorDrawable) this.status.getBackground()).getColor());
				bundle.putInt("status_fgcolor", this.status.getCurrentTextColor());
				bundle.putString("status_text", this.status.getText().toString());

				// Chrono
				bundle.putLong("chrono_st", this.chrono.getBase() - SystemClock.elapsedRealtime());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	protected void onRestoreInstanceState(Bundle bundle) {
		super.onRestoreInstanceState(bundle);

		// Restauration de l'objet Carré magique et MAJ de l'interface
		if (bundle.containsKey("carre_magique")) {
			try {
				ByteArrayInputStream bais = new ByteArrayInputStream(bundle.getByteArray("carre_magique"));
				ObjectInputStream ois = new ObjectInputStream(bais);

				this.carreMagique = (CarreMagique) ois.readObject();
				this.majInterface();

				// Mise à jour du status
				this.status.setBackgroundColor(bundle.getInt("status_bgcolor"));
				this.status.setTextColor(bundle.getInt("status_fgcolor"));
				this.status.setText(bundle.getString("status_text"));

				// Mise à jour du chronomètre
				if (this.carreMagique.jeuEnCours()) {
					this.chrono.setBase(SystemClock.elapsedRealtime() + bundle.getLong("chrono_st"));
					this.chrono.start();
				} else if (this.carreMagique.getTempsDeJeu() != null) {
					this.chrono.setFormat("Niveau " + this.carreMagique.getNiveau() + " / Score: " + this.carreMagique.getScore() + " / %s");
					this.chrono.setBase(SystemClock.elapsedRealtime() - this.carreMagique.getTempsDeJeu());
					this.chrono.stop();
				}

			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Retourne la matrice de valeurs de la grille de jeu
	 * @return Matrice des valeurs
	 */
	private int[][] getMatriceValeurs() {
		int[][] matrice = new int[CarreMagique.ORDRE][CarreMagique.ORDRE];

		for (int y = 0; y < matrice.length; y++)
			for (int x = 0; x < matrice[y].length; x++)
				matrice[y][x] = getValeurChamp(y * matrice[0].length + x);

		return matrice;
	}

	/**
	 * Retourne la valeur du champ souhaité
	 * @param n Indice du champ où rechercher la valeur
	 * @return Valeur trouvée dans le champ souhaitée
	 */
	private int getValeurChamp(int n) {
		String v = this.champs.get(n).getText().toString();

		try {
			return Integer.parseInt(v);
		} catch (Exception ex) {
			return Integer.MIN_VALUE;
		}
	}

	/**
	 * Ouvre l'activité liée à la demande du niveau à jouer
	 */
	private void demanderNiveau() {
		Intent intent = new Intent(CarreActivity.this, CarreNiveauActivity.class);
		startActivity(intent);
	}

	/**
	 * Créé les objets nécessaires au fonctionnement de la grille du carré magique
	 * @param niv Niveau à utiliser pour initialiser le jeu
	 */
	private void creationCarreMagique(int niv) {
		if (niv < 1 || niv > 9) return;

		for (EditText champ : this.champs) {
			champ.setText("");
			champ.setTextColor(Color.BLACK);

			champ.setFocusableInTouchMode(true);
			champ.setFocusable(true);
		}

		this.status.setBackgroundResource(R.color.colorPrimary);
		this.status.setText("");

		// Création du nouveau carré magique
		this.carreMagique = new CarreMagique();

		this.findViewById(R.id.grille).setVisibility(View.VISIBLE);
		this.findViewById(R.id.soumettre).setEnabled(true);
		this.findViewById(R.id.aide).setEnabled(true);
		this.status.setText("");

		this.carreMagique.remplir();
		this.carreMagique.setNiveau(niv);
		this.carreMagique.demarrer();

		// Remplissage de la grille suivant le niveau
		for (int i = 0; i < 9 - this.carreMagique.getNiveau(); i++)
			this.devoilerChiffre(false);

		// Démarrage du chronomètre
		this.chrono.setVisibility(View.VISIBLE);
		this.chrono.setFormat("Niveau " + this.carreMagique.getNiveau() + " / %s");
		this.chrono.start();

		this.majInterface();
	}

	/**
	 * Met à jour l'activité Android en fonction des informations du métier
	 */
	private void majInterface() {
		if (this.carreMagique == null) return;

		// Affichage des résultats
		int[] res = this.carreMagique.getResultats();

		((TextView) this.findViewById(R.id.r1)).setText(String.valueOf(res[0]));
		((TextView) this.findViewById(R.id.r2)).setText(String.valueOf(res[1]));
		((TextView) this.findViewById(R.id.r3)).setText(String.valueOf(res[2]));
		((TextView) this.findViewById(R.id.r4)).setText(String.valueOf(res[3]));
		((TextView) this.findViewById(R.id.r5)).setText(String.valueOf(res[4]));
		((TextView) this.findViewById(R.id.r6)).setText(String.valueOf(res[5]));

		// Mise à jour des champs texte si besoin
		int i = 0;

		for (EditText champ : this.champs) {
			if (!this.carreMagique.getRemplissageValeursAide()[i] &&
					!this.carreMagique.getRemplissageValeursDefaut()[i]) {
				champ.setFocusable(true);
				champ.setFocusableInTouchMode(true);
			} else {
				champ.setFocusable(false);
			}

			if (this.carreMagique.getRemplissageValeursAide()[i++])
				champ.setTextColor(Color.RED);
			else
				champ.setTextColor(Color.BLACK);
		}
	}

	/**
	 * Dévoile un chiffre dans la grille.
	 * @param aide Si vrai, indique au métier que le chiffre à été dévoilé
	 *             par l'utilisation du bouton "Aide".
	 */
	private void devoilerChiffre(boolean aide) {
		// On test avant si tous les champs sont remplis ou non ...
		boolean grilleRemplie = true;

		for (EditText champ : this.champs)
			if (champ.getText().toString().isEmpty())
				grilleRemplie = false;

		if (grilleRemplie) return;

		// ... et on affiche un chiffre au hasard !
		EditText champ;
		int indice;

		do {
			indice = CarreMagique.RND.nextInt(this.champs.size());
			champ = this.champs.get(indice);

			if (!champ.getText().toString().isEmpty())
				champ = null;

		} while (champ == null);

		champ.setText(String.valueOf(this.carreMagique.getValeur(indice)));
		champ.setFocusable(false);

		if (aide) {
			this.carreMagique.ajouterIndiceAideRempli(indice);
			champ.setTextColor(Color.RED);
		} else {
			this.carreMagique.ajouterIndiceDefautRempli(indice);
		}
	}

	/**
	 * Méthode qui gère le clic sur le bouton "Nouvelle partie"
	 * @param view Bouton
	 */
	public void nouvellePartie(View view) {
		// Demande du niveau à utiliser
		this.demanderNiveau();
	}

	/**
	 * Méthode qui gère le clic sur le bouton "Règles du jeu"
	 * @param view Bouton
	 */
	public void ouvrirRegles(View view) {
		Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(RULES_URL));
		startActivity(browserIntent);
	}

	/**
	 * Méthode qui gère le clic sur le bouton "Soumettre"
	 * @param view Bouton
	 */
	public void verifierGrille(View view) {
		if (this.carreMagique == null) return;

		if (this.carreMagique.verifier(this.getMatriceValeurs())) {
			this.status.setBackgroundResource(R.color.colorSuccess);
			this.status.setTextColor(Color.rgb(0, 0, 0));
			this.status.setText(R.string.status_yes);

			this.carreMagique.terminer();

			this.chrono.setFormat("Niveau " + this.carreMagique.getNiveau() + " / Score: " + this.carreMagique.getScore() + " / %s");
			this.chrono.setBase(this.chrono.getBase());
			this.chrono.stop();
		} else {
			this.status.setBackgroundResource(R.color.colorError);
			this.status.setTextColor(Color.rgb(255, 255, 255));
			this.status.setText(R.string.status_no);
		}
	}

	/**
	 * Méthode qui gère le clic sur le bouton "Aide"
	 * @param view Bouton
	 */
	public void aider(View view) {
		if (this.carreMagique == null) return;

		this.devoilerChiffre(true);
	}

	/**
	 * Méthode qui gère le clic sur le bouton "Quitter"
	 * @param view Bouton
	 */
	public void quitter(View view) {
		this.finish();
	}

}
