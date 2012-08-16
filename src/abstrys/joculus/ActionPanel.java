package abstrys.joculus;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

class ActionPanel extends JPanel
{
   JLabel label_wc = null;

   public ActionPanel(final Settings s)
   {
      //setBackground(Settings.action_panel_color);
      Dimension d = s.window_size_default;

      setLayout(new BorderLayout());

      label_wc = new JLabel();
      add(label_wc, BorderLayout.CENTER);
      label_wc.setVisible(s.display_word_count);

      JButton b = new JButton("Settings");
      b.addActionListener(new ActionListener() {
         @Override public void actionPerformed(ActionEvent ae) { new ProcessorSettingsDlg(s); } });
      add(b, BorderLayout.EAST);
   }

   public void setWordCount(int wc)
   {
       label_wc.setText(String.valueOf(wc) + " words");
   }
}
