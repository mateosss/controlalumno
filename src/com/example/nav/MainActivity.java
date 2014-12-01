//clase principal
package com.example.nav;

import java.util.Arrays;
import java.util.Date;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;

public class MainActivity extends FragmentActivity {
	ViewPager Tab;// Las tabs
	TabPagerAdapter TabAdapter;
	ActionBar actionBar;
	CharSequence tituloDelMenu;

	private Curso[] cursos = new Curso[0];// toda la informaciómn (todos los
											// cursos)
	private Curso lastCourse = null;// el ultimo curso seleccionado
	private Alumno lastAlumn = null;// el ultimo alumno seleccionado
	private String[] observaciones = { "Muy mal desempeño -",
			"Muy mal desempeño +", "Mal desempeño -", "Mal desempeño +",
			"Desempeño regular -", "Desempeño regular +", "Buen desempeño -",
			"Buen desempeño +", "Muy buen desempeño", "Excelente desempeño" };
	private int[] notas = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };// observaciones y
															// notas
															// predeterminada

	// metodo que se encarga de guardar las propiedades de la main activity en
	// la clase abstracta properties, si es necesario las guarda en disco
	public void saveProperties() {
		Properties.setCursos(cursos);
		Properties.setLastAlumn(lastAlumn);
		Properties.setLastCourse(lastCourse);
		Properties.setObservaciones(observaciones);
		Properties.setNotas(notas);

		if (Properties.isSave()) {
			Properties.save();
		}
	}

	// metodo que se encarga de cargar los valores de la clase properties a la
	// main activity
	public void syncProperties() {
		cursos = Properties.getCursos();
		lastCourse = Properties.getLastCourse();
		lastAlumn = Properties.getLastAlumn();
		observaciones = Properties.getObservaciones();
		notas = Properties.getNotas();
	}

	// metodo que crea un dialogo con el grafico
	public void mostrarGrafico(View view) {

		mostrarGraficoDialogo dialogo = new mostrarGraficoDialogo();
		FragmentManager fragmentManager = getFragmentManager();
		dialogo.show(fragmentManager, getResources()
				.getString(R.string.grafico));
	}

	// metodo que abre configuraciones, si es necesario abre el editor de notas
	public void openSettings(boolean openNota) {
		Intent intent = new Intent(this, Settings.class);
		if (openNota) {
			Properties.setOpenNota(true);
		}
		startActivity(intent);
	}

	// mezcla los atributos observaciones y notas en un solo array para poder
	// visualizarlo en el seleccionador de notas
	public String[] makeNotas() {
		String[] notasFinal = new String[notas.length + 1];
		for (int i = 0; i < notas.length; i++) {
			notasFinal[i] = notas[i]
					+ getResources().getString(R.string.separador)
					+ observaciones[i];
		}
		notasFinal[notasFinal.length - 1] = getResources().getString(
				R.string.editar_notas);
		return notasFinal;

	}

	// abre el dialogo para agregar un alumno
	public void addAlumnoEvent() {
		agregarAlumnoDialogo dialogo = new agregarAlumnoDialogo();
		FragmentManager fragmentManager = getFragmentManager();
		dialogo.show(fragmentManager,
				getResources().getString(R.string.agregar_alumno));
	}

	// abre el dialogo para agregar una nota
	public void addNotaEvent() {
		agregarNotaDialogo dialogo = new agregarNotaDialogo();
		FragmentManager fragmentManager = getFragmentManager();
		dialogo.show(fragmentManager,
				getResources().getString(R.string.agregar_nota));

	}

	// abre el dialogo para agregar un curso
	public void addCursoEvent() {
		agregarCursoDialogo dialogo = new agregarCursoDialogo();
		FragmentManager fragmentManager = getFragmentManager();
		dialogo.show(fragmentManager,
				getResources().getString(R.string.agregar_curso));

	}

	// metodo encargado de agregar un curso al array de cursos de esta clase
	public boolean addCurso(Curso curso) {
		Curso[] cursosAux = new Curso[cursos.length + 1];
		for (int i = 0; i < cursos.length; i++) {
			cursosAux[i] = cursos[i];
			if (curso.getCurso().equalsIgnoreCase(cursosAux[i].getCurso())) {
				toast(getResources().getString(R.string.error_curso_existente));
				return false;
			}
		}
		cursosAux[cursosAux.length - 1] = curso;
		cursos = cursosAux;
		toast(getResources().getString(R.string.agregado));
		return true;
	}

	// metodo encargado de agregar un alumno a un curso, mediante el uso de las
	// clases logicas
	public boolean addAlumno(Alumno alumno, Curso curso) {
		boolean cargar = false;
		for (int i = 0; i < cursos.length; i++) {
			if (cursos[i].getCurso().equalsIgnoreCase((curso.getCurso()))) {
				cargar = cursos[i].addAlumno(alumno);
				toast(getResources().getString(R.string.alumno_cargado));
				break;
			}
		}
		if (!cargar) {
			toast(getResources().getString(R.string.error_alumno_existente));
			return false;
		}
		return true;
	}

	// metodo encargado de agregar una nota a un alumno de un curso mediate las
	// clases logicas
	public boolean addNota(Nota nota, Alumno alumno, Curso curso) {

		for (int i = 0; i < cursos.length; i++) {
			if (cursos[i].getCurso().equalsIgnoreCase((curso.getCurso()))) {
				for (int j = 0; j < cursos[i].getAlumnos().length; j++) {
					if (cursos[i].getAlumnos()[j].getNombre().equalsIgnoreCase(
							alumno.getNombre())) {
						cursos[i].getAlumnos()[j].addNota(nota);
						toast(getResources().getString(R.string.nota_agregada));
						return true;
					} else {
						if (j == cursos[i].getAlumnos().length - 1) {
							toast(getResources().getString(
									R.string.no_se_encuentra_el_alumno)
									+ alumno.getNombre()
									+ getResources().getString(
											R.string.en_el_curso)
									+ curso.getCurso());
							return false;
						}
					}
				}
			}
		}
		toast(getResources().getString(R.string.no_se_encuentra_el_curso)
				+ curso.getCurso());
		return false;
	}

	// carga los valores del grafico
	public void cargarGrafico(XYPlot grafico, Number[] serie) {
		XYSeries series1 = new SimpleXYSeries(Arrays.asList(serie),
				SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, getResources()
						.getString(R.string.notas));

		LineAndPointFormatter series1Format = new LineAndPointFormatter(
				Color.rgb(255, 255, 255), Color.rgb(0, 153, 204),
				Color.alpha(0), null);
		grafico.destroyDrawingCache();
		grafico.setScrollContainer(false);
		grafico.clear();
		grafico.addSeries(series1, series1Format);

	}

	// el metodo central de la aplicacion se ejecuta muy seguido, se encarga de
	// refrescar la lista que sea necesaria (de cursos, alumnos o notas), y
	// darles los eventos del click y del click largo
	public void refresh(int tipo) {
		String[] listaString = null;
		ListView lista = null;

		if (Properties.isFirsTime()) {
			tipo = Properties.getSeleccion();
			Tab.setCurrentItem(tipo);
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
					lastCourse = cursos[position];

					Tab.setCurrentItem(1);

					getActionBar().setTitle(cursos[position].getCurso());
					Properties.setSave(true);
					refresh(1);
				}
			});
			lista.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
				@Override
				public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
						int position, long arg3) {
					toast(getResources().getString(R.string.eliminado)
							+ cursos[position].getCurso());

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
					Properties.setSave(true);
					refresh(0);
					return false;
				}

			});

		}

		else {
			if (tipo == 1) {
				if (lastCourse == null) {
					toast(getResources().getString(
							R.string.no_hay_curso_seleccionado));
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
							lastAlumn = lastCourse.getAlumnos()[position];
							Tab.setCurrentItem(2);
							getActionBar().setTitle(
									lastCourse.getAlumnos()[position]
											.getNombre());
							toast(lastCourse.getAlumnos()[position].getNombre());
							Properties.setSave(true);

							refresh(2);

						}
					});
					lista.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
						@Override
						public boolean onItemLongClick(AdapterView<?> arg0,
								View arg1, int position, long arg3) {
							toast(getResources().getString(R.string.eliminado)
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
									Properties.setSave(true);
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
						toast(getResources().getString(
								R.string.no_hay_alumno_seleccionado));
					} else {

						listaString = new String[lastAlumn.getHistorial()
								.getNotas().length];

						for (int i = 0; i < listaString.length; i++) {
							listaString[(listaString.length - 1) - i] = lastAlumn
									.getHistorial().getNotas()[i].getNota()
									+ getResources().getString(R.string.dia)
									+ lastAlumn.getHistorial().getNotas()[i]
											.getFecha()
									+ getResources().getString(
											R.string.observacion)
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
								Properties.setSave(true);
								refresh(2);
								return false;
							}

						});
						TextView nombre = (TextView) findViewById(R.id.alumno);
						TextView notamax = (TextView) findViewById(R.id.notamax);
						TextView notamin = (TextView) findViewById(R.id.notamin);
						TextView promedio = (TextView) findViewById(R.id.promedio);
						TextView estado = (TextView) findViewById(R.id.estado);
						ImageView estadoIM = (ImageView) findViewById(R.id.estadoIM);
						nombre.setText(lastAlumn.getNombre() + "");
						notamax.setText(getResources().getString(
								R.string.nota_max)
								+ lastAlumn.getNotaMax());
						notamin.setText(getResources().getString(
								R.string.nota_min)
								+ lastAlumn.getNotaMin());
						promedio.setText(getResources().getString(
								R.string.promedio)
								+ lastAlumn.getPromedio());

						if (lastAlumn.getEstado() == 5) {
							estado.setText(getResources().getString(
									R.string.estado)
									+ getResources().getString(
											R.string.excelente));
							estadoIM.setImageResource(R.drawable.ic_muy_bien);
						} else if (lastAlumn.getEstado() == 4) {
							estado.setText(getResources().getString(
									R.string.estado)
									+ getResources().getString(R.string.bien));
							estadoIM.setImageResource(R.drawable.ic_bien);
						} else if (lastAlumn.getEstado() == 3) {
							estado.setText(getResources().getString(
									R.string.estado)
									+ getResources()
											.getString(R.string.regular));
							estadoIM.setImageResource(R.drawable.ic_regular);
						} else if (lastAlumn.getEstado() == 2) {
							estado.setText(getResources().getString(
									R.string.estado)
									+ getResources().getString(R.string.mal));
							estadoIM.setImageResource(R.drawable.ic_mal);
						} else if (lastAlumn.getEstado() == 1) {
							estado.setText(getResources().getString(
									R.string.estado)
									+ getResources()
											.getString(R.string.muy_mal));
							estadoIM.setImageResource(R.drawable.ic_muy_malpng);
						}

					}
				}
			}

		}

		observaciones = Properties.getObservaciones();
		notas = Properties.getNotas();
		saveProperties();
	}

	// metodo para mostrar una notificacion toast
	public void toast(String texto) {
		Toast.makeText(this, texto, Toast.LENGTH_SHORT).show();
	}

	// metodo sobrescrito que se encarga de refrescar un tab cuando la
	// aplicacion vuelve luego de ser pausada
	@Override
	protected void onResume() {
		try {
			refresh(Tab.getCurrentItem());

		} catch (Exception e) {
			System.out.println("no podemos hacer el on resume refresh");
		}
		super.onResume();
	}

	// metodo sobrescrito de inicio de la aplicacion, aca se crean las tabs
	// ademas de cargar las propiedades en disco
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		Properties.load();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		TabAdapter = new TabPagerAdapter(getSupportFragmentManager());
		Tab = (ViewPager) findViewById(R.id.pager);
		Tab.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {

				actionBar = getActionBar();
				actionBar.setSelectedNavigationItem(position);
			}
		});
		Tab.setAdapter(TabAdapter);

		actionBar = getActionBar();
		// Enable Tabs on Action Bar
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		ActionBar.TabListener tabListener = new ActionBar.TabListener() {

			@Override
			public void onTabReselected(android.app.ActionBar.Tab tab,
					FragmentTransaction ft) {
				refresh(tab.getPosition());

			}

			@Override
			public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
				if (tab.getPosition() == 1 && lastCourse == null) {
					Tab.setCurrentItem(0);
					toast(getResources()
							.getString(R.string.seleccione_un_curso));
				} else {
					if (tab.getPosition() == 2 && lastAlumn == null) {
						Tab.setCurrentItem(0);
						toast(getResources().getString(
								R.string.seleccione_un_alumno));
					} else {
						try {
							refresh(tab.getPosition());

						} catch (Exception e) {
							System.out.println(getResources().getString(
									R.string.no_es_posible_refrescar));
						}
						Tab.setCurrentItem(tab.getPosition());
						if (Tab.getCurrentItem() == 0) {
							setTitle(getResources().getString(
									R.string.tus_cursos));
						}
						if (Tab.getCurrentItem() == 1) {
							setTitle(lastCourse.getCurso());
						}
						if (Tab.getCurrentItem() == 2) {
							setTitle(lastAlumn.getNombre());
						}
					}
				}

			}

			@Override
			public void onTabUnselected(android.app.ActionBar.Tab tab,
					FragmentTransaction ft) {
				// TODO Auto-generated method stub

			}
		};
		// Add New Tab
		actionBar.addTab(actionBar.newTab()
				.setText(getResources().getString(R.string.cursos))
				.setTabListener(tabListener));
		actionBar.addTab(actionBar.newTab()
				.setText(getResources().getString(R.string.ultimo_curso))
				.setTabListener(tabListener));
		actionBar.addTab(actionBar.newTab()
				.setText(getResources().getString(R.string.ultimo_alumno))
				.setTabListener(tabListener));

	}

	// metodo de creacion de la action bar
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);

		return super.onCreateOptionsMenu(menu);
	}

	/* Called whenever we call invalidateOptionsMenu() */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// Cuando abris o cerras el nav draw se esconde o aparece el icono de
		// search respectivamente
		if (Tab.getCurrentItem() == 0) {
			refresh(0);
		}
		if (Tab.getCurrentItem() == 1) {
			refresh(1);
		}
		if (Tab.getCurrentItem() == 2) {
			refresh(2);
		}
		return super.onPrepareOptionsMenu(menu);

	}

	// metodo que ocurre cuando se selecciona un item de la action bar, aquí se
	// dirigen los eventso de cada botón
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (Tab.getCurrentItem() == 0) {
			refresh(0);
		}
		if (Tab.getCurrentItem() == 1) {
			refresh(1);
		}
		if (Tab.getCurrentItem() == 2) {
			refresh(2);
		}
		switch (item.getItemId()) {
		case R.id.action_websearch:
			Intent intent = new Intent(this, Search.class);
			startActivity(intent);

			return true;
		case R.id.action_settings:
			openSettings(false);
			return true;

		case R.id.action_export:
			if (Tab.getCurrentItem() == 0) {
				toast(getResources().getString(R.string.exportando_curso)
						+ lastCourse.getCurso());
				PdfManager.crearPdfCurso(MainActivity.this, lastCourse);
				PdfManager
						.showPdfFile(lastCourse.getCurso(), MainActivity.this);

			}
			if (Tab.getCurrentItem() == 1) {
				toast(getResources().getString(R.string.exportando_curso)
						+ lastCourse.getCurso());
				PdfManager.crearPdfCurso(MainActivity.this, lastCourse);
				PdfManager
						.showPdfFile(lastCourse.getCurso(), MainActivity.this);

			}
			if (Tab.getCurrentItem() == 2) {
				toast(getResources().getString(R.string.exportando_alumno)
						+ lastAlumn.getNombre());
				System.out.println(lastAlumn.getNombre());
				PdfManager.crearPdfAlumno(MainActivity.this, lastCourse,
						lastAlumn);
				PdfManager
						.showPdfFile(lastAlumn.getNombre(), MainActivity.this);
			}
			return true;

		case R.id.action_mail:
			if (Tab.getCurrentItem() == 0) {
				toast(getResources().getString(R.string.exportando_curso)
						+ lastCourse.getCurso());
				PdfManager.crearPdfCurso(MainActivity.this, lastCourse);
				PdfManager.sendPdfByEmail(lastCourse.getCurso(),
						Properties.getMail(),
						getResources().getString(R.string.informe_de)
								+ lastCourse.getCurso(), MainActivity.this);
			}
			if (Tab.getCurrentItem() == 1) {
				toast(getResources().getString(R.string.exportando_curso)
						+ lastCourse.getCurso());
				PdfManager.crearPdfCurso(MainActivity.this, lastCourse);
				PdfManager.sendPdfByEmail(lastCourse.getCurso(),
						Properties.getMail(),
						getResources().getString(R.string.informe_de)
								+ lastCourse.getCurso(), MainActivity.this);
			}
			if (Tab.getCurrentItem() == 2) {
				toast(getResources().getString(R.string.exportando_alumno)
						+ lastAlumn.getNombre());
				PdfManager.crearPdfAlumno(MainActivity.this, lastCourse,
						lastAlumn);
				PdfManager.sendPdfByEmail(lastAlumn.getNombre(),
						Properties.getMail(),
						getResources().getString(R.string.informe_de)
								+ lastAlumn.getNombre(), MainActivity.this);
			}
			return true;
		case R.id.action_add:
			if (Tab.getCurrentItem() == 0) {
				addCursoEvent();
			}
			if (Tab.getCurrentItem() == 1) {
				addAlumnoEvent();
			}
			if (Tab.getCurrentItem() == 2) {
				addNotaEvent();
			}
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	// setea el título de la action bar
	@Override
	public void setTitle(CharSequence title) {
		tituloDelMenu = title;
		getActionBar().setTitle(tituloDelMenu);
	}

	// clases para crear dialogos, se setean los eventos y demás
	public class agregarCursoDialogo extends DialogFragment {
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// Use the Builder class for convenient dialog construction
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			LayoutInflater inflater = getActivity().getLayoutInflater();
			final View agregarCursoDialog = inflater.inflate(
					R.layout.add_dialog, null);

			builder.setView(agregarCursoDialog)
					.setMessage(
							getResources().getString(R.string.agregar_curso))
					.setPositiveButton(
							getResources().getString(R.string.aceptar),
							new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog,
										int id) {
									EditText nombre1 = (EditText) agregarCursoDialog
											.findViewById(R.id.input);
									String nombre = nombre1.getText()
											.toString();
									Curso curso = new Curso(nombre);
									boolean carga = addCurso(curso);
									Properties.setSave(true);
									refresh(0);
								}
							})
					.setNegativeButton(
							getResources().getString(R.string.cancelar),
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
					.setMessage(
							getResources().getString(R.string.agregar_curso))
					.setPositiveButton(
							getResources().getString(R.string.aceptar),
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									EditText nombre1 = (EditText) agregarAlumnoDialog
											.findViewById(R.id.input);
									String nombre = nombre1.getText()
											.toString();
									boolean carga = addAlumno(
											new Alumno(nombre), lastCourse);
									Properties.setSave(true);
									refresh(1);
								}
							})
					.setNegativeButton(
							getResources().getString(R.string.cancelar),
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
			builder.setMessage(getResources().getString(R.string.agregar_nota))
					.setPositiveButton(
							getResources().getString(R.string.aceptar),
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									if (spinner.getSelectedItem() == null) {
										toast(getResources()
												.getString(
														R.string.no_hay_nota_seleccionada));
									}

									boolean carga = addNota(new Nota(
											new Date(), nota, anotacion),
											lastAlumn, lastCourse);
									Properties.setSave(true);
									refresh(2);
								}
							})
					.setNegativeButton(
							getResources().getString(R.string.cancelar),
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
					.setMessage(
							getResources().getString(R.string.grafico_de)
									+ lastAlumn.getNombre())
					.setPositiveButton(
							getResources().getString(R.string.aceptar),
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
								}
							})
					.setNegativeButton(
							getResources().getString(R.string.cancelar),
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
