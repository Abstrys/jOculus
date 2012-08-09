package abstrys.antiword;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.TimerTask;
import javax.swing.*;

public class AntiWord extends JFrame
{
    JFrame app_frame = null;
    String cur_filepath = null;
    File cur_file = null;
    HTMLPanel html_panel = null;

    public AntiWord()
    {
    }

   public boolean init(String args[])
   {
      parseArgs(args);

      if(cur_filepath != null)
      {
         // check to see if the file exists.
         cur_file = new File(cur_filepath);
         if(!cur_file.exists())
         {
            JOptionPane.showMessageDialog(null, "A filename was specified, but does not exist!");
            System.exit(1);
         }
      }

      html_panel = new HTMLPanel();
      // Set up the application frame.
      app_frame = new JFrame(Strings.APPNAME);
//      app_frame.getContentPane().add(html_panel);
      app_frame.getContentPane().add(html_panel);
      app_frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
      app_frame.pack();
      app_frame.setVisible(true);

      return true;
   }

   public void run()
   {
      // load the file, and display it in the panel.
      html_panel.setHTML(readStringFromFile());
   }

   public String readStringFromFile()
   {
      BufferedReader br = null;
      try
      {
         br = new BufferedReader(new FileReader(cur_file));
      }
      catch(java.io.FileNotFoundException exc)
      {
         return null;
      }

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
      }
      catch(java.io.IOException exc)
      {
         JOptionPane.showMessageDialog(null, "Error: " + exc.getMessage(), Strings.APPNAME, JOptionPane.ERROR_MESSAGE);
      }

      return sb.toString();
   }

   public static void main(String args[])
   {
      AntiWord app_instance = new AntiWord();

      if(app_instance.init(args))
      {
         app_instance.run();
      }
   }

   private void parseArgs(String args[])
   {
      for(String arg : args)
      {
         if(arg.startsWith("-"))
         {
            // TODO: maybe add something here.
         }
         else // expect a filename.
         {
            cur_filepath = arg;
         }
      }
   }

   private class FileModificationMonitor extends TimerTask
   {
      private long last_timestamp;
      private File file;

      public FileModificationMonitor(File file)
      {
         this.file = file;
         this.last_timestamp = file.lastModified();
      }

      public final void run()
      {
         long cur_timestamp = file.lastModified();

         if(last_timestamp != cur_timestamp)
         {
            notifyChange(file);
            last_timestamp = cur_timestamp;
         }
      }

      protected void notifyChange(File file)
      {
         // reload the document.
         JOptionPane.showMessageDialog(null, "The document has changed!");
      }
   }

}

