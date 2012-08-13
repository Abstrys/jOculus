/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package abstrys.antiword;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.swing.JOptionPane;

/**
 *
 * @author eron
 */
public class Utility
{
    static String processCmd(String cmdline)
    {
        String output_str = "";
        Process cmd_proc = null;
        
        try
        {
            cmd_proc = Runtime.getRuntime().exec(cmdline);
        }
        catch(IOException exc)
        {
            return "";
        }
        
        if(cmd_proc != null)
        {
            BufferedReader error_reader = new BufferedReader(new InputStreamReader(cmd_proc.getErrorStream()));
            BufferedReader output_reader = new BufferedReader(new InputStreamReader(cmd_proc.getInputStream()));
            output_str = readStringFromReader(output_reader);
            String error_str = readStringFromReader(error_reader);
            if(error_str.length() > 0)
            {
                JOptionPane.showMessageDialog(null, error_str, "Error", JOptionPane.ERROR_MESSAGE);
            }
        }            
        
        return output_str;
    }

   public static String readStringFromReader(BufferedReader br)
   {

      StringBuilder sb = new StringBuilder();
      try
      {
         boolean done = false;
         do
         {
            String s = br.readLine();
            if(s == null)
            {
               done = true;
            }
            else
            {
               sb.append(s);
            }
         } while(!done);
         
         br.close();
      }
      catch(java.io.IOException exc)
      {
         JOptionPane.showMessageDialog(null, "Error: " + exc.getMessage(), Strings.APPNAME, JOptionPane.ERROR_MESSAGE);
      }

      return sb.toString();
   }
   
   public static String readStringFromFile(File file_to_read)
   {
      BufferedReader br;
      try
      {
         br = new BufferedReader(new FileReader(file_to_read));
      }
      catch(java.io.FileNotFoundException exc)
      {
         return null;
      }
      
      return Utility.readStringFromReader(br);
   }
   
   public static int countWordsInString(String s)
   {
       boolean prev_char_was_space = false;
       int wc = 0;
       
       for(int i = 0; i < s.length(); i++)
       {
           if(s.charAt(i) == ' ')
           {
               prev_char_was_space = true;
           }
           else if(prev_char_was_space)
           {
               wc++;
               prev_char_was_space = false;
           }           
       }
       
       return wc;
   }
}