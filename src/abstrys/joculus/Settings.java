package abstrys.joculus;

import java.awt.Color;
import java.awt.Dimension;

class Settings
{
   Color action_panel_color;
   boolean display_word_count;
   String editor_path;
   boolean editor_use_env;
   String md_processor_name;
   String md_processor_opt;
   String md_processor_path;
   Dimension window_size_default;
   boolean window_size_remember;

   public Settings()
   {
      setDefaults();
   }

   public boolean save()
   {
      // http://docs.oracle.com/javase/6/docs/api/java/util/Properties.html
      return false;
   }

   public boolean load()
   {
      return true;
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

   final void setProcessorDefaults()
   {
   }

   final void setUIDefaults()
   {
   }
}
