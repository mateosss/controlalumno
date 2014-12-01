//vista de busqueda, implementa la clase logica buscador, 
//y se encarga de mostrar los resultados de buscador y de al
//seleccionarlos que se seleccionen en la main activity
package com.example.nav;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;

public class Search extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search);
		getActionBar().setDisplayHomeAsUpEnabled(false);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search, menu);
		getActionBar().setIcon(R.drawable.action_search);
		MenuItem searchItem = menu.findItem(R.id.action_search);
		SearchView mSearchView;
		mSearchView = (SearchView) searchItem.getActionView();
		mSearchView
				.setQueryHint(getResources().getString(R.string.search_hint));
		mSearchView.setOnQueryTextListener(new OnQueryTextListener() {

			@Override
			public boolean onQueryTextSubmit(String text) {
				return false;
			}

			@Override
			public boolean onQueryTextChange(String text) {
				Buscador search = new Buscador();
				Object[] resultados = search.buscar(Properties.getCursos(),
						text);
				ListView listaAlumnos = (ListView) findViewById(R.id.alumnosSearch);
				ListView listaCursos = (ListView) findViewById(R.id.cursosSearch);
				Curso[] cursosRes = (Curso[]) resultados[0];
				Alumno[] alumnosRes = (Alumno[]) resultados[1];
				final int[] posCursosRes = (int[]) resultados[2];
				final int[][] posAlumnosRes = (int[][]) resultados[3];
				String[] listaAlumnosString = new String[alumnosRes.length];
				String[] listaCursosString = new String[cursosRes.length];
				for (int i = 0; i < alumnosRes.length; i++) {
					listaAlumnosString[i] = getResources().getString(
							R.string.alumno_resultado)
							+ alumnosRes[i].getNombre();
				}
				for (int i = 0; i < cursosRes.length; i++) {
					listaCursosString[i] = getResources().getString(
							R.string.curso_resultado)
							+ cursosRes[i].getCurso();
				}
				ArrayAdapter<String> adaptadorAlumnos = new ArrayAdapter<String>(
						Search.this, android.R.layout.simple_list_item_1,
						listaAlumnosString);
				ArrayAdapter<String> adaptadorCursos = new ArrayAdapter<String>(
						Search.this, android.R.layout.simple_list_item_1,
						listaCursosString);
				listaAlumnos.setAdapter(adaptadorAlumnos);
				listaAlumnos
						.setOnItemClickListener(new AdapterView.OnItemClickListener() {

							@Override
							public void onItemClick(AdapterView<?> arg0,
									View arg1, int position, long arg3) {
								int posicionCurso = posAlumnosRes[position][0];
								int posicionAlumno = posAlumnosRes[position][1];
								Properties.setLastCourse(Properties.getCursos()[posicionCurso]);
								Properties
										.setLastAlumn(Properties
												.getLastCourse().getAlumnos()[posicionAlumno]);
								Properties.setFirsTime(true);
								Properties.setSeleccion(2);
								finish();
							}

						});
				listaCursos.setAdapter(adaptadorCursos);
				listaCursos
						.setOnItemClickListener(new AdapterView.OnItemClickListener() {

							@Override
							public void onItemClick(AdapterView<?> arg0,
									View arg1, int position, long arg3) {
								int posicionCurso = posCursosRes[position];
								Properties.setLastCourse(Properties.getCursos()[posicionCurso]);

								Properties.setFirsTime(true);
								Properties.setSeleccion(1);

								finish();
							}

						});

				return false;
			}
		});
		searchItem.expandActionView();
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
}
