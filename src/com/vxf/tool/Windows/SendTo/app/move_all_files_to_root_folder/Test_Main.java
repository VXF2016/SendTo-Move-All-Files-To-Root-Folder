package com.vxf.tool.Windows.SendTo.app.move_all_files_to_root_folder;

import static org.junit.jupiter.api.Assertions.*;

class Test_Main
{

	@org.junit.jupiter.api.Test
	void make_help_by_comparator()
	{
		String[][] comparator={
				{"s","S","string"},
				{"a","A","apple"},
		};
		String result="s|S|string\ta|A|apple";
		String real_result=Main.make_help_by_comparator(comparator);
		if(real_result.equals(result))
		{
			System.out.println("pass");
		}
		else
		{
			System.err.println("real_result: "+real_result);
			throw new org.junit.platform.commons.JUnitException("don't pass");
		}
	}
}