//clase logica que maneja los datos del objeto curso
package com.example.nav;

import java.io.Serializable;

public class Curso implements Serializable{
	private Alumno[] alumnos = new Alumno[0];
	Historial historial = new Historial();
	private String curso;
	int estadoGral;
	float promGral = 0;
	
	public Curso(String curso) {
		this.curso = curso;
	}

	public void promedioGeneral() {
		float prom;
		int promAux = 0;
		for (int i = 0; i < alumnos.length; i++) {
			promAux += alumnos[i].getPromedio();
		}
		prom = promAux / alumnos.length;

		promGral = prom;
		this.redondearPromGral(prom);

	}

	public boolean addAlumno(Alumno alumno) {

		Alumno[] alumnosAux = new Alumno[alumnos.length + 1];
		for (int i = 0; i < alumnos.length; i++) {
			if (alumno.getNombre().equalsIgnoreCase(alumnos[i].getNombre())) {
				return false;
			}
			alumnosAux[i] = alumnos[i];
		}
		alumnosAux[alumnosAux.length - 1] = alumno;
		alumnos = alumnosAux;
		return true;
	}

	public void deleteAlumno(String nombresillo) {
		
		Alumno[] alumnoAux = new Alumno[alumnos.length - 1];
		int borrar = 0;
		for (int i = 0; i < alumnos.length; i++) {
			if (alumnos[i].getNombre().equalsIgnoreCase(nombresillo)) {
				borrar++;
			} else {
				alumnoAux[i - borrar] = alumnos[i];
			}
		}
		alumnos = alumnoAux;

	}

	public boolean hasAlumno(Alumno alumno){
		for (int i = 0; i < alumnos.length; i++) {
			if (alumnos[i].getNombre().equalsIgnoreCase(alumno.getNombre())) {
				return true;
			}
		}
		return false;
		
	}
	public void redondearPromGral(float promgral) {
		if (promgral >= 1 & promgral <= 2.4) {
			estadoGral = 1;
		}
		if (promgral >= 2.5 & promgral <= 4.4) {
			estadoGral = 2;
		}
		if (promgral >= 4.5 & promgral <= 6.4) {
			estadoGral = 3;
		}
		if (promgral >= 6.5 & promgral <= 8.4) {
			estadoGral = 4;
		}
		if (promgral >= 8.5) {
			estadoGral = 5;
		}
	}

	public Alumno[] getAlumnos() {
		return alumnos;
	}

	public void setAlumnos(Alumno[] alumnos) {
		this.alumnos = alumnos;
	}
	public String[] getNombres(){
		String[] aux = new String[alumnos.length];
		for (int i = 0; i < aux.length; i++) {
			aux[i] = alumnos[i].getNombre();
		}
		return aux;
		
	}
	public Historial getHistorial() {
		return historial;
	}

	public void setHistorial(Historial historial) {
		this.historial = historial;
	}

	public String getCurso() {
		return curso;
	}

	public void setCurso(String curso) {
		this.curso = curso;
	}

	public int getEstadoGral() {
		return estadoGral;
	}

	public void setEstadoGral(int estadoGral) {
		this.estadoGral = estadoGral;
	}

	public float getPromGral() {
		return promGral;
	}

	public void setPromGral(float promGral) {
		this.promGral = promGral;
	}

	@Override
	public String toString() {
		String stringAux = "Curso: " + curso + "\n\tPromedio general:"
				+ promGral + "\n\tEstado " + estadoGral + "\n\n";
		for (int i = 0; i < alumnos.length; i++) {
			stringAux += alumnos[i].toString();
		}
		return stringAux;
	}

}