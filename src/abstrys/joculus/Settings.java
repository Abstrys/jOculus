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
import java.util.ArrayList;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

class Settings
{
   final String SETTINGS_FILE = "options.xml";

   // These settings are properties that are saved / loaded.
   Color action_panel_color;
   final String ACTION_PANEL_COLOR_R_PROPERTY_NAME = "action_panel_color/r";
   final String ACTION_PANEL_COLOR_G_PROPERTY_NAME = "action_panel_color/g";
   final String ACTION_PANEL_COLOR_B_PROPERTY_NAME = "action_panel_color/b";

   boolean display_word_count;
   final String DISPLAY_WORD_COUNT_PROPERTY_NAME = "display_word_count";

   String editor_path;
   final String EDITOR_PATH_PROPERTY_NAME = "editor/path";

   boolean editor_use_env;
   final String EDITOR_USE_ENV_PROPERTY_NAME = "editor/use_env";

   public class ExternalProcessor {
      String name;
      String path;
      String options;

      public ExternalProcessor(String name)
      {
         this.name = name;
         path = null;
         options = null;
      }

      /**
       * Copies the values of another ExternalProcessor to make a new one.
       * @param p the ExternalProcessor to copy.
       */
      private ExternalProcessor(ExternalProcessor p)
      {
         this.name = p.name;
         this.path = p.path;
         this.options = p.options;
      }

      @Override
      public String toString()
      {
         return name;
      }

   };
   ArrayList<ExternalProcessor> md_processors;
   ExternalProcessor cur_processor;

   final String MD_PROCESSORS_NODE = "md_processors";
   final String MD_PROCESSOR_PATH = "path";
   final String MD_PROCESSOR_OPTIONS = "options";
   final String MD_CUR_PROCESSOR_NAME = "cur_processor_name";

   Dimension window_size_last;
   final String WINDOW_SIZE_DEFAULT_W_PROPERTY_NAME = "window_size/default_w";
   final String WINDOW_SIZE_DEFAULT_H_PROPERTY_NAME = "window_size/default_h";

   boolean window_size_remember;
   final String WINDOW_SIZE_REMEMBER_PROPERTY_NAME = "window_size/remember";

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

      // markdown processor defaults
      md_processors = new ArrayList<ExternalProcessor>();

      ExternalProcessor ep = new ExternalProcessor("Pandoc");
      ep.path = "/usr/local/bin/pandoc";
      ep.options = "--to html";
      cur_processor = ep; // this is the default selected processor.
      md_processors.add(ep);

      ep = new ExternalProcessor("MultiMarkdown");
      ep.path = "/usr/local/bin/mmd";
      ep.options = "--to html --smart";
      md_processors.add(ep);

      ep = new ExternalProcessor("Markdown");
      ep.path = "/usr/local/bin/markdown";
      ep.options = "";
      md_processors.add(ep);

      window_size_last = new Dimension(400, 500);
      window_size_remember = true;
   }

   @Override
   public Settings clone()
   {
      Settings s = new Settings();
      s.action_panel_color = new Color(this.action_panel_color.getRGB());
      s.display_word_count = this.display_word_count;
      s.editor_path = this.editor_path;
      s.editor_use_env = this.editor_use_env;
      for(ExternalProcessor p : this.md_processors)
      {
         s.md_processors.add(new ExternalProcessor(p));
         if(p.name.compareTo(this.cur_processor.name) == 0)
         {
            s.cur_processor = p;
         }
      }
      s.window_size_last = new Dimension(this.window_size_last.width, this.window_size_last.height);
      s.window_size_remember = this.window_size_remember;

      return s;
   }

   public boolean save()
   {
      Preferences prefs = Preferences.userNodeForPackage(Joculus.class);

      // general preferences
      prefs.putInt(ACTION_PANEL_COLOR_R_PROPERTY_NAME, action_panel_color.getRed());
      prefs.putInt(ACTION_PANEL_COLOR_G_PROPERTY_NAME, action_panel_color.getGreen());
      prefs.putInt(ACTION_PANEL_COLOR_B_PROPERTY_NAME, action_panel_color.getBlue());
      prefs.putBoolean(DISPLAY_WORD_COUNT_PROPERTY_NAME, display_word_count);

      // text editor preferences
      prefs.put(EDITOR_PATH_PROPERTY_NAME, editor_path);
      prefs.putBoolean(EDITOR_USE_ENV_PROPERTY_NAME, editor_use_env);

      // window preferences
      prefs.putInt(WINDOW_SIZE_DEFAULT_W_PROPERTY_NAME, window_size_last.width);
      prefs.putInt(WINDOW_SIZE_DEFAULT_H_PROPERTY_NAME, window_size_last.height);
      prefs.putBoolean(WINDOW_SIZE_REMEMBER_PROPERTY_NAME, window_size_remember);

      // markdown processor preferences

      // First, remove any old processors from the preferences.
      try
      {
         Preferences md_prefs = prefs.node(MD_PROCESSORS_NODE);
         md_prefs.removeNode();
         md_prefs.flush();
      }
      catch(BackingStoreException ex)
      {
         Joculus.showError(UIStrings.ERROR_WRITE_SETTINGS_FAILED + "\n" + ex.getMessage());
         return false;
      }

      // add the currently-defined processors
      prefs = prefs.node(MD_PROCESSORS_NODE);
      for(ExternalProcessor ep : md_processors)
      {
         prefs = prefs.node(ep.name);
         prefs.put(this.MD_PROCESSOR_PATH, (ep.path == null) ? "" : ep.path);
         prefs.put(this.MD_PROCESSOR_OPTIONS, (ep.options == null) ? "" : ep.options);
         prefs = prefs.parent();
      }
      prefs = prefs.parent();

      // set the current processor.
      prefs.put(MD_CUR_PROCESSOR_NAME, cur_processor.name);

      // write all changes.
      try
      {
         prefs.flush();
      }
      catch (BackingStoreException ex)
      {
         Joculus.showError(UIStrings.ERROR_WRITE_SETTINGS_FAILED + "\n" + ex.getMessage());
         return false;
      }
      return true;
   }

   /*
    * Save only the window size. This is a shortcut used at the end of a normal session.
    */
   public boolean saveWindowSize()
   {
      Preferences prefs = Preferences.userNodeForPackage(Joculus.class);

      // window preferences
      prefs.putInt(WINDOW_SIZE_DEFAULT_W_PROPERTY_NAME, window_size_last.width);
      prefs.putInt(WINDOW_SIZE_DEFAULT_H_PROPERTY_NAME, window_size_last.height);

      try
      {
         prefs.flush();
      }
      catch (BackingStoreException ex)
      {
         Joculus.showError(UIStrings.ERROR_WRITE_SETTINGS_FAILED + "\n" + ex.getMessage());
      }
      return true;
   }

   public boolean load()
   {
      Preferences prefs = Preferences.userNodeForPackage(Joculus.class);

      //
      // general preferences
      //

      // for the action panel color, use the present set color (likely to be the default),
      // unless different values are read in from the settings.
      int r = action_panel_color.getRed();
      int g = action_panel_color.getGreen();
      int b = action_panel_color.getBlue();

      action_panel_color = new Color(prefs.getInt(ACTION_PANEL_COLOR_R_PROPERTY_NAME, r), prefs.getInt(ACTION_PANEL_COLOR_G_PROPERTY_NAME, g), prefs.getInt(ACTION_PANEL_COLOR_B_PROPERTY_NAME, b));

      display_word_count = prefs.getBoolean(DISPLAY_WORD_COUNT_PROPERTY_NAME, display_word_count);

      // window size.
      if(window_size_last == null)
      {
         window_size_last = new Dimension();
      }
      window_size_last.width = prefs.getInt(WINDOW_SIZE_DEFAULT_W_PROPERTY_NAME, window_size_last.width);
      window_size_last.height = prefs.getInt(WINDOW_SIZE_DEFAULT_H_PROPERTY_NAME, window_size_last.height);
      window_size_remember = prefs.getBoolean(WINDOW_SIZE_REMEMBER_PROPERTY_NAME, window_size_remember);

      // text editor preferences
      editor_path = prefs.get(EDITOR_PATH_PROPERTY_NAME, editor_path);
      editor_use_env = prefs.getBoolean(EDITOR_USE_ENV_PROPERTY_NAME, editor_use_env);

      // markdown processor preferences
      String cur_proc_name = null;
      cur_proc_name = prefs.get(MD_CUR_PROCESSOR_NAME, cur_proc_name);
      md_processors = new ArrayList<ExternalProcessor>();
      prefs = prefs.node(MD_PROCESSORS_NODE);
      String[] proc_names;
      try
      {
         proc_names = prefs.childrenNames();
      }
      catch (BackingStoreException ex)
      {
         Joculus.showError(UIStrings.ERROR_READ_SETTINGS_FAILED + "\n" + ex.getMessage());
         return false;
      }

      for(String proc_name : proc_names)
      {
         prefs = prefs.node(proc_name);
         ExternalProcessor ep = new ExternalProcessor(proc_name);
         ep.path = prefs.get(MD_PROCESSOR_PATH, "");
         ep.options = prefs.get(MD_PROCESSOR_OPTIONS, "");
         prefs = prefs.parent();
         md_processors.add(ep);

         // if this is the currently selected processor, set it.
         if((cur_proc_name != null) && (proc_name.compareTo(cur_proc_name) == 0))
         {
            cur_processor = ep;
         }
      }

      if(cur_processor == null)
      {
         cur_processor = md_processors.get(0);
      }

      return true;
   }

   public ExternalProcessor getCurProcessorByName(String name)
   {
      if(md_processors == null)
      {
         return null;
      }

      for(ExternalProcessor p : md_processors)
      {
         if(p.name.compareTo(name) == 0)
         {
            return p;
         }
      }
      return null;
   }

   public ExternalProcessor newProcessor(String name)
   {
      ExternalProcessor ep = new ExternalProcessor(name);
      return ep;
   }
}
