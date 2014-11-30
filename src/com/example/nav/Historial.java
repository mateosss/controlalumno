//clase logica que se implementa para guardar notas en el alumno, y más adelante guardara notas del curso
package com.example.nav;

import java.io.Serializable;

public class Historial implements Serializable {
	private Nota[] notas = new Nota[0];

	public Historial() {
	}

	public Nota[] getNotas() {
		return notas;
	}

	public void setNotas(Nota[] notas) {
		this.notas = notas;
	}

	public void addNota(Nota nota) {
		Nota[] notasAux = new Nota[notas.length + 1];
		for (int i = 0; i < notas.length; i++) {
			notasAux[i] = notas[i];
		}
		nota.setId(notasAux.length - 1);
		notasAux[notasAux.length - 1] = nota;
		notas = notasAux;
	}

	public void deleteNota(Nota nota) {
		Nota[] notasAux = new Nota[notas.length - 1];
		int basico = 0;
		for (int i = 0; i < notas.length; i++) {
			if (nota.getId() == i) {
				notas[i] = null;
				basico++;
			} else {
				notas[i].setId(i - basico);
				notasAux[i - basico] = notas[i];

			}
		}
		notas = notasAux;
	}
}