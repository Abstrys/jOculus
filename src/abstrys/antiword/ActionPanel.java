package abstrys.antiword;

import javax.swing.*;
import java.awt.*;

class ActionPanel extends JPanel
{
   public ActionPanel(String message)
   {
      //setBackground(Settings.action_panel_color);
      Dimension d = Settings.default_size;
      add(new JLabel(message));
   }
}
