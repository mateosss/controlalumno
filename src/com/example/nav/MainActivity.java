package com.example.nav;

import java.util.Arrays;
import java.util.Date;

import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	private DrawerLayout dondeDibujar;
	private ListView listaNavDraw;
	private ActionBarDrawerToggle navDrawToggle;
	private CharSequence tituloDelLienzo;
	private CharSequence tituloDelMenu;
	private String[] itemsNavDraw;

	private Curso[] cursos = new Curso[0];
	private Curso lastCourse = null;
	private Alumno lastAlumn = null;
	private String[] observaciones = {
			"Consume Drogas ilegales en clase",
			"No Consume Drogas ilegales en clase",
			"Le pega un \"Tubaso\" a su compañero, y no estoy hablando de una llamada",
			"Le gusta rengar", "Dice \" Hola\" reiteradamente", "Flatulencia",
			"Tira un monitor a la cabeza de su compañero",
			"Agrega el prefijo perr , ej: \"perranchez\"",
			"Agrega el prefijo chucho, ej: \"chucholas\"",
			"Agrega el sufijo ossio , ej: \"Sanchossio\"" };
	private int[] notas = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };

	public void saveProperties() {
		Properties.setCursos(cursos);
		Properties.setLastAlumn(lastAlumn);
		Properties.setLastCourse(lastCourse);
		Properties.setObservaciones(observaciones);
		Properties.setNotas(notas);

		Properties.save();
	}

	public void syncProperties() {
		cursos = Properties.getCursos();
		lastCourse = Properties.getLastCourse();
		lastAlumn = Properties.getLastAlumn();
		observaciones = Properties.getObservaciones();
		notas = Properties.getNotas();
	}

	public void mostrarGrafico(View view) {

		mostrarGraficoDialogo dialogo = new mostrarGraficoDialogo();
		FragmentManager fragmentManager = getFragmentManager();
		dialogo.show(fragmentManager, "Grafico");
	}

	public void openSettings(boolean openNota) {
		Intent intent = new Intent(this, Settings.class);
		if (openNota) {
			Properties.setOpenNota(true);
		}
		startActivity(intent);
	}

	public String[] makeNotas() {
		String[] notasFinal = new String[notas.length + 1];
		for (int i = 0; i < notas.length; i++) {
			notasFinal[i] = notas[i] + " - " + observaciones[i];
		}
		notasFinal[notasFinal.length - 1] = "Editar notas...";
		return notasFinal;

	}

	public void addAlumnoEvent(View view) {
		agregarAlumnoDialogo dialogo = new agregarAlumnoDialogo();
		FragmentManager fragmentManager = getFragmentManager();
		dialogo.show(fragmentManager, "Agregar Alumno");
	}

	public void addNotaEvent(View view) {
		agregarNotaDialogo dialogo = new agregarNotaDialogo();
		FragmentManager fragmentManager = getFragmentManager();
		dialogo.show(fragmentManager, "Agregar Nota");

	}

	public void addCursoEvent(View view) {
		agregarCursoDialogo dialogo = new agregarCursoDialogo();
		FragmentManager fragmentManager = getFragmentManager();
		dialogo.show(fragmentManager, "Agregar Curso");

	}

	public boolean addCurso(Curso curso) {
		Curso[] cursosAux = new Curso[cursos.length + 1];
		for (int i = 0; i < cursos.length; i++) {
			cursosAux[i] = cursos[i];
			if (curso.getCurso().equalsIgnoreCase(cursosAux[i].getCurso())) {
				toast("Error: Curso ya existente!");
				return false;
			}
		}
		cursosAux[cursosAux.length - 1] = curso;
		cursos = cursosAux;
		toast("Agregado");
		return true;
	}

	public boolean addAlumno(Alumno alumno, Curso curso) {
		boolean cargar = false;
		for (int i = 0; i < cursos.length; i++) {
			if (cursos[i].getCurso().equalsIgnoreCase((curso.getCurso()))) {
				cargar = cursos[i].addAlumno(alumno);
				toast("Alumno Cargado");
				break;
			}
		}
		if (!cargar) {
			toast("Error: Alumno ya existe");
			return false;
		}
		return true;
	}

	public boolean addNota(Nota nota, Alumno alumno, Curso curso) {

		for (int i = 0; i < cursos.length; i++) {
			toast(cursos[i].getCurso());
			if (cursos[i].getCurso().equalsIgnoreCase((curso.getCurso()))) {
				for (int j = 0; j < cursos[i].getAlumnos().length; j++) {
					if (cursos[i].getAlumnos()[j].getNombre().equalsIgnoreCase(
							alumno.getNombre())) {
						cursos[i].getAlumnos()[j].addNota(nota);
						toast("Nota agregada");
						return true;
					} else {
						if (j == cursos[i].getAlumnos().length - 1) {
							toast("No se encuentra el alumno "
									+ alumno.getNombre() + " en el curso "
									+ curso.getCurso());
							return false;
						}
					}
				}
			}
		}
		toast("No se encuentra el curso " + curso.getCurso());
		return false;
	}

	public void cargarGrafico(XYPlot grafico, Number[] serie) {
		for (int i = 0; i < serie.length; i++) {
			System.out.println("PATOSIO");
			System.out.println(serie[i] + "");
		}
		XYSeries series1 = new SimpleXYSeries(Arrays.asList(serie),
				SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Notas");

		LineAndPointFormatter series1Format = new LineAndPointFormatter(
				Color.rgb(255, 255, 255), Color.rgb(0, 153, 204),
				Color.alpha(0), null);
		grafico.destroyDrawingCache();
		grafico.setScrollContainer(false);
		grafico.clear();
		grafico.addSeries(series1, series1Format);

	}

	public void refresh(int tipo) {

		String[] listaString = null;
		ListView lista = null;

		if (Properties.isFirsTime()) {
			syncProperties();
			Properties.setFirsTime(false);

		}

		if (tipo == 0) {

			listaString = new String[cursos.length];
			for (int i = 0; i < listaString.length; i++) {
				listaString[i] = cursos[i].getCurso();
			}
			lista = (ListView) findViewById(R.id.cursos);
			ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1, listaString);
			lista.setAdapter(adaptador);
			lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int position, long arg3) {
					Fragment fragment = new VistaFragment();
					Bundle args = new Bundle();
					args.putInt(VistaFragment.ARG_ITEM_NUMBER, 1);
					fragment.setArguments(args);
					FragmentManager fragmentManager = getFragmentManager();
					fragmentManager.beginTransaction()
							.replace(R.id.contenedor, fragment).commit();
					listaNavDraw.setItemChecked(1, true);
					lastCourse = cursos[position];

					getActionBar().setTitle(cursos[position].getCurso());
					toast(cursos[position].getCurso());
				}
			});
			lista.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
				@Override
				public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
						int position, long arg3) {
					toast("eliminado " + cursos[position].getCurso());

					Curso[] cursosAux = new Curso[cursos.length - 1];
					int borrar = 0;
					for (int i = 0; i < cursos.length; i++) {
						if (cursos[i].getCurso().equalsIgnoreCase(
								cursos[position].getCurso())) {
							borrar++;
						} else {
							cursosAux[i - borrar] = cursos[i];
						}
					}
					cursos = cursosAux;
					lastCourse = null;
					refresh(0);
					return false;
				}

			});

		}

		else {
			if (tipo == 1) {
				if (lastCourse == null) {
					toast("No hay curso seleccionado");
				} else {
					listaString = new String[lastCourse.getAlumnos().length];
					for (int i = 0; i < listaString.length; i++) {
						listaString[i] = lastCourse.getAlumnos()[i].getNombre();
					}
					lista = (ListView) findViewById(R.id.alumnos);
					ArrayAdapter<String> adaptador = new ArrayAdapter<String>(
							this, android.R.layout.simple_list_item_1,
							listaString);
					lista.setAdapter(adaptador);
					lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1,
								int position, long arg3) {
							Fragment fragment = new VistaFragment();
							Bundle args = new Bundle();
							args.putInt(VistaFragment.ARG_ITEM_NUMBER, 2);
							fragment.setArguments(args);
							FragmentManager fragmentManager = getFragmentManager();
							fragmentManager.beginTransaction()
									.replace(R.id.contenedor, fragment)
									.commit();
							listaNavDraw.setItemChecked(2, true);
							lastAlumn = lastCourse.getAlumnos()[position];
							getActionBar().setTitle(
									lastCourse.getAlumnos()[position]
											.getNombre());
							toast(lastCourse.getAlumnos()[position].getNombre());

						}
					});
					lista.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
						@Override
						public boolean onItemLongClick(AdapterView<?> arg0,
								View arg1, int position, long arg3) {
							toast("eliminado "
									+ lastCourse.getAlumnos()[position]
											.getNombre());
							for (int i = 0; i < cursos.length; i++) {
								if (cursos[i].getCurso().equalsIgnoreCase(
										lastCourse.getCurso())) {
									toast(lastCourse.getAlumnos()[position]
											.getNombre());
									cursos[i].deleteAlumno(lastCourse
											.getAlumnos()[position].getNombre());
									lastCourse = cursos[i];
									refresh(1);
								}
							}
							return false;
						}

					});
				}
			} else {
				if (tipo == 2) {
					if (lastAlumn == null) {
						toast("No hay alumno seleccionado");
						// setContentView(R.layout.no_alumn_selected);
					} else {

						listaString = new String[lastAlumn.getHistorial()
								.getNotas().length];

						for (int i = 0; i < listaString.length; i++) {

							System.out.println(lastAlumn.getHistorial()
									.getNotas()[i].getNota() + "");

							listaString[(listaString.length - 1) - i] = lastAlumn
									.getHistorial().getNotas()[i].getNota()
									+ "\n Dia: "
									+ lastAlumn.getHistorial().getNotas()[i]
											.getFecha()
									+ "\n Observación: "
									+ lastAlumn.getHistorial().getNotas()[i]
											.getAnotacion();
						}
						lista = (ListView) findViewById(R.id.historial);

						ArrayAdapter<String> adaptador = new ArrayAdapter<String>(
								this, android.R.layout.simple_list_item_1,
								listaString);
						lista.setAdapter(adaptador);

						lista.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
							@Override
							public boolean onItemLongClick(AdapterView<?> arg0,
									View arg1, int position, long arg3) {
								for (int i = 0; i < lastAlumn.getHistorial()
										.getNotas().length; i++) {
									if (lastAlumn.getHistorial().getNotas()[(lastAlumn
											.getHistorial().getNotas().length - 1)
											- i]
											.getFecha()
											.toString()
											.equalsIgnoreCase(
													lastAlumn.getHistorial()
															.getNotas()[lastAlumn
															.getHistorial()
															.getNotas().length
															- 1 - position]
															.getFecha()
															.toString())) {
										toast(lastAlumn.getHistorial()
												.getNotas()[lastAlumn
												.getHistorial().getNotas().length
												- 1 - position].toString());
										lastAlumn
												.deleteNota(lastAlumn
														.getHistorial()
														.getNotas()[lastAlumn
														.getHistorial()
														.getNotas().length
														- 1 - position]);

									}
								}
								for (int j = 0; j < cursos.length; j++) {
									if (cursos[j].getCurso().equalsIgnoreCase(
											lastCourse.getCurso())) {
										for (int k = 0; k < cursos[j]
												.getAlumnos().length; k++) {
											if (cursos[j].getAlumnos()[k]
													.getNombre()
													.equalsIgnoreCase(
															lastAlumn
																	.getNombre())) {
												cursos[j].getAlumnos()[k] = lastAlumn;
											}
										}
									}
								}
								refresh(2);
								return false;
							}

						});

						TextView nombre = (TextView) findViewById(R.id.alumno);
						TextView notamax = (TextView) findViewById(R.id.notamax);
						TextView notamin = (TextView) findViewById(R.id.notamin);
						TextView promedio = (TextView) findViewById(R.id.promedio);
						TextView estado = (TextView) findViewById(R.id.estado);
						nombre.setText(lastAlumn.getNombre() + "");
						notamax.setText("Nota Max: " + lastAlumn.getNotaMax());
						notamin.setText("Nota Min: " + lastAlumn.getNotaMin());
						promedio.setText("Promedio: " + lastAlumn.getPromedio());
						estado.setText("Estado: " + lastAlumn.getEstado());
					}
				}
			}

		}

		observaciones = Properties.getObservaciones();
		notas = Properties.getNotas();
		saveProperties();
	}

	public void toast(String texto) {
		Toast.makeText(this, texto, Toast.LENGTH_SHORT).show();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Properties.load();

		tituloDelMenu = tituloDelLienzo = getTitle();
		itemsNavDraw = getResources().getStringArray(R.array.items_array);// Array
																			// de
																			// Strings
		dondeDibujar = (DrawerLayout) findViewById(R.id.lienzo);// El drawer
																// layout,
																// container
																// contenedor
																// del activity
																// layout
		listaNavDraw = (ListView) findViewById(R.id.listaNavDraw);// el listView
																	// del
																	// Activity
																	// layout
		dondeDibujar.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);// Sombrita del navdraw
		listaNavDraw.setAdapter(new ArrayAdapter<String>(this,
				R.layout.drawer_list_item, itemsNavDraw));// Mete el texto en el
															// navDraw
		listaNavDraw.setOnItemClickListener(new DrawerItemClickListener());// Hace
																			// que
																			// cuando
																			// tocas
																			// algo
		// se abra el layout y se esconda el navdraw
		getActionBar().setDisplayHomeAsUpEnabled(true);// tocas icono y sale el
														// nav Draw
		getActionBar().setHomeButtonEnabled(true);// ademas del icono de las 3
													// barritas

		// crea la interaccion entre el icono y que se abra el navdraw
		navDrawToggle = new ActionBarDrawerToggle(this, /* host Activity */
		dondeDibujar, /* DrawerLayout object */
		R.drawable.ic_drawer, /* nav drawer image to replace 'Up' caret */
		R.string.drawer_open, /* "open drawer" description for accessibility */
		R.string.drawer_close /* "close drawer" description for accessibility */
		) {
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(tituloDelMenu);
				invalidateOptionsMenu(); // creates call to onPrepareoptions
											// menu
				if (((String) getActionBar().getTitle())
						.equalsIgnoreCase("All Courses")) {
					refresh(0);
					toast("refresh 0");
				}
				if (((String) getActionBar().getTitle())
						.equalsIgnoreCase("Last Course")) {
					refresh(1);
					toast("refresh 2");
				}
				if (((String) getActionBar().getTitle())
						.equalsIgnoreCase("Last Alumn")) {
					refresh(2);
					toast("refresh 3");
				}

			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(tituloDelLienzo);
				invalidateOptionsMenu(); // creates call to
											// onPrepareOptionsMenu()
			}
		};
		dondeDibujar.setDrawerListener(navDrawToggle);

		if (savedInstanceState == null) {
			selectItem(0);
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);

		if (((String) getActionBar().getTitle())
				.equalsIgnoreCase("All Courses")) {
			refresh(0);
			toast("refresh 0");
		}
		if (((String) getActionBar().getTitle())
				.equalsIgnoreCase("Last Course")) {
			refresh(1);
			toast("refresh 2");
		}
		if (((String) getActionBar().getTitle()).equalsIgnoreCase("Last Alumn")) {
			refresh(2);
			toast("refresh 3");
		}
		return super.onCreateOptionsMenu(menu);
	}

	/* Called whenever we call invalidateOptionsMenu() */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// Cuando abris o cerras el nav draw se esconde o aparece el icono de
		// search respectivamente
		boolean drawerOpen = dondeDibujar.isDrawerOpen(listaNavDraw);
		menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
		menu.findItem(R.id.action_add).setVisible(!drawerOpen);
		menu.findItem(R.id.action_export).setVisible(!drawerOpen);
		menu.findItem(R.id.action_mail).setVisible(!drawerOpen);
		if (((String) getActionBar().getTitle())
				.equalsIgnoreCase("All Courses")) {
			refresh(0);
			toast("refresh 0");
		}
		if (((String) getActionBar().getTitle())
				.equalsIgnoreCase("Last Course")) {
			refresh(1);
			toast("refresh 2");
		}
		if (((String) getActionBar().getTitle()).equalsIgnoreCase("Last Alumn")) {
			refresh(2);
			toast("refresh 3");
		}
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// The action bar home/up action should open or close the drawer.
		// ActionBarDrawerToggle will take care of this.
		if (navDrawToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Handle action buttons
		switch (item.getItemId()) {
		case R.id.action_websearch:
			Intent intent = new Intent(this, Search.class);
			startActivity(intent);

			return true;
		case R.id.action_settings:
			openSettings(false);
			return true;

		case R.id.action_export:
			if (((String) getActionBar().getTitle())
					.equalsIgnoreCase("Last Course")) {
				PdfManager.crearPdfCurso(MainActivity.this, lastCourse);
				PdfManager
						.showPdfFile(lastCourse.getCurso(), MainActivity.this);

			}
			if (((String) getActionBar().getTitle())
					.equalsIgnoreCase("Last Alumn")) {
				PdfManager.crearPdfAlumno(MainActivity.this, lastCourse,
						lastAlumn);
				PdfManager
						.showPdfFile(lastAlumn.getNombre(), MainActivity.this);
			}
			return true;

		case R.id.action_mail:
			if (((String) getActionBar().getTitle())
					.equalsIgnoreCase("Last Course")) {
				PdfManager.crearPdfCurso(MainActivity.this, lastCourse);
				PdfManager.sendPdfByEmail(lastCourse.getCurso(),
						Properties.getMail(),
						"Informe de " + lastCourse.getCurso(),
						MainActivity.this);
			}
			if (((String) getActionBar().getTitle())
					.equalsIgnoreCase("Last Alumn")) {
				PdfManager.crearPdfAlumno(MainActivity.this, lastCourse,
						lastAlumn);
				PdfManager.sendPdfByEmail(lastAlumn.getNombre(),
						Properties.getMail(),
						"Informe de " + lastAlumn.getNombre(),
						MainActivity.this);
			}
			return true;
		case R.id.action_add:
			if (((String) getActionBar().getTitle())
					.equalsIgnoreCase("All Courses")) {
				addCursoEvent(dondeDibujar);
			}
			if (((String) getActionBar().getTitle())
					.equalsIgnoreCase("Last Course")) {
				addAlumnoEvent(dondeDibujar);
			}
			if (((String) getActionBar().getTitle())
					.equalsIgnoreCase("Last Alumn")) {
				addNotaEvent(dondeDibujar);
			}
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/* The click listener for ListView in the navigation drawer */
	private class DrawerItemClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			selectItem(position);

		}
	}

	private void selectItem(int position) {
		// update the main content by replacing fragments
		Fragment fragment = new VistaFragment();
		Bundle args = new Bundle();
		args.putInt(VistaFragment.ARG_ITEM_NUMBER, position);
		fragment.setArguments(args);

		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction().replace(R.id.contenedor, fragment)
				.commit();

		// update selected item and title, then close the drawer
		listaNavDraw.setItemChecked(position, true);
		setTitle(itemsNavDraw[position]);
		dondeDibujar.closeDrawer(listaNavDraw);

	}

	@Override
	public void setTitle(CharSequence title) {
		tituloDelMenu = title;
		getActionBar().setTitle(tituloDelMenu);
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		navDrawToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		navDrawToggle.onConfigurationChanged(newConfig);
	}

	/**
	 * Fragment that appears in el lienzo, shows a layout
	 */
	public class VistaFragment extends Fragment {
		public static final String ARG_ITEM_NUMBER = "item_number";

		public VistaFragment() {
			// Empty constructor required for fragment subclasses
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			int i = getArguments().getInt(ARG_ITEM_NUMBER);
			String seccion = getResources().getStringArray(R.array.items_array)[i];
			View rootView = null;
			switch (i) {
			case 0:
				rootView = inflater.inflate(R.layout.fragment_cursos_todos,
						container, false);

				break;
			case 1:
				rootView = inflater.inflate(R.layout.fragment_ultimo_curso,
						container, false);
				break;
			case 2:
				rootView = inflater.inflate(R.layout.fragment_ultimo_alumno,
						container, false);
				break;

			}

			getActivity().setTitle(seccion);

			return rootView;
		}
	}

	public class agregarCursoDialogo extends DialogFragment {
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// Use the Builder class for convenient dialog construction
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			LayoutInflater inflater = getActivity().getLayoutInflater();
			final View agregarCursoDialog = inflater.inflate(
					R.layout.add_dialog, null);

			builder.setView(agregarCursoDialog)
					.setMessage("Agregue Curso")
					.setPositiveButton("Aceptar",
							new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog,
										int id) {
									EditText nombre1 = (EditText) agregarCursoDialog
											.findViewById(R.id.input);
									String nombre = nombre1.getText()
											.toString();
									Curso curso = new Curso(nombre);
									boolean carga = addCurso(curso);
									System.out
											.println("---------------sadasdas--------asdas-d####_123123-12-3--a-ds-dsa-3-3-");

									refresh(0);
								}
							})
					.setNegativeButton("Cancelar",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									// User cancelled the dialog
								}
							});
			// Create the AlertDialog object and return it
			return builder.create();
		}
	}

	public class agregarAlumnoDialogo extends DialogFragment {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) { //
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			LayoutInflater inflater = getActivity().getLayoutInflater();
			final View agregarAlumnoDialog = inflater.inflate(
					R.layout.add_dialog, null);

			builder.setView(agregarAlumnoDialog)
					.setMessage("Agregue Alumno")
					.setPositiveButton("Aceptar",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									EditText nombre1 = (EditText) agregarAlumnoDialog
											.findViewById(R.id.input);
									String nombre = nombre1.getText()
											.toString();
									boolean carga = addAlumno(
											new Alumno(nombre), lastCourse);
									refresh(1);
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

	public class agregarNotaDialogo extends DialogFragment {
		int nota = 0;
		String anotacion;

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) { //
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			LayoutInflater inflater = getActivity().getLayoutInflater();
			final View agregarNotaDialog = inflater.inflate(
					R.layout.add_nota_dialog, null);
			final Spinner spinner = (Spinner) agregarNotaDialog
					.findViewById(R.id.nota);
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(
					agregarNotaDialog.getContext(),
					android.R.layout.simple_spinner_item, makeNotas());
			spinner.setAdapter(adapter);
			spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> adapterView,
						View view, int position, long id) {
					if (position == makeNotas().length - 1) {
						openSettings(true);

						syncProperties();
						for (int i = 0; i < Properties.getNotas().length; i++) {
							System.out.println(Properties.getNotas()[i] + "");
							System.out.println(notas[i] + "");
						}
						ArrayAdapter<String> adaptadorNuevo = new ArrayAdapter<String>(
								agregarNotaDialog.getContext(),
								android.R.layout.simple_spinner_item,
								makeNotas());
						spinner.setAdapter(adaptadorNuevo);
					} else {
						nota = notas[position];
						anotacion = observaciones[position];
					}

				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
					// TODO Auto-generated method stub

				}
			});
			builder.setView(agregarNotaDialog);
			builder.setMessage("Agregue Nota")
					.setPositiveButton("Aceptar",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									if (spinner.getSelectedItem() == null) {
										toast("null wacho");
									}

									boolean carga = addNota(new Nota(
											new Date(), nota, anotacion),
											lastAlumn, lastCourse);
									refresh(2);
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

	public class mostrarGraficoDialogo extends DialogFragment {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) { //
			Number[] serieDeNotas = new Number[lastAlumn.getHistorial()
					.getNotas().length];
			for (int i = 0; i < serieDeNotas.length; i++) {
				serieDeNotas[i] = lastAlumn.getHistorial().getNotas()[i]
						.getNota();
			}

			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			LayoutInflater inflater = getActivity().getLayoutInflater();
			final View mostrarGraficoDialog = inflater.inflate(
					R.layout.grafico, null);

			builder.setView(mostrarGraficoDialog)
					.setMessage("Gráfico de " + lastAlumn.getNombre())
					.setPositiveButton("Aceptar",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
								}
							})
					.setNegativeButton("Cancelar",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
								}
							});
			XYPlot grafico = (XYPlot) mostrarGraficoDialog
					.findViewById(R.id.grafico);
			cargarGrafico(grafico, serieDeNotas);
			return builder.create();
		}
	}
}
