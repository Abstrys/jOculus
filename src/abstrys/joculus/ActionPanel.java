package abstrys.joculus;

import java.awt.*;
import javax.swing.*;

class ActionPanel extends JPanel
{
   JLabel label_msg = null;
   JLabel label_wc = null;
      
   public ActionPanel()
   {
      //setBackground(Settings.action_panel_color);
      Dimension d = Settings.default_size;

      label_wc = new JLabel();
      add(label_wc);
      label_wc.setVisible(Settings.display_word_count);
   }
   
   public void setWordCount(int wc)
   {       
       label_wc.setText(String.valueOf(wc) + " words");
   }
   
   public void setMessage(String msg)
   {
       if(msg == null)
       {
           msg = "";
       }

       label_msg.setText(msg);
   }
}
