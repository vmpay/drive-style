package eu.vmpay.drivestyle.utils;

import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Date;
import java.util.List;

/**
 * Created by andrew on 11/6/17.
 */

public class ExportUtils
{
	private static final String TAG = "ExportUtils";

	private static final File PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
	private static final String SEPARATOR = "/";
	private static final String EXTENSION = ".csv";
	private static final CharSequence[] ILLEGAL_CHARACTERS = { "/", "\n", "\r", "\t", "\0", "\f",
			"`", "?", "*", "\\", "<", ">", "|", "\"", ":" };

	/**
	 * Exports the {@param list} to the PATH with the {@param fileName}.
	 * Throws {@link IOException} if cannot create the file
	 *
	 * @param fileName name of the file to export
	 * @param list     list of String arrays that contains data for export
	 * @throws IOException
	 */
	public static void exportToCsv(@Nullable String fileName, @NonNull List<String[]> list) throws IOException
	{
		if(list.isEmpty())
		{
			Log.d(TAG, "The list is empty");
			return;
		}
		if(fileName == null)
		{
			fileName = new Date(System.currentTimeMillis()).toString();
		}
		Writer writer = new FileWriter(PATH + SEPARATOR + fileName + EXTENSION);
		CSVWriter csvWriter = new CSVWriter(writer);
		csvWriter.writeAll(list);
		writer.close();
		Log.d(TAG, "Exported successfully to " + PATH + SEPARATOR + fileName + EXTENSION);
	}

	/**
	 * Checks if {@param fileName} valid
	 *
	 * @param filename
	 * @return
	 */
	public static boolean isFileNameValid(@Nullable String filename)
	{
		if(filename == null)
		{
			return false;
		}
		for(CharSequence entry : ILLEGAL_CHARACTERS)
		{
			if(filename.contains(entry))
			{
				return false;
			}
		}
		return true;
	}
}
