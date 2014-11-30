//clase logica encargada de manejar los datos de un objeto alumno
package com.example.nav;

import java.io.Serializable;

public class Alumno implements Serializable{

	private int estado;
	private String nombre;
	private int notaMaxima;
	private int notaMinima;
	private float promedio;
	Historial historial = new Historial();

	public int getNotaMaxima() {
		return notaMaxima;
	}

	public void setNotaMaxima(int notaMaxima) {
		this.notaMaxima = notaMaxima;
	}

	public int getNotaMinima() {
		return notaMinima;
	}

	public void setNotaMinima(int notaMinima) {
		this.notaMinima = notaMinima;
	}

	public Historial getHistorial() {
		return historial;
	}

	public void setHistorial(Historial historial) {
		this.historial = historial;
	}

	public Alumno(String nombre) {
		this.nombre = nombre;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public int getNotaMax() {
		return notaMaxima;
	}

	public void setNotaMax(int notaMax) {
		this.notaMaxima = notaMax;
	}

	public int getNotaMin() {
		return notaMinima;
	}

	public void setNotaMin(int notaMin) {
		this.notaMinima = notaMin;
	}

	public float getPromedio() {
		return promedio;
	}

	public void setPromedio(float promedio) {
		this.promedio = promedio;
	}

	public int getEstado() {
		return estado;
	}

	public void setEstado(int estado) {
		this.estado = estado;
	}

	public boolean hasNota(Nota nota) {
		for (int i = 0; i < historial.getNotas().length; i++) {
			if (historial.getNotas()[i].toString().equalsIgnoreCase(
					nota.toString())) {
				return true;
			}
		}
		return false;

	}

	public void refresh() {
		System.out.println("######REFRESCANDO ALUMNO");
		Nota[] notas = historial.getNotas();
		float prom;
		float promAux = 0;
		int nMx = 0;
		int nMn = 0;
		for (int i = 0; i < notas.length; i++) {
			promAux += notas[i].getNota();
			if (i == 0) {
				nMx = notas[i].getNota();
				nMn = notas[i].getNota();
			} else {
				if (notas[i].getNota() > nMx) {
					nMx = notas[i].getNota();
				}
				if (notas[i].getNota() < nMn) {
					nMn = notas[i].getNota();
				}
			}
		}
		prom = promAux / notas.length;

		this.redondearProm(prom);
		promedio = prom;
		notaMaxima = nMx;
		notaMinima = nMn;
	}

	public void redondearProm(float prom) {

		if (prom >= 1 & prom <= 2.4) {
			estado = 1;
		} else {
			if (prom >= 2.5 & prom <= 4.4) {
				estado = 2;
			} else {
				if (prom >= 4.5 & prom <= 6.4) {
					estado = 3;
				} else {
					if (prom >= 6.5 & prom <= 8.4) {
						estado = 4;
					} else {
						if (prom >= 8.5) {
							estado = 5;
						}else {
							estado = 0;
						}
					}
				}
			}
		}
	}

	public void addNota(Nota nota) {
		historial.addNota(nota);
		this.refresh();
	}

	public void deleteNota(Nota nota) {
		historial.deleteNota(nota);
		this.refresh();
	}

	@Override
	public String toString() {
		return "\nNombre: " + nombre + "\n\tNota maxima: " + notaMaxima
				+ "\n\tNota minima: " + notaMinima + "\n\tPromedio: "
				+ promedio + "\n\tEstado: " + estado;
	}
}