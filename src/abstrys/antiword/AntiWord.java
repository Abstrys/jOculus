package abstrys.antiword;

import java.awt.BorderLayout;
import java.awt.Container;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Timer;
import javax.swing.*;

public class AntiWord extends JFrame implements FileModificationMonitor.ReloadsFile
{
    JFrame app_frame = null;
    String cur_filepath = null;
    File cur_file = null;
    boolean file_is_html = false;
    String file_pre = "";
    String file_ext = "";
    HTMLPanel html_panel = null;
    ActionPanel action_panel = null;
    Timer file_timer_task = null;

    public AntiWord()
    {
    }

   public boolean init(String args[])
   {
      parseArgs(args);

      boolean file_loaded = false;
      boolean file_specified = false;
      if(cur_filepath != null)
      {
         file_loaded = setFile(cur_filepath);
         file_specified = true;
      }

      // the file was specified, but it could not be loaded. The user has been notified (in the setFile method).
      if(file_specified && !file_loaded)
      {
         return false;
      }

      // Set up the application frame.
      app_frame = new JFrame(Strings.APPNAME);
      html_panel = new HTMLPanel();
      action_panel = new ActionPanel(file_ext);

      Container cp = app_frame.getContentPane();
      cp.setLayout(new BorderLayout());

      cp.add(html_panel, BorderLayout.CENTER);
      cp.add(action_panel, BorderLayout.SOUTH);

      app_frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
      app_frame.pack();
      app_frame.setVisible(true);

      return true;
   }

   public void run()
   {
      // load the file, and display it in the panel.
      html_panel.setHTML(Utility.readStringFromFile(cur_file));
   }

    @Override
   public void reloadFile()
   {
       String html_content = "";
       
       if(file_is_html)
       {
           html_content = Utility.readStringFromFile(cur_file);
       }
       else
       {
           html_content = Utility.processCmd(Settings.md_processor_cmd);
       }
       
       // invoke the processor to convert the file.
       
       html_panel.setHTML(html_content);
   }    
   
   protected boolean setFile(String fpath)
   {
       if(fpath != null)
       {
           cur_filepath = fpath;
       }
       
      // check to see if the file exists.
      cur_file = new File(cur_filepath);
      if(!cur_file.exists())
      {
         JOptionPane.showMessageDialog(null, "A filename was specified, but does not exist!");
         return false;
      }

      // excellent. Set up a file notification monitor.
      if(file_timer_task != null)
      {
          file_timer_task.cancel();
      }

      file_timer_task = new Timer();
      file_timer_task.schedule(new FileModificationMonitor(cur_file, this), 1000, 1000);

      // grab the filename and extension while we're at it.
      String[] parts = cur_filepath.split("\\.(?=[^\\.]+$)");
      file_pre = parts[0];
      file_ext = parts[1];
      
      if(file_ext.matches("htm[l]"))
      {
          file_is_html = true;
      }
      
      return true;
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
}

