package com.example.nav;

import org.w3c.dom.Notation;

import android.animation.LayoutTransition;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

public class Settings extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		if (Properties.isOpenNota()) {
			Properties.setOpenNota(false);
			makeNotas();
		} else {
			ListView lista = (ListView) findViewById(R.id.config);
			lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int position, long arg3) {
					if (position == 0) {
						makeNotas();
					}
					if (position == 1) {
						makeChangeMail();
					}
					if (position == 2) {
						makeAbout();
					}

				}
			});
		}
	}

	public void makeAbout() {
		setContentView(R.layout.about);
	}

	public void makeChangeMail() {
		CambiarMail dialogo = new CambiarMail();
		FragmentManager fragmentManager = getFragmentManager();
		dialogo.show(fragmentManager, "Nuevo Mail");
	}

	public void makeNotas() {
		setContentView(R.layout.notas_edit);
		getActionBar().setTitle("Edición de Notas");
		final EditText textIn = (EditText) findViewById(R.id.textin);
		final Spinner notaIn = (Spinner) findViewById(R.id.notain);
		Button buttonAdd = (Button) findViewById(R.id.add);
		Button buttonAceptar = (Button) findViewById(R.id.aceptarNotas);
		Button buttonCancelar = (Button) findViewById(R.id.cancelarNotas);
		final LinearLayout container = (LinearLayout) findViewById(R.id.container);
		for (int i = 0; i < Properties.getObservaciones().length; i++) {
			LayoutInflater layoutInflater = (LayoutInflater) getBaseContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			final View addView = layoutInflater.inflate(R.layout.row, null);
			TextView textOut = (TextView) addView
					.findViewById(R.id.observacionT);
			Spinner nota = (Spinner) addView.findViewById(R.id.notaT);
			nota.setSelection(Properties.getNotas()[i] - 1);
			textOut.setText(Properties.getObservaciones()[i]);
			Button buttonRemove = (Button) addView.findViewById(R.id.remove);
			buttonRemove.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					((LinearLayout) addView.getParent()).removeView(addView);

				}
			});

			container.addView(addView, 0);

		}
		buttonAdd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				LayoutInflater layoutInflater = (LayoutInflater) getBaseContext()
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				final View addView = layoutInflater.inflate(R.layout.row, null);

				Spinner nota = (Spinner) addView.findViewById(R.id.notaT);
				nota.setSelection(notaIn.getSelectedItemPosition());
				TextView textOut = (TextView) addView
						.findViewById(R.id.observacionT);
				textOut.setText(textIn.getText().toString());
				Button buttonRemove = (Button) addView
						.findViewById(R.id.remove);
				buttonRemove.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						((LinearLayout) addView.getParent())
								.removeView(addView);
					}
				});
				((EditText) findViewById(R.id.textin)).setText("");

				container.addView(addView, 0);
			}
		});
		buttonCancelar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		buttonAceptar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				int len = container.getChildCount();
				int[] notas = new int[len];
				String[] observaciones = new String[len];

				for (int i = 0; i < len; i++) {
					Spinner spinner = (Spinner) container.getTouchables().get(
							i * 3);
					EditText edit = (EditText) container.getTouchables().get(
							(i * 3) + 1);
					String obs = edit.getText().toString();
					int nota = Integer.parseInt(spinner.getSelectedItem()
							.toString());
					notas[(len - 1) - i] = nota;
					observaciones[(len - 1) - i] = obs;
				}

				Properties.setNotas(notas);
				Properties.setObservaciones(observaciones);
				finish();
			}
		});
		LayoutTransition transition = new LayoutTransition();
		container.setLayoutTransition(transition);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_settings, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public class CambiarMail extends DialogFragment {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) { //
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			LayoutInflater inflater = getActivity().getLayoutInflater();
			final View agregarAlumnoDialog = inflater.inflate(
					R.layout.add_dialog, null);
			final EditText mail1 = (EditText) agregarAlumnoDialog
					.findViewById(R.id.input);
			mail1.setText(Properties.getMail());
			builder.setView(agregarAlumnoDialog)
					.setMessage("Inserte Nuevo Mail")
					.setPositiveButton("Aceptar",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {

									String mail = mail1.getText().toString();
									Properties.setMail(mail);

								}
							})
					.setNegativeButton("Cancelar",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
								}
							});
			return builder.create();
		}
	}
}