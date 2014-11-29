package com.example.nav;

import java.io.IOException;

public abstract class Properties {

	static public Curso[] cursos = new Curso[0];
	static public Curso lastCourse = null;
	static public Alumno lastAlumn = null;
	static private String[] observaciones = {
			"Consume Drogas ilegales en clase",
			"No Consume Drogas ilegales en clase",
			"Le pega un \"Tubaso\" a su compañero, y no estoy hablando de una llamada",
			"Le gusta rengar", "Dice \" Hola\" reiteradamente", "Flatulencia",
			"Tira un monitor a la cabeza de su compañero",
			"Agrega el prefijo perr , ej: \"perranchez\"",
			"Agrega el prefijo chucho, ej: \"chucholas\"",
			"Agrega el sufijo ossio , ej: \"Sanchossio\"" };
	static private int[] notas = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
	static public String mail = "mateodemayo@gmail.com";
	static int seleccion = 0;
	static public boolean firsTime = true;
	static boolean openNota = false;

	public static int getSeleccion() {
		return seleccion;
	}

	public static void setSeleccion(int seleccion) {
		Properties.seleccion = seleccion;
	}

	public static boolean isFirsTime() {
		return firsTime;
	}

	public static void setFirsTime(boolean firsTime) {
		Properties.firsTime = firsTime;
	}

	public static boolean isOpenNota() {
		return openNota;
	}

	public static void setOpenNota(boolean openNota) {
		Properties.openNota = openNota;
	}

	public static Curso[] getCursos() {
		return cursos;
	}

	public static void setCursos(Curso[] cursos) {
		Properties.cursos = cursos;
	}

	public static Curso getLastCourse() {
		return lastCourse;
	}

	public static void setLastCourse(Curso lastCourse) {
		Properties.lastCourse = lastCourse;
	}

	public static Alumno getLastAlumn() {
		return lastAlumn;
	}

	public static void setLastAlumn(Alumno lastAlumn) {
		Properties.lastAlumn = lastAlumn;
	}

	public static String[] getObservaciones() {
		return observaciones;
	}

	public static void setObservaciones(String[] observaciones) {
		Properties.observaciones = observaciones;
	}

	public static int[] getNotas() {
		return notas;
	}

	public static void setNotas(int[] notas) {
		Properties.notas = notas;
	}

	public static String getMail() {
		return mail;
	}

	public static void setMail(String mail) {
		Properties.mail = mail;
	}

	public static void save() {
		System.out.println("TRATANDO DE GUARDAR");
		Object[] datos = new Object[6];
		datos[0] = cursos;
		datos[1] = lastCourse;
		datos[2] = lastAlumn;
		datos[3] = observaciones;
		datos[4] = notas;
		datos[5] = mail;
		try {

			SaveLoad.save(datos);
			System.out.println("CREO QUE GUARDO");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void load() {
		System.out.println("TRATANDO DE CARGAR");
		try {
			Object[] datos = SaveLoad.load();
			if (datos != null) {
				System.out.println("NO ES NULL VAMO CARAJO");
				cursos = (Curso[]) datos[0];
				lastCourse = (Curso) datos[1];
				lastAlumn = (Alumno) datos[2];
				observaciones = (String[]) datos[3];
				notas = (int[]) datos[4];
				mail = (String) datos[5];
			}

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
