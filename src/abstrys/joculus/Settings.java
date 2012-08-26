/**
 * ActionPanel.java
 *
 *    Part of the jOculus project: https://github.com/Abstrys/jOculus
 *
 *    Copyright (C) 2012 by Eron Hennessey
 */
package abstrys.joculus;

import java.awt.Color;
import java.awt.Dimension;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

class Settings
{
   final String SETTINGS_FILE = "options.xml";
   
   // These settings are properties that are saved / loaded.
   Color action_panel_color;
   final String ACTION_PANEL_COLOR_R_PROPERTY_NAME = "action_panel_color_r";
   final String ACTION_PANEL_COLOR_G_PROPERTY_NAME = "action_panel_color_g";
   final String ACTION_PANEL_COLOR_B_PROPERTY_NAME = "action_panel_color_b";

   boolean display_word_count;
   final String DISPLAY_WORD_COUNT_PROPERTY_NAME = "display_word_count";

   String editor_path;
   final String EDITOR_PATH_PROPERTY_NAME = "editor_path";

   boolean editor_use_env;
   final String EDITOR_USE_ENV_PROPERTY_NAME = "editor_use_env";

   String md_processor_name;
   final String MD_PROCESSOR_NAME_PROPERTY_NAME = "md_processor_name";

   String md_processor_opt;
   final String MD_PROCESSOR_OPT_PROPERTY_NAME = "md_processor_opt";

   String md_processor_path;
   final String MD_PROCESSOR_PATH_PROPERTY_NAME = "md_processor_path";

   Dimension window_size_default;
   final String WINDOW_SIZE_DEFAULT_W_PROPERTY_NAME = "window_size_w_default";
   final String WINDOW_SIZE_DEFAULT_H_PROPERTY_NAME = "window_size_h_default";

   boolean window_size_remember;
   final String WINDOW_SIZE_REMEMBER_PROPERTY_NAME = "window_size_remember";

   public Settings()
   {
      setDefaults();
   }

   final void setDefaults()
   {
      action_panel_color = Color.darkGray;
      display_word_count = true;
      editor_path = "";
      editor_use_env = true;
      md_processor_name = "Pandoc";
      md_processor_opt = "--to html";
      md_processor_path = "/usr/local/bin/pandoc";
      window_size_default = new Dimension(400, 500);
      window_size_remember = true;
   }
   
   public boolean save()
   {      
      // make sure the settings directory exists!
      File settings_dir = new File(getSettingsPath());
      if(!settings_dir.exists())
      {
         settings_dir.mkdir();
      }
      else if(settings_dir.isFile())
      {
         settings_dir.delete();
         settings_dir.mkdir();
      }

      String complete_path = settings_dir.getAbsolutePath() + File.separator + SETTINGS_FILE;
      File settingsFile = new File(complete_path);
      if (!settingsFile.exists())
      {
         try
         {
            settingsFile.createNewFile();
         }
         catch (IOException ex)
         {
            Joculus.showError(Strings.ERROR_CREATEFILE_FAILED + " " + complete_path + ":\n" + ex.getMessage());
         }
      }

      FileOutputStream fos = null;
      try
      {
         fos = new FileOutputStream(settingsFile);
      }
      catch (FileNotFoundException ex)
      {
         Joculus.showError(Strings.ERROR_OPENSTREAM_FAILED + "\n" + ex.getMessage());
      }

      Properties p = new Properties();
      p.setProperty(ACTION_PANEL_COLOR_R_PROPERTY_NAME, String.valueOf(action_panel_color.getRed()));
      p.setProperty(ACTION_PANEL_COLOR_G_PROPERTY_NAME, String.valueOf(action_panel_color.getGreen()));
      p.setProperty(ACTION_PANEL_COLOR_B_PROPERTY_NAME, String.valueOf(action_panel_color.getBlue()));
      p.setProperty(DISPLAY_WORD_COUNT_PROPERTY_NAME, display_word_count ? "1" : "0");
      p.setProperty(EDITOR_PATH_PROPERTY_NAME, editor_path);
      p.setProperty(EDITOR_USE_ENV_PROPERTY_NAME, editor_use_env ? "1" : "0");
      p.setProperty(MD_PROCESSOR_NAME_PROPERTY_NAME, md_processor_name);
      p.setProperty(MD_PROCESSOR_OPT_PROPERTY_NAME, md_processor_opt);
      p.setProperty(MD_PROCESSOR_PATH_PROPERTY_NAME, md_processor_path);
      p.setProperty(WINDOW_SIZE_DEFAULT_W_PROPERTY_NAME, String.valueOf(window_size_default.width));
      p.setProperty(WINDOW_SIZE_DEFAULT_H_PROPERTY_NAME, String.valueOf(window_size_default.height));
      p.setProperty(WINDOW_SIZE_REMEMBER_PROPERTY_NAME, window_size_remember ? "1" : "0");
      Date d = new Date();
      try
      {
         p.storeToXML(fos, d.toString());
      }
      catch (IOException ex)
      {
         Joculus.showError(Strings.ERROR_WRITE_SETTINGS_FAILED + "\n" + ex.getMessage());
         return false;
      }

      return true;
   }

   public boolean load()
   {
      File settings_dir = new File(getSettingsPath());
      if(!settings_dir.exists())
      {
         settings_dir.mkdir();
      }
      else if(settings_dir.isFile())
      {
         settings_dir.delete();
         settings_dir.mkdir();
      }      
      
      String complete_path = settings_dir.getAbsolutePath() + File.separator + SETTINGS_FILE;
      File settingsFile = new File(complete_path);
      if (!settingsFile.exists())
      {
         // the settings aren't there. use the defaults.
         return true;
      }

      FileInputStream fis;
      try
      {
         fis = new FileInputStream(settingsFile);
      }
      catch (FileNotFoundException ex)
      {
         // the file is there, I just can't create a filestream for it.
         Joculus.showError(Strings.ERROR_OPENSTREAM_FAILED + "\n" + ex.getMessage());
         return false;
      }

      Properties p = new Properties();
      try
      {
         p.loadFromXML(fis);
      }
      catch (IOException ex)
      {
         Joculus.showError(Strings.ERROR_OPENSTREAM_FAILED + "\n" + ex.getMessage());
      }

      Color c = new Color(
              Integer.parseInt(p.getProperty(ACTION_PANEL_COLOR_R_PROPERTY_NAME)),
              Integer.parseInt(p.getProperty(ACTION_PANEL_COLOR_G_PROPERTY_NAME)),
              Integer.parseInt(p.getProperty(ACTION_PANEL_COLOR_B_PROPERTY_NAME)));
      action_panel_color = c;
      display_word_count = (Integer.parseInt(p.getProperty(DISPLAY_WORD_COUNT_PROPERTY_NAME)) == 1);
      editor_path = p.getProperty(EDITOR_PATH_PROPERTY_NAME);
      editor_use_env = (Integer.parseInt(p.getProperty(EDITOR_USE_ENV_PROPERTY_NAME)) == 1);
      md_processor_name = p.getProperty(MD_PROCESSOR_NAME_PROPERTY_NAME);
      md_processor_opt = p.getProperty(MD_PROCESSOR_OPT_PROPERTY_NAME);
      md_processor_path = p.getProperty(MD_PROCESSOR_PATH_PROPERTY_NAME);
      Dimension d = new Dimension(
              Integer.parseInt(p.getProperty(WINDOW_SIZE_DEFAULT_W_PROPERTY_NAME)),
              Integer.parseInt(p.getProperty(WINDOW_SIZE_DEFAULT_H_PROPERTY_NAME)));
      window_size_default = d;
      window_size_remember = (Integer.parseInt(p.getProperty(WINDOW_SIZE_REMEMBER_PROPERTY_NAME)) == 1);

      return true;
   }

   public static String getSettingsPath()
   {
      final String SETTINGS_DIR = ".jOculus";      
      String home_path = Utility.getUserHome();
      if(home_path == null)
      {
         return null;
      }
      
      return home_path + File.separator + SETTINGS_DIR;
   }
}
