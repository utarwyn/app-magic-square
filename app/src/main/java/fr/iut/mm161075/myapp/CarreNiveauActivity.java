package fr.iut.mm161075.myapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

/**
 * Activité Android pour choisir le niveau de jeu.
 *
 * @version 1.0.0
 * @author Maxime MALGORN
 */
public class CarreNiveauActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_carre_niveau);
	}

	public void selectionnerNiveau(View view) {
		Button bouton = (Button) view;

		Intent intent = new Intent(this, CarreActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra("niveau", Integer.parseInt(bouton.getText().toString()));

		startActivity(intent);
	}

}
