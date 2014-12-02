//función copiada y pegada de ITSaChat, se encarga de guardar/cargar un array de objetos
package com.example.nav;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import android.os.Environment;

public abstract class SaveLoad {

	private static String archivo = "config.cfg";

	public static void save(Object[] datos) throws IOException {

		String tarjeta = Environment.getExternalStorageDirectory()
				+ "/MisCursos";
		File carpeta = new File(tarjeta + "/");
		if (!carpeta.exists()) {
			carpeta.mkdir();
		}
		File file = new File(tarjeta + "/" + archivo);
		System.out.println(tarjeta + "/" + archivo);
		ObjectOutputStream salida = new ObjectOutputStream(
				new FileOutputStream(tarjeta + "/" + archivo));
		salida.writeObject(datos);
		salida.flush();
		salida.close();
	}

	public static Object[] load() throws IOException, ClassNotFoundException {
		try {
			String tarjeta = Environment.getExternalStorageDirectory()
					+ "/MisCursos";
			System.out.println(tarjeta + "/" + archivo);

			ObjectInputStream entrada = new ObjectInputStream(
					new FileInputStream(tarjeta + "/" + archivo));

			Object[] datos = (Object[]) entrada.readObject();
			entrada.close();
			return datos;
		} catch (FileNotFoundException e) {
			System.out
					.println("Archivo de configuración inexistente, cargando valores predeterminados");
		}
		return null;
	}

	public static String getArchivo() {
		return archivo;
	}

	public static void setArchivo(String archivo) {
		SaveLoad.archivo = archivo;
	}

}
