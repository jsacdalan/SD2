import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
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
	      int fileLOC = countFuncLOC(file, null);
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
	  public static int countFuncLOC(File file, List functionDataList) throws IOException {
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

	    while (sReadLine != null) {
	      bContinueThisLine = true;           // reset to true at the beginning of each new line
	      for (int i = 0; (i < sReadLine.length()) && (sReadLine.charAt(i) != '\n') && (bContinueThisLine); i++) // a loop to scan each line character by character
	      {
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

	    return iTotalLOC;
	  }
}