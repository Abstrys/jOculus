package abstrys.joculus;
import java.awt.Color;
import java.awt.Dimension;

class Settings
{
   public static Dimension default_size = new Dimension(400, 500);
   public static Color action_panel_color = Color.darkGray;
   public static String md_processor_cmd = "/usr/local/bin/pandoc --from markdown --to html";
   public static boolean display_word_count = true;
   
   public Settings()
   {
       setDefaults();
   }
   
   public void setDefaults()
   {
    default_size = new Dimension(400,600);
    action_panel_color = Color.darkGray;
    md_processor_cmd = "/usr/local/bin/pandoc --from markdown --to html";
    display_word_count = true;
   }
}
