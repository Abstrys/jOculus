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
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

class Settings
{
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

   int font_smoothing_value = 0;
   final String FONT_SMOOTHING_PROPERTY_NAME = "font_smoothing";

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
   final String CUR_MD_PROCESSOR_NAME = "cur_processor_name";

   public class CSSFile {
      String name;
      String path;

      public CSSFile(String name)
      {
         this.name = name;
         path = null;
      }

      /**
       * Copies the values of another CSSFile to make a new one.
       * @param p the CSSFile to copy.
       */
      private CSSFile(CSSFile p)
      {
         this.name = p.name;
         this.path = p.path;
      }

      @Override
      public String toString()
      {
         return name;
      }

   };
   ArrayList<CSSFile> css_files;
   CSSFile cur_css_file;

   final String CSS_FILES_NODE = "css_files";
   final String CSS_FILE_PATH = "path";
   final String CUR_CSS_FILE_NAME = "cur_css_file_name";

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

      font_smoothing_value = 0;

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
      s.font_smoothing_value = this.font_smoothing_value;

      for(ExternalProcessor p : this.md_processors)
      {
         s.md_processors.add(new ExternalProcessor(p));
         if(p.name.compareTo(this.cur_processor.name) == 0)
         {
            s.cur_processor = p;
         }
      }

      for(CSSFile p : this.css_files)
      {
         s.css_files.add(new CSSFile(p));
         if(p.name.compareTo(this.cur_css_file.name) == 0)
         {
            s.cur_css_file = p;
         }
      }

      s.window_size_last = new Dimension(this.window_size_last.width, this.window_size_last.height);
      s.window_size_remember = this.window_size_remember;

      return s;
   }

   public boolean save()
   {
      Preferences prefs = Preferences.userNodeForPackage(Joculus.class);
      
      boolean save_ok = true;

      save_ok = save_general_prefs(prefs);      
      save_ok = save_editor_prefs(prefs);
      save_ok = save_window_prefs(prefs);
      save_ok = save_processor_prefs(prefs);
      save_ok = save_css_prefs(prefs);

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

   private boolean save_general_prefs(Preferences prefs)
   {
       assert(prefs != null);
      prefs.putInt(ACTION_PANEL_COLOR_R_PROPERTY_NAME, action_panel_color.getRed());
      prefs.putInt(ACTION_PANEL_COLOR_G_PROPERTY_NAME, action_panel_color.getGreen());
      prefs.putInt(ACTION_PANEL_COLOR_B_PROPERTY_NAME, action_panel_color.getBlue());
      prefs.putBoolean(DISPLAY_WORD_COUNT_PROPERTY_NAME, display_word_count);
      prefs.putInt(FONT_SMOOTHING_PROPERTY_NAME, font_smoothing_value);

      return true;
   }

   private boolean save_editor_prefs(Preferences prefs)
   {
      assert(prefs != null);
      prefs.put(EDITOR_PATH_PROPERTY_NAME, editor_path);
      prefs.putBoolean(EDITOR_USE_ENV_PROPERTY_NAME, editor_use_env);
      return true;
   }
   
   private boolean save_window_prefs(Preferences prefs)
   {
      assert(prefs != null);
      // window preferences
      prefs.putInt(WINDOW_SIZE_DEFAULT_W_PROPERTY_NAME, window_size_last.width);
      prefs.putInt(WINDOW_SIZE_DEFAULT_H_PROPERTY_NAME, window_size_last.height);
      prefs.putBoolean(WINDOW_SIZE_REMEMBER_PROPERTY_NAME, window_size_remember);
      return true;
   }
   
   private boolean save_processor_prefs(Preferences prefs)
   {
      //
      // markdown processor preferences
      //

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
      
      // move back out to the prefs node that was passed in.
      prefs = prefs.parent();

      // set the current processor.
      prefs.put(CUR_MD_PROCESSOR_NAME, cur_processor.name);
      
      return true;
   }

   private boolean save_css_prefs(Preferences prefs)
   {
      assert(prefs != null);
      //
      // css file preferences
      //

      // First, remove any old css files from the preferences.
      try
      {
         Preferences md_prefs = prefs.node(CSS_FILES_NODE);
         md_prefs.removeNode();
         md_prefs.flush();
      }
      catch(BackingStoreException ex)
      {
         Joculus.showError(UIStrings.ERROR_WRITE_SETTINGS_FAILED + "\n" + ex.getMessage());
         return false;
      }

      // add the currently-defined css files
      prefs = prefs.node(CSS_FILES_NODE); // we deleted this node, so need to add it again.
      for(CSSFile cf : css_files)
      {
         prefs = prefs.node(cf.name);
         prefs.put(this.CSS_FILE_PATH, (cf.path == null) ? "" : cf.path);
         prefs = prefs.parent();
      }
      prefs = prefs.parent();

      // set the current css file.
      prefs.put(CUR_CSS_FILE_NAME, cur_css_file.name);

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

      action_panel_color = new Color(
              prefs.getInt(ACTION_PANEL_COLOR_R_PROPERTY_NAME, r),
              prefs.getInt(ACTION_PANEL_COLOR_G_PROPERTY_NAME, g),
              prefs.getInt(ACTION_PANEL_COLOR_B_PROPERTY_NAME, b));

      display_word_count = prefs.getBoolean(DISPLAY_WORD_COUNT_PROPERTY_NAME, display_word_count);

      font_smoothing_value = prefs.getInt(FONT_SMOOTHING_PROPERTY_NAME, font_smoothing_value);

      // window size.
      if(window_size_last == null)
      {
         window_size_last = new Dimension();
      }
      window_size_last.width = prefs.getInt(WINDOW_SIZE_DEFAULT_W_PROPERTY_NAME, window_size_last.width);
      window_size_last.height = prefs.getInt(WINDOW_SIZE_DEFAULT_H_PROPERTY_NAME, window_size_last.height);
      window_size_remember = prefs.getBoolean(WINDOW_SIZE_REMEMBER_PROPERTY_NAME, window_size_remember);

      //
      // text editor preferences
      //
      editor_path = prefs.get(EDITOR_PATH_PROPERTY_NAME, editor_path);
      editor_use_env = prefs.getBoolean(EDITOR_USE_ENV_PROPERTY_NAME, editor_use_env);

      //
      // markdown processor preferences
      //
      String cur_proc_name = null;
      cur_proc_name = prefs.get(CUR_MD_PROCESSOR_NAME, cur_proc_name);
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

      //
      // css file preferences
      //
      String cur_css_name = null;
      cur_css_name = prefs.get(CUR_CSS_FILE_NAME, cur_css_name);
      css_files = new ArrayList<CSSFile>();
      prefs = prefs.node(CSS_FILES_NODE);
      String[] css_names;
      try
      {
         css_names = prefs.childrenNames();
      }
      catch (BackingStoreException ex)
      {
         Joculus.showError(UIStrings.ERROR_READ_SETTINGS_FAILED + "\n" + ex.getMessage());
         return false;
      }

      for(String proc_name : css_names)
      {
         prefs = prefs.node(proc_name);
         CSSFile ep = new CSSFile(proc_name);
         ep.path = prefs.get(CSS_FILE_PATH, "");
         prefs = prefs.parent();
         css_files.add(ep);

         // if this is the currently selected css_file, set it.
         if((cur_css_name != null) && (proc_name.compareTo(cur_css_name) == 0))
         {
            cur_css_file = ep;
         }
      }

      return true;
   }

   public ExternalProcessor getProcessorByName(String name)
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

   CSSFile newCSSFile(String name)
   {
      CSSFile cf = new CSSFile(name);
      return cf;
   }

   public Object getFontSmoothingValue()
   {
      switch(font_smoothing_value)
      {
      default:
      case 0:
         return RenderingHints.VALUE_TEXT_ANTIALIAS_DEFAULT;
      case 1:
         return RenderingHints.VALUE_TEXT_ANTIALIAS_OFF;
      case 2:
         return RenderingHints.VALUE_TEXT_ANTIALIAS_ON;
      case 3:
         return RenderingHints.VALUE_TEXT_ANTIALIAS_GASP;
      case 4:
         return RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HBGR;
      case 5:
         return RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB;
      case 6:
         return RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_VBGR;
      case 7:
         return RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_VRGB;
      }

   }

   public void setFontSmoothingValue(Object sv)
   {
      if(sv == RenderingHints.VALUE_TEXT_ANTIALIAS_OFF)
      {
         font_smoothing_value = 1;
      }
      else if(sv == RenderingHints.VALUE_TEXT_ANTIALIAS_ON)
      {
         font_smoothing_value = 2;
      }
      else if(sv == RenderingHints.VALUE_TEXT_ANTIALIAS_GASP)
      {
         font_smoothing_value = 3;
      }
      else if(sv == RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HBGR)
      {
         font_smoothing_value = 4;
      }
      else if(sv == RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB)
      {
         font_smoothing_value = 5;
      }
      else if(sv == RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_VBGR)
      {
         font_smoothing_value = 6;
      }
      else if(sv == RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_VRGB)
      {
         font_smoothing_value = 7;
      }
      else
      {
         font_smoothing_value = 0;
      }
   }
}
