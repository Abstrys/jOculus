package abstrys.antiword;

import java.awt.*;
import javax.swing.*;

class ActionPanel extends JPanel
{
   JLabel label_msg = null;
   JLabel label_wc = null;
   
    ActionPanel()
    {
        this("");
    }   
   
   public ActionPanel(String message)
   {
      //setBackground(Settings.action_panel_color);
      Dimension d = Settings.default_size;

      if(message != null)
      {
          message = "";
      }

      label_msg = new JLabel(message);
      add(label_msg);

      label_wc = new JLabel();
      if(Settings.display_word_count)
      {
          if(message != null)
          {
              add(new JSeparator(JSeparator.VERTICAL));
          }
          add(label_wc);
      }
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
