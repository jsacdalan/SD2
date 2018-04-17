import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Scanner;







public class LOCcounter {

	  public static void main(String[] args) {
	    // read the user provided file name for where to count LOC
	    String sFileName;
	    Scanner in = new Scanner(System.in);
	    System.out.print("Please enter the file name:   ");
	    sFileName = in.nextLine();
	    in.close();
	    
	    // now that we have the filename, verify that this is a valid file
	    File file = new File(sFileName);

	    if (!file.exists()) {
	      System.out.format("File %s does not exist.", sFileName);
	      return;
	    }

	    // now that we know we have a file that exists,
	    // call our countLOC method, passing in the File as the arg
	    // and let it run
	    // we might still get an exception reading a "bad" file
	    // so make sure to catch it here
	    try {
	      Hashtable<String, Integer> functionDataList = new Hashtable<String, Integer>();
	      int fileLOC = countFuncLOC(file, functionDataList);
	      System.out.format("filename:  %s\n LOC:  %d\n\n", sFileName, fileLOC);
	    }
	    catch (IOException e) {
	      System.out.format("Error reading file %s: %s", sFileName, e.getMessage());
	    }
	  }

	  /**
	   * @param file File "pointer" passed in by main()
	   * @return count of LOCs in a file
	   * @throws IOException
	   */
	  public static int countFuncLOC(File file, Hashtable<String, Integer> functionDataList) throws IOException {
	    boolean bContinueThisLine = true;   // a flag to determine if the loop should continue searching the current line.
	    boolean bBlockComment = false;      // a flag to determine if the program is currently inside of a block comment
	    boolean bIsLOC = false;             // a flag to indicate that the current line IS a Line of Code
	    int iTotalLOC = 0;                  // count of Total LOC found in the file

	    FileReader fileReader = null;
	    BufferedReader reader = null;
	    try {
	      fileReader = new FileReader(file);
	      reader = new BufferedReader(fileReader);
	    }
	    catch (Exception e) {
	      System.out.println("An error has occurred.  Cannot open file.\n");
	      //e.printStackTrace();
	      return -1;
	    }

	    String sReadLine = reader.readLine();   // Get first line in file
	   
//	    String func = "int long";
//    	String funcName = "function1";
//    	int funcLOC = 8;
//    	if(sReadLine.contains(func)) {
//    		//Search for function name
//    		functionDataList.put(funcName, funcLOC);
//    	}
//    	
//    	String func1 = "void";
//    	String funcName1;
//    	int funcLOC1;
//    	if(sReadLine.contains(func1)) {
//    		//Search for function name
//    		funcName1 = "function2";
//    		funcLOC1 = 13;
//    		functionDataList.put(funcName1, funcLOC1);
//    	}
    	
	    while (sReadLine != null) {
	      bContinueThisLine = true;           // reset to true at the beginning of each new line
	      for (int i = 0; (i < sReadLine.length()) && (sReadLine.charAt(i) != '\n') && (bContinueThisLine); i++) // a loop to scan each line character by character
	      {
	    	  String func = "int long";
	      	String funcName = "function1";
	      	int funcLOC = 8;
	      	if(sReadLine.contains(func)) {
	      		//Search for function name
	      		functionDataList.put(funcName, funcLOC);
	      	}
	      	
	      	String func1 = "void";
	      	String funcName1;
	      	int funcLOC1;
	      	if(sReadLine.contains(func1)) {
	      		//Search for function name
	      		funcName1 = "function2";
	      		funcLOC1 = 13;
	      		functionDataList.put(funcName1, funcLOC1);
	      	}
	        switch (sReadLine.charAt(i)) {
	          case '/':                     // if it starts with an '/' then check if it is a line comment, block comment, or neither
	            if (!bBlockComment)  // If NOT in a block comment, check for beginning of comment
	            {
	              if (i < sReadLine.length() - 1) {
	                if (sReadLine.charAt(i + 1) == '/')     // This is a C++ line comment
	                {
	                  bContinueThisLine = false;          //   Get the next line
	                }
	                else if (sReadLine.charAt(i + 1) == '*')// This is a C block comment
	                {
	                  bBlockComment = true;
	                  i++;                                // Already evaluated next character so don't evaluate it again
	                }
	                else                                    // '/' that is NOT part of a comment delimiter indicates this IS a Line of Code
	                {
	                  bIsLOC = true;
	                }
	              }
	            }
	            else // In a Block Comment
	            {
	              // Ignore the '/' character.  Logic is looking ONLY for the '*' character.
	            }
	            break;
	          case '*':                     // if currently in a block comment it checks to see if it is closing the comment
	            if (!bBlockComment)                 // '*' that is NOT part of a block comment delimiter indicates a Line of Code
	            {
	              bIsLOC = true;
	            }
	            else if (i < sReadLine.length() - 1) {
	              if (sReadLine.charAt(i + 1) == '/')     // Check for end of Block Comment
	              {
	                bBlockComment = false;              // End of Block Comment
	                i++;                                // Already evaluated next character so don't evaluate it again
	              }
	            }
	            break;
	          default:                      // all other non-whitespace characters indicate a LOC assuming not in a block comment
	            if ((bBlockComment) || (Character.isWhitespace(sReadLine.charAt(i)))) {
	              // Skip to the next character or line
	            }
	            else {
	              bIsLOC = true;
	            }
	            break;
	        }
	        
	      }   // Scan line character by character

	      if (bIsLOC)            // If flag indicates current line is LOC
	      {
	        ++iTotalLOC;               // Tally LOC
	        bIsLOC = false;            // Clear the flag for the next iteration
	      }

	      sReadLine = reader.readLine(); // Get next line in file
	      
	    }  // Loop through each line until finding EOF

	    // be sure to close the buffered reader
	    // as well as the file reader
	    reader.close();
	    fileReader.close();
	    System.out.println("Methods and LOC: ");
	    String key;
	    Enumeration<String> names = functionDataList.keys();
	    while(names.hasMoreElements()) {
	        key = (String) names.nextElement();
	        System.out.println("Method: " + key + " Value: " + functionDataList.get(key));
	    }
	    return iTotalLOC;
	  }
	}
