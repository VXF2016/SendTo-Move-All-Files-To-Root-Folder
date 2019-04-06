package com.vxf.tool.Windows.SendTo.app.move_all_files_to_root_folder;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;

import static java.nio.file.LinkOption.NOFOLLOW_LINKS;

public class Main
{
	public static void main(String[] args)
	{
		try(BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(System.in)))
		{
			System.out.println("file or folder selected:");
			StringBuilder stringBuilder = new StringBuilder();
			for (String path : args)
			{
				stringBuilder.append('\t');
				stringBuilder.append(path);
				stringBuilder.append('\n');
			}
			System.out.println(stringBuilder.toString());
			File folder_out=new File(".").getCanonicalFile();
			System.out.println("to folder: " + folder_out);
			int code;
			code=until_yes_or_no_or_exit(bufferedReader);
			if(code==1)
			{
				String[][] comparator={
						{"c","C","copy"},//0
						{"m","M","move"},
						{"d","D","delete"},
						{"q","Q","quit"}//3
				};
				String help="Choose Mode: \n"+make_help_by_comparator(comparator);
				code=until_compared(help,comparator,bufferedReader);
				if(code==0)
				{
					final Traversal_Action Traversal_Action_copy=new Traversal_Action(folder_out) {
						@Override
						public void action(File file) throws IOException
						{
							Files.copy(file.toPath(),new File(folder_out,file.getName()).toPath(),NOFOLLOW_LINKS);
						}
					};
					for (String file_or_folder_name: args)
					{
						File file_or_folder=new File(file_or_folder_name);
						if(file_or_folder.isDirectory())
						{
							try
							{
								traverse_a_folder_action_when_file(new File(file_or_folder_name), Traversal_Action_copy);
							}
							catch (IOException e)
							{
								System.err.println("Failed at: " + file_or_folder_name);
							}
						}
					}
					System.out.println("send any line for quit");
					bufferedReader.readLine();
				}
				else if(code==1)
				{
					final Traversal_Action Traversal_Action_move=new Traversal_Action(folder_out) {
						@Override
						public void action(File file) throws IOException
						{
							if(!	file.renameTo(new File(folder_out,file.getName()))	)
							{
								throw new IOException("move failed");
							}
						}
					};
					for (String file_or_folder_name: args)
					{
						File file_or_folder=new File(file_or_folder_name);
						if(file_or_folder.isDirectory())
						{
							try
							{
								traverse_a_folder_action_when_file(new File(file_or_folder_name), Traversal_Action_move);
							}
							catch (IOException e)
							{
								System.err.println("Failed at: " + file_or_folder_name);
							}
						}
					}
					System.out.println("send any line for quit");
					bufferedReader.readLine();
				}
				else if(code==2)
				{
					final Traversal_Action Traversal_Action_delete=new Traversal_Action(folder_out) {
						@Override
						public void action(File file) throws IOException
						{
							if(!	file.delete()	)
							{
								throw new IOException("delete failed");
							}
						}
					};
					for (String file_or_folder_name: args)
					{
						File file_or_folder=new File(file_or_folder_name);
						if(file_or_folder.isDirectory())
						{
							try
							{
								traverse_a_folder_action_when_file(new File(file_or_folder_name), Traversal_Action_delete);
							}
							catch (IOException e)
							{
								System.err.println("Failed at: " + file_or_folder_name);
							}
						}
					}
					System.out.println("send any line for quit");
					bufferedReader.readLine();
				}
				else if(code==3)//quit
				{
					//do noting for quit
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			try
			{
				System.out.println("send any line for quit");
				System.in.read();
			}
			catch (IOException ex)
			{
				ex.printStackTrace();
			}
		}

	}
	public static int yes_or_no_or_quit(BufferedReader bufferedReader) throws IOException, NullPointerException
	{
		//yes=1, no=0, not yes or no=-1.ex quit=2
		String line=bufferedReader.readLine();
		if(line==null)
		{
			throw new NullPointerException();
		}
		else
		{
			if(line.length()==1)
			{
				if("y".equals(line) | "Y".equals(line))
				{
					return 1;
				}
				else if ("n".equals(line) | "N".equals(line))
				{
					return 0;
				}
				else if ("q".equals(line) | "Q".equals(line))
				{
					return 2;
				}
				else return -1;
			}
			else
			{
				return -1;
			}
		}

	}
	public static int until_yes_or_no_or_exit(BufferedReader bufferedReader) throws IOException, NullPointerException
	{
		int a;
		do
		{
			System.out.print("y(Y) or n(N) or q(Q): ");
			a=yes_or_no_or_quit(bufferedReader);
			if(a==0|a==1|a==2)
			{
				return a;
			}
			else System.err.println("Error Input: "+a);
		}while (true);
	}
	public static int read_a_line_and_compare(String[][] comparator, BufferedReader bufferedReader) throws IOException, NullPointerException
	{
		String line=bufferedReader.readLine();
		if(line==null)
		{
			throw new NullPointerException();
		}
		else
		{
			for (int i=0;i<comparator.length;i++)
			{
				for(int j=0;j<comparator[i].length;j++)
				{
					if(line.equals(comparator[i][j]))
					{
						return i;
					}
				}
			}
			return -1;
		}

	}
	public static String make_help_by_comparator(String[][] comparator)
	{
		StringBuilder stringBuilder=new StringBuilder();
		for (int i=0;;i++)
		{
			for(int j=0;;j++)
			{
				stringBuilder.append(comparator[i][j]);
				if((j+1)==comparator[i].length)
				{
					break;
				}
				else
				{
					stringBuilder.append('|');
				}
			}
			if((i+1)==comparator.length)
			{
				break;
			}
			else
			{
				stringBuilder.append('\t');
			}
		}
		return stringBuilder.toString();
	}
	public static int until_compared(String help, String[][] comparator, BufferedReader bufferedReader) throws Exception
	{
		int a;
		do
		{
			System.out.println(help);
			a=read_a_line_and_compare(comparator, bufferedReader);
			if(a==-1)
			{
				System.err.println("Error Input: "+a);
			}
			else if(a>=comparator.length)
			{
				throw new Exception("Function Error: read_a_line_and_compare");
			}
			else
			{
				return a;
			}
		}while (true);
	}
	public static void traverse_a_folder_action_when_file(File file_or_folder, Traversal_Action traversal_action) throws IOException
	{
		if(file_or_folder.isFile())
		{
			traversal_action.action(file_or_folder);
		}
		else
		{
			if(file_or_folder.isDirectory())
			{
				File[] file_or_folders=file_or_folder.listFiles();
				if(file_or_folders!=null)
				{
					for(int i=0;i<file_or_folders.length;i++)
					{
						traverse_a_folder_action_when_file(file_or_folders[i], traversal_action);
					}
				}
			}
		}

	}
	public static abstract class Traversal_Action
	{
		File folder_out;
		public Traversal_Action(File folder_out)
		{
			this.folder_out=folder_out;
		}
		abstract public void action(File file) throws IOException;
	}
}
