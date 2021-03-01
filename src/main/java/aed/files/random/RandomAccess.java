package aed.files.random;

import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.Scanner;

public class RandomAccess {

	private static final int TEAM_BYTES = 126;
	private static final int EARNED_CUPS_BYTES = 117;

	static Scanner sc = new Scanner(System.in);
	static RandomAccessFile file = null;

	public static void main(String[] args) throws FileNotFoundException {

		try {
			file = new RandomAccessFile("/users/adexuz/desktop/datos.dat", "rw");

			insertTeamData(file, 1, "Fútbol Club Barcelona", "LFP", "Barcelona, Spain", 79, true);
			insertTeamData(file, 2, "Real Madrid Fútbol Club", "LFP", "Madrid, Spain", 102, true);
			insertTeamData(file, 3, "Club Atlético de Madrid", "LFP", "Madrid, Spain", 32, true);
			insertTeamData(file, 4, "Club Deportivo Tenerife", "LFP", "Canary Islands, Spain", 0, false);
			insertTeamData(file, 5, "Unión Deportiva Las Palmas", "LFP", "Canary Islands, Spain", 0, false);

			// showData();
			// showTeam(5);
			modifyCups(5, 9);
			
			
		} catch (FileNotFoundException ex) {
			System.out.println(ex.getMessage());
		} finally {
			try {
				if (file != null) {
					file.close();
				}
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
		}

	}

	private static void insertTeamData(RandomAccessFile file, int codigoEquipo, String nombreEquipo, String codigoLiga,
			String localidad, int numeroCopasGanadas, boolean internacional) {

		try {
			file.writeInt(codigoEquipo);
			file.writeChar(',');

			file.writeBytes(getFilledString(nombreEquipo, 40));
			file.writeChar(',');

			file.writeBytes(getFilledString(codigoLiga, 5));
			file.writeChar(',');

			file.writeBytes(getFilledString(localidad, 60));
			file.writeChar(',');

			file.writeInt(numeroCopasGanadas);
			file.writeChar(',');

			file.writeBoolean(internacional);
			file.writeChar(',');
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static String getFilledString(String ogStr, int length) {
		char[] newString = new char[length];
		Arrays.fill(newString, '\0');

		if (ogStr.length() <= length) {
			for (int i = 0; i < ogStr.length(); i++) {
				newString[i] = ogStr.charAt(i);
			}
		} else {
			for (int i = 0; i < length; i++) {
				newString[i] = ogStr.charAt(i);
			}
		}

		return new String(newString);
	}

	public static void showData() {
		try {
			file.seek(0); // nos situamos al principio
			while (true) {

				byte[] nombreEquipoBytes = new byte[40];
				byte[] codigoLigaBytes = new byte[5];
				byte[] localidadBytes = new byte[60];

				System.out.println(file.readInt());
				System.out.println(file.readChar());

				file.read(nombreEquipoBytes);
				System.out.println(new String(nombreEquipoBytes));
				System.out.println(file.readChar());

				file.read(codigoLigaBytes);
				System.out.println(new String(codigoLigaBytes));
				System.out.println(file.readChar());

				file.read(localidadBytes);
				System.out.println(new String(localidadBytes));
				System.out.println(file.readChar());

				System.out.println(file.readInt());
				System.out.println(file.readChar());

				System.out.println(file.readBoolean());
				System.out.println(file.readChar());

			}
		} catch (EOFException e) {
			System.out.println("Fin de fichero");
		} catch (IOException ex) {
			System.out.println(ex.getMessage());
		}
	}

	public static void showTeam(int codTeam) {
		if (codTeam > 0) {
			try {

				file.seek((codTeam - 1) * TEAM_BYTES);

				byte[] nombreEquipoBytes = new byte[40];
				byte[] codigoLigaBytes = new byte[5];
				byte[] localidadBytes = new byte[60];

				System.out.println(file.readInt());
				System.out.println(file.readChar());

				file.read(nombreEquipoBytes);
				System.out.println(new String(nombreEquipoBytes));
				System.out.println(file.readChar());

				file.read(codigoLigaBytes);
				System.out.println(new String(codigoLigaBytes));
				System.out.println(file.readChar());

				file.read(localidadBytes);
				System.out.println(new String(localidadBytes));
				System.out.println(file.readChar());

				System.out.println(file.readInt());
				System.out.println(file.readChar());

				System.out.println(file.readBoolean());
				System.out.println(file.readChar());

			} catch (IOException ex) {
				ex.printStackTrace();
			}

		}
	}
	
	public static void modifyCups(int codTeam, int newEarnedCups) {
		try {
			int codTeamOffset = (codTeam - 1) * TEAM_BYTES;
			int earnedCupsOffset = codTeamOffset + EARNED_CUPS_BYTES;
			
			file.seek(earnedCupsOffset);
			file.writeInt(newEarnedCups);

			showTeam(codTeam);
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
