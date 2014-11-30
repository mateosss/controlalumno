package com.example.nav;

//clase logica encargada de la busqueda, mandando strings devuelve 
//un array con los cursos y alumnos que coinciden con la busqueda,
//además de sus respectivas posiciones
import java.util.Locale;

public class Buscador {
	Object[] cursosYAlumnos = new Object[4];
	Curso[] cursosRet = new Curso[0];
	Alumno[] alumnosRet = new Alumno[0];
	int[] posCursos = new int[0];
	int[][] posAlumnos = new int[0][2];

	public Object[] buscar(Curso[] cursos, String busqueda) {
		String busMin = busqueda.toLowerCase(Locale.getDefault());
		for (int i = 0; i < cursos.length; i++) {
			if (cursos[i].getCurso().toLowerCase(Locale.getDefault())
					.startsWith(busMin)) {
				addCurso(cursos[i], i);
			}
			for (int j = 0; j < cursos[i].getAlumnos().length; j++) {

				if (cursos[i].getAlumnos()[j].getNombre()
						.toLowerCase(Locale.getDefault()).startsWith(busMin)) {
					addAlumno(cursos[i].getAlumnos()[j], i, j);
				}
			}
		}
		cursosYAlumnos[0] = cursosRet;
		cursosYAlumnos[1] = alumnosRet;
		cursosYAlumnos[2] = posCursos;
		cursosYAlumnos[3] = posAlumnos;

		return cursosYAlumnos;
	}

	public void addCurso(Curso curso, int pos) {
		Curso[] cursosAux = new Curso[cursosRet.length + 1];
		int[] posCursosAux = new int[posCursos.length + 1];
		for (int i = 0; i < cursosRet.length; i++) {
			cursosAux[i] = cursosRet[i];
			posCursosAux[i] = posCursosAux[i];
		}
		posCursosAux[posCursosAux.length - 1] = pos;
		cursosAux[cursosAux.length - 1] = curso;
		posCursos = posCursosAux;
		cursosRet = cursosAux;

	}

	public void addAlumno(Alumno alumno, int curso, int pos) {
		Alumno[] alumnosAux = new Alumno[alumnosRet.length + 1];
		int[][] posAlumnosAux = new int[alumnosRet.length + 1][2];
		for (int i = 0; i < alumnosRet.length; i++) {
			alumnosAux[i] = alumnosRet[i];
			posAlumnosAux[i][0] = posAlumnos[i][0];
			posAlumnosAux[i][1] = posAlumnos[i][1];
		}
		alumnosAux[alumnosAux.length - 1] = alumno;
		posAlumnosAux[posAlumnosAux.length - 1][0] = curso;
		posAlumnosAux[posAlumnosAux.length - 1][1] = pos;

		alumnosRet = alumnosAux;
		posAlumnos = posAlumnosAux;
	}

}
