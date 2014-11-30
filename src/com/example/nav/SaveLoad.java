//función copiada y pegada de ITSaChat, se encarga de guardar/cargar un array de objetos
package com.example.nav;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import android.os.Environment;

public abstract class SaveLoad {

	private static String archivo = "config.cfg";

	public static void save(Object[] datos) throws IOException {

		File tarjeta = Environment.getExternalStorageDirectory();
		File file = new File(tarjeta.getAbsolutePath() + "/" + archivo);
		System.out.println(tarjeta.getAbsolutePath());
		ObjectOutputStream salida = new ObjectOutputStream(
				new FileOutputStream(tarjeta.getAbsolutePath() + "/" + archivo));
		salida.writeObject(datos);
		salida.flush();
		salida.close();
	}

	public static Object[] load() throws IOException, ClassNotFoundException {
		try {
			File tarjeta = Environment.getExternalStorageDirectory();
			ObjectInputStream entrada = new ObjectInputStream(
					new FileInputStream(tarjeta.getAbsolutePath() + "/"
							+ archivo));
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
