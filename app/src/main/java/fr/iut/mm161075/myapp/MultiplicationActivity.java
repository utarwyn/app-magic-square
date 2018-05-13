package fr.iut.mm161075.myapp;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MultiplicationActivity extends AppCompatActivity {

	private int nbOfCalls;

	private TextView operation;

	private EditText champ;

	private int result;

	private int bgColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplication);

        this.operation = findViewById(R.id.operation);
        this.champ = findViewById(R.id.valeur);
        this.bgColor = ((ColorDrawable) this.getWindow().getDecorView().getBackground()).getColor();

        this.newOperation();

		/*Toast myToast;

		myToast = Toast.makeText(this, "onCreate() " + (++nbOfCalls), Toast.LENGTH_LONG);
		myToast.setGravity(Gravity.CENTER, 0, 0);
		myToast.show();*/
    }

    @Override
    protected void onStart() {
        super.onStart();
        /*Toast myToast;

        myToast = Toast.makeText(this, "onStart() " + (++nbOfCalls), Toast.LENGTH_LONG);
        myToast.setGravity(Gravity.CENTER, 0, 0);
        myToast.show();*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        /*Toast myToast;

        myToast = Toast.makeText(this, "onResume() " + (++nbOfCalls), Toast.LENGTH_LONG);
        myToast.setGravity(Gravity.CENTER, 0, 0);
        myToast.show();*/
    }

    @Override
    protected void onPause() {
        super.onPause();
        /*Toast myToast;

        myToast = Toast.makeText(this, "onPause() " + (++nbOfCalls), Toast.LENGTH_LONG);
        myToast.setGravity(Gravity.CENTER, 0, 0);
        myToast.show();*/
    }

    @Override
    protected void onStop() {
        super.onStop();
        /*Toast myToast;

        myToast = Toast.makeText(this, "onStop() " + (++nbOfCalls), Toast.LENGTH_LONG);
        myToast.setGravity(Gravity.CENTER, 0, 0);
        myToast.show();*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /*Toast myToast;

        myToast = Toast.makeText(this, "onDestroy() " + (++nbOfCalls), Toast.LENGTH_LONG);
        myToast.setGravity(Gravity.CENTER, 0, 0);
        myToast.show();*/
    }

    private void newOperation() {
    	int nbA = (int) Math.round(Math.random() * 10);
    	int nbB = (int) Math.round(Math.random() * 10);

    	this.result = nbA * nbB;

    	this.operation.setText(nbA + " x " + nbB + " =");
		this.champ.setText("");

		this.champ.setBackgroundColor(Color.WHITE);
		this.champ.setTextColor(Color.BLACK);
	}

	public void verifyOperation(View view) {
		try {
			int valeur = Integer.parseInt(this.champ.getText().toString());

			if (valeur == this.result)
				this.newOperation();
			else {
				this.champ.setBackgroundColor(getResources().getColor(R.color.colorError));
				this.champ.setTextColor(Color.WHITE);
			}
		} catch (Exception ignored) {

		}
	}

}
