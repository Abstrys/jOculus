/**
 * ActionPanel.java
 *
 * Part of the jOculus project: https://github.com/Abstrys/jOculus
 *
 * Copyright (C) 2012 by Eron Hennessey
 */
package abstrys.joculus;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.*;

public class Joculus implements TextFileLoader.TextHandler, MarkdownProcessor.XhtmlHandler
{
   enum OSType { OS_Unix, OS_MacOSX, OS_Windows, Unknown };
   OSType os_type;

   JFrame app_frame = null;
   File cur_file = null;
   TextFileLoader cur_file_loader = null;
   String cur_html;
   boolean markdown_failed;
   String file_contents;
   String file_pre = "";
   String file_ext = "";
   String base_url = ""; // the URL form of the file's parent directory.
   HTMLPanel html_panel = null;
   ActionPanel action_panel = null;
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

      os_type = getOSType();

      // set the application icon
      final String[] icon_names =
      {
         "jOculus-icon-256.png", "jOculus-icon-128.png",
         "jOculus-icon-96.png", "jOculus-icon-64.png", "jOculus-icon-48.png",
         "jOculus-icon-32.png", "jOculus-icon-16.png"
      };
      ArrayList<BufferedImage> icon_list = new ArrayList<BufferedImage>();
      for (String icon : icon_names)
      {
         BufferedImage i = null;
         try
         {
            i = ImageIO.read(getClass().getClassLoader().getResource("abstrys/joculus/res/" + icon));
         }
         catch (IOException ex)
         {
            showError(ex.getMessage());
         }

         if (i != null)
         {
            icon_list.add(i);
         }
      }

      app_frame.setIconImage(icon_list.get(3));

      html_panel = new HTMLPanel(settings);
      action_panel = new ActionPanel(this);

      if (fpath == null)
      {
         JOptionPane.showMessageDialog(app_frame, Strings.ERROR_NO_FILE_SPECIFIED, Strings.ERRORMSG_TITLE, JOptionPane.ERROR_MESSAGE);
         return false;
      }

      if (!setFile(fpath))
      {
         // the file was specified, but it could not be loaded. The user has been notified (in the setFile method).
         return false;
      }

      // Set up the application frame.

      Container cp = app_frame.getContentPane();
      cp.setLayout(new BorderLayout());

      cp.add(html_panel, BorderLayout.CENTER);
      cp.add(action_panel, BorderLayout.SOUTH);

      app_frame.pack();
      app_frame.setVisible(true);

      // handle window closing, passing execution to the onExit method.
      app_frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
      app_frame.addWindowListener(new WindowAdapter()
      {
         @Override
         public void windowClosing(WindowEvent e)
         {
            onExit();
         }
      });
      return true;
   }

   public void run()
   {

   }

   private void onExit()
   {
      // if the user wants to save the window size on exit, do it.
      if (settings.window_size_remember)
      {
         settings.window_size_default = html_panel.getSize();
         settings.save();
      }

      System.exit(1);
   }

   Settings getSettings()
   {
      return settings;
   }

   public void refreshDisplay()
   {
      if(markdown_failed)
      {
         new Thread(new MarkdownProcessor(cur_file, settings, this)).start();
      }

      StringBuilder html_content = new StringBuilder();

      // invoke the processor to convert the file.
      html_content.append(Strings.XHTML_DECL);
      html_content.append(Strings.XHTML_HEAD_BEGIN);
      // TODO: add the stylesheet here.
      html_content.append(Strings.XHTML_HEAD_END);
      html_content.append(Strings.XHTML_BODY_BEGIN);
      html_content.append(cur_html);
      html_content.append(Strings.XHTML_END);

      base_url = "file://" + cur_file.getAbsoluteFile().getParent() + "/";

      html_panel.setHTML(html_content.toString(), base_url);
   }

   protected boolean setFile(String fpath)
   {
      if(cur_file_loader != null)
      {
         cur_file_loader.die();
         cur_file_loader = null;
      }

      if (fpath == null)
      {
         app_frame.setTitle(Strings.APPNAME + " - no file loaded");
         return false;
      }

      // grab the filename and extension while we're at it.
      String[] parts = fpath.split("\\.(?=[^\\.]+$)");

      file_pre = parts[0];
      file_ext = parts[1];

      if (app_frame != null)
      {
         app_frame.setTitle(Strings.APPNAME + " - " + file_pre);
      }

      cur_file_loader = new TextFileLoader(fpath, this);
      cur_file_loader.start();

      return true;
   }

   public static void main(String args[])
   {
      Joculus app_instance = new Joculus();

      if (app_instance.init(args))
      {
      //   app_instance.run();
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

   private OSType getOSType()
   {
      String os_name = System.getProperty("os.name").toLowerCase();
      if(os_name.contains("mac"))
      {
         return OSType.OS_MacOSX;
      }
      else if(os_name.contains("nix") || os_name.contains("nux"))
      {
         return OSType.OS_Unix;
      }
      else if(os_name.contains("win"))
      {
         return OSType.OS_Windows;
      }
      else
      {
         return OSType.Unknown;
      }
   }

   public static void showError(String err_msg)
   {
      JOptionPane.showMessageDialog(null, err_msg, Strings.ERRORMSG_TITLE, JOptionPane.ERROR_MESSAGE);
   }

   // === === ===
   // Handlers for the TextFileLoader
   @Override
   public void textFileLoaded(File file, int wc, String contents)
   {
      cur_file = file;
      this.action_panel.setWordCount(wc);

      new Thread(new MarkdownProcessor(file, settings, this)).start();
   }

   @Override
   public void textFileFailed(String error_text)
   {
      showError(error_text);
   }

   // === === ===
   // Handlers for the MarkdownProcessor
   @Override
   public void xhtmlSuccess(String xhtml)
   {
      cur_html = xhtml;
      markdown_failed = false;
      refreshDisplay();
   }

   @Override
   public void xhtmlFailure(String error_text)
   {
      markdown_failed = true;
      showError(error_text);
   }
}
