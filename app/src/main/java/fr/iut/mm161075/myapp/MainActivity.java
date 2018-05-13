package fr.iut.mm161075.myapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Activité principale de l'application.
 * Affiche les boutons du menu prinipal pour choisir le module.
 *
 * @version 1.0.0
 * @author Maxime MALGORN
 */
public class MainActivity extends AppCompatActivity {

	@Override
	public void onCreate(Bundle saveBundleState) {
		super.onCreate(saveBundleState);
		setContentView(R.layout.activity_main);
	}

	public void allerVersMultiplication(View view) {
		Intent intent = new Intent(MainActivity.this, MultiplicationActivity.class);
		startActivity(intent);
	}

	public void allerVersCarreMagique(View view) {
		Intent intent = new Intent(MainActivity.this, CarreActivity.class);
		startActivity(intent);
	}

	public void quitter(View view) {
		this.finish();
	}

}
