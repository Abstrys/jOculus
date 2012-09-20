/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package abstrys.joculus;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Loads a file on a thread, and sends the string to the calling process.
 * @author Eron Hennessey
 */
public class TextFileLoader extends Thread
{
  /**
   * Handles a callback from the TextFileLoader class
   * @author eronh
   */
   public interface TextHandler
   {
     /**
      * Receives the string from a FileLoader on success
      * @param wc the number of words in the contents
      * @param contents the contents of the file.
      */
     public void textFileLoaded(File file, int wc, String contents);

     /**
      * Receives the reason from a FileLoader on failure
      * @param error_text text that describes the problem.
      */
     public void textFileFailed(String error_text);
   }

   private TextHandler fl_handler;
   private File fl_file;
   private long last_timestamp;
   private boolean keep_running;


   /**
    * Creates a new FileLoader
    * @param fpath the pathname of the file to load
    * @param handler the FileLoaderHandler to call when the file is loaded.
    */
   public TextFileLoader(String fpath, TextHandler handler)
   {
      fl_file = new File(fpath);
      fl_handler = handler;
   }

   /**
    * Creates a new FileLoader
    * @param file the file to load
    * @param handler the FileLoaderHandler to call when the file is loaded.
    */
   public TextFileLoader(File file, TextHandler handler)
   {
      fl_file = file;
      fl_handler = handler;
   }

   /**
    * Count the words in the string
    * @param s the string to process
    * @return the number of words in the string
    */
   private int countWordsInString(String s)
   {
      boolean is_space;
      boolean prev_is_space = true;
      int wc = 0;
      final int len = s.length();

      for (int i = 0; i < len; i++)
      {
         int ch = s.codePointAt(i);
         is_space = Character.isSpaceChar(s.codePointAt(i));
         if(prev_is_space && !is_space)
         {
            wc++;
         }

         prev_is_space = is_space;
      }

      return wc;
   }

   @Override
   public void run()
   {
      keep_running = true;
      readStringFromFile();
      last_timestamp = fl_file.lastModified();

      while(keep_running)
      {
         long cur_timestamp = fl_file.lastModified();

         if(last_timestamp != cur_timestamp)
         {
            readStringFromFile();
            last_timestamp = cur_timestamp;
         }

         try
         {
            this.sleep(555);
         }
         catch(InterruptedException exc)
         {
            keep_running = false;
         }
      }
   }

   public void die()
   {
      keep_running = false;
   }

   public void readStringFromFile()
   {
      // make sure the file is legit.
      if(!fl_file.exists())
      {
         fl_handler.textFileFailed(UIStrings.ERROR_BAD_FILEPATH + fl_file.getPath());
      }

      // read from the file.
      String file_contents;
      try
      {
         file_contents = new Scanner(fl_file).useDelimiter("\\Z").next();
      }
      catch (FileNotFoundException ex)
      {
         if(fl_handler != null)
         {
            fl_handler.textFileFailed(ex.getMessage());
         }
         return;
      }

      // count the words in the text
      int wc = countWordsInString(file_contents);

      if(fl_handler != null)
      {
         fl_handler.textFileLoaded(fl_file, wc, file_contents);
      }
   }
}
