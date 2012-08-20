package abstrys.joculus;

import java.awt.BorderLayout;
import java.awt.Container;
import java.io.File;
import java.util.Timer;
import javax.swing.*;

public class Joculus extends JFrame implements FileModificationMonitor.ReloadsFile
{
   JFrame app_frame = null;
   File cur_file = null;
   boolean file_is_html = false;
   String file_pre = "";
   String file_ext = "";
   String base_url = ""; // the URL form of the file's parent directory.
   HTMLPanel html_panel = null;
   ActionPanel action_panel = null;
   Timer file_timer_task = null;
   Settings settings;

   public Joculus()
   {
   }

   public boolean init(String args[])
   {
      settings = new Settings();
      settings.load();

      String fpath = parseArgs(args);

      app_frame = new JFrame();
      html_panel = new HTMLPanel(settings);
      action_panel = new ActionPanel(settings);

      if (fpath == null)
      {
         JOptionPane.showMessageDialog(this, Strings.ERROR_NO_FILE_SPECIFIED, Strings.ERRORMSG_TITLE, JOptionPane.ERROR_MESSAGE);
         return false;
      }

      if(!setFile(fpath))
      {
         // the file was specified, but it could not be loaded. The user has been notified (in the setFile method).
         return false;
      }

      // Set up the application frame.

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
      reloadFile();
   }

   Settings getSettings()
   {
      return settings;
   }

   @Override
   public void reloadFile()
   {
      StringBuilder html_content = new StringBuilder();
      final String xhtml_doc_start = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n<html xmlns=\"http://www.w3.org/1999/xhtml\">";
      final String xhtml_head_start = "<head>";
      final String xhtml_head_end = "</head>";
      final String xhtml_body_start = "<body>";
      final String xhtml_body_end = "</body>";
      final String xhtml_doc_end = "</html>";

      if (file_is_html)
      {
         html_content.append(Utility.readStringFromFile(cur_file));
      }
      else
      {
         // invoke the processor to convert the file.
         html_content.append(xhtml_doc_start);
         html_content.append(xhtml_head_start);
         // TODO: add the stylesheet here.
         html_content.append(xhtml_head_end);
         html_content.append(xhtml_body_start);
         File f = new File(settings.md_processor_path);
         if(!f.exists())
         {
            Joculus.showError(Strings.ERROR_INVALID_PROCESSOR_PATH);
         }
         else
         {            
            html_content.append(Utility.processCmd(
                    settings.md_processor_path + " "
                    + settings.md_processor_opt + " " + cur_file.getPath()));
         }
         html_content.append(xhtml_body_end);
         html_content.append(xhtml_doc_end);
         
         if (settings.display_word_count)
         {
            action_panel.setWordCount(Utility.countWordsInString(Utility.readStringFromFile(cur_file)));
         }
      }
            
      html_panel.setHTML(html_content.toString(), base_url);
   }

   protected boolean setFile(String fpath)
   {
      if (fpath == null)
      {
         return false;
      }

      // check to see if the file exists.
      cur_file = new File(fpath);
      if (!cur_file.exists())
      {
         JOptionPane.showMessageDialog(this, Strings.ERROR_BAD_FILEPATH);
         return false;
      }

      // excellent. Set up a file notification monitor.
      if (file_timer_task != null)
      {
         file_timer_task.cancel();
      }

      file_timer_task = new Timer();
      file_timer_task.schedule(new FileModificationMonitor(cur_file, this), 1000, 1000);

      // grab the filename and extension while we're at it.
      String[] parts = cur_file.getName().split("\\.(?=[^\\.]+$)");

      file_pre = parts[0];
      file_ext = parts[1];

      if (file_ext.matches("htm[l]"))
      {
         file_is_html = true;
      }

      if (app_frame != null)
      {
         app_frame.setTitle(Strings.APPNAME + " - " + cur_file.getName());
      }

      base_url = "file://" + cur_file.getAbsoluteFile().getParent() + "/";      
      
      return true;
   }

   public static void main(String args[])
   {
      Joculus app_instance = new Joculus();

      if (app_instance.init(args))
      {
         app_instance.run();
      }
   }

   /**
    * Returns the args. If a file was specified (required!), then it returns the file path in the return value.
    *
    * @param args the command-line arguments.
    * @return the filepath
    */
   private String parseArgs(String args[])
   {
      String fpath = null;

      for (String arg : args)
      {
         if (arg.startsWith("-"))
         {
            // TODO: maybe add something here.
         }
         else // expect a filename.
         {
            fpath = arg;
         }
      }

      return fpath;
   }

   public static void showError(String err_msg)
   {
      JOptionPane.showMessageDialog(null, err_msg, Strings.ERRORMSG_TITLE, JOptionPane.ERROR_MESSAGE);
   }
}
