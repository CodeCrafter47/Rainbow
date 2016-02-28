package PluginExampleEvents;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Date;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.zip.GZIPOutputStream;


public class AsyncLog implements Runnable
{
	public static String PluginName = "PlayerActions";
	public static String filename = "AsyncLog" + File.separator + PluginName + ".log";
	public static ArrayBlockingQueue<String> pendingOutput = new ArrayBlockingQueue<String>(20000);
	static boolean closeThread = false;
	static boolean isClosed = false;
	
	public static void StartAsyncLog()
	{
		System.out.println("[AsyncLog-" + PluginName + "] Started.");
		EnsureDirectory("AsyncLog");

		(new Thread(new AsyncLog())).start();
		closeThread = false;
		isClosed = false;
	}
	
	public static void EndAsyncLog()
	{
		closeThread = true;
		for(int i=0; i<20; i++)
		{
			if(isClosed)
			{
				//System.out.println("[AsyncLog-" + PluginName + "] Gracefully shutdown.");
				break;
			}
			try
			{
				Thread.sleep(250);
			}
			catch(Exception exc)
			{
				exc.printStackTrace();
			}
		}
	}
	
	public static String GetLogPrefix()
	{
		Date dt = new Date();

		String dtStr = String.format("%d/%d/%4d", dt.getMonth()+1, dt.getDate(), dt.getYear()+1900);

		int hr = dt.getHours();
		int min = dt.getMinutes();
		int sec = dt.getSeconds();

		String tmStr = "";
		if(hr < 12) tmStr = String.format("%02d:%02d:%02dam", hr == 0 ? 12 : hr, min, sec);
		else 
		{
			hr -= 12;
			tmStr = String.format("%02d:%02d:%02dpm", hr == 0 ? 12 : hr, min, sec);
		}
		
		return dtStr + " " + tmStr + " ";
	}
	
	static int failCount = 0;
	
	public static void Log(String msg)
	{
		if(msg == null) return;
		if(msg.length() <= 0) return;
		try
		{
			pendingOutput.add(GetLogPrefix() + msg);
		}
		catch(Exception exc)
		{
			failCount++;
			if(failCount % 20 == 1)
			{
				System.out.println("AsyncLog-" + PluginName + " Failure: " + msg);
			}
		}
			
	}

	public static void EnsureDirectory(String dirName)
	{
		File pDir = new File(dirName);
		if(pDir.isDirectory()) return;

		try
		{
			System.out.println("Creating directory: " + dirName);
			pDir.mkdir();
		}
		catch(Throwable exc)
		{
			System.out.println("EnsureDirectory " + dirName + ": " + exc.toString());
		}		
	}

	@Override
	public void run() 
	{
		try
		{
			for(int iter=1;;iter++)
			{
				// Sleep phase (sleep 1 second for a bit...)
				for(int idxSleep=0;idxSleep<71; idxSleep++)
				{
					if(closeThread) break; 
					Thread.sleep(1000);
				}

				// File write phase...
				int nLines = 0;
				String msg = pendingOutput.poll();
				if(msg != null)
				{
					long ms1 = System.currentTimeMillis();
					File f = new File(filename);
					BufferedWriter bw = new BufferedWriter(new FileWriter(f, true));
					
					while(msg != null)
					{
						nLines++;
						bw.write(msg + "\r\n");
						msg = pendingOutput.poll();
					}
					bw.close();

					long ms2 = System.currentTimeMillis();
					
					System.out.println(String.format("[AsyncLog-%s] %d lines to %s -- Took %d ms", PluginName, nLines, filename, ms2-ms1));
					
				}

				if(closeThread) break; 
			}
		}
		catch(Throwable exc)
		{
			exc.printStackTrace();
		}

		isClosed = true;		
	}	
}
