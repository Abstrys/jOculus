/**
 * ActionPanel.java
 *
 *    Part of the jOculus project: https://github.com/Abstrys/jOculus
 *
 *    Copyright (C) 2012 by Eron Hennessey
 */
package abstrys.joculus;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

final class ActionPanel extends JPanel
{
   JLabel label_wc = null;

   public ActionPanel(final Joculus app)
   {
      //setBackground(Settings.action_panel_color);
      Dimension d = app.settings.window_size_default;

      setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));

      JButton b;

      b = new JButton(getToolbarIcon("refresh"));
      b.setToolTipText(Strings.UI_TOOLBAR_REFRESH_TIP);
      b.addActionListener(new ActionListener() {
         @Override public void actionPerformed(ActionEvent ae) {
            app.refreshDisplay(); }});
      add(b);

      b = new JButton(getToolbarIcon("open"));
      b.setToolTipText(Strings.UI_TOOLBAR_OPEN_TIP);
      // TODO: create a popup menu that displays the last 5 files opened, plus an option to open a new file.
      add(b);

      b = new JButton(getToolbarIcon("edit"));
      b.setToolTipText(Strings.UI_TOOLBAR_EDIT_TIP);
      // TODO: open the configured editor -or- the editor that's specified by the EDITOR environment variable.
      add(b);

      add(Box.createHorizontalGlue());

      label_wc = new JLabel();
      label_wc.setBorder(new EmptyBorder(2,6,2,2));
      add(label_wc);
      label_wc.setVisible(app.settings.display_word_count);

      add(Box.createHorizontalGlue());

      b = new JButton(getToolbarIcon("style"));
      b.setToolTipText(Strings.UI_TOOLBAR_STYLE_TIP);
      // TODO: pop up a menu with the currently configured stylesheets, and the option to manage stylesheets.
      add(b);

      b = new JButton(getToolbarIcon("settings"));
      b.setToolTipText(Strings.UI_TOOLBAR_SETTINGS_TIP);
      b.addActionListener(new ActionListener() {
         @Override public void actionPerformed(ActionEvent ae) {
            ProcessorSettingsDlg d = new ProcessorSettingsDlg(app.settings);
            app.refreshDisplay();
         }
      });
      add(b);

      b = new JButton(getToolbarIcon("about"));
      b.setToolTipText(Strings.UI_TOOLBAR_ABOUT_TIP);
      // TODO: open the about dialog.
      add(b);
   }

   public void setWordCount(int wc)
   {
       label_wc.setText(String.valueOf(wc) + " words");
   }

   public ImageIcon getToolbarIcon(String name)
   {
      ImageIcon i = null;
      try
      {
         BufferedImage bi = ImageIO.read(getClass().getClassLoader().getResource("abstrys/joculus/res/toolbar/" + name + ".png"));
         i = new ImageIcon(bi);
      }
      catch (IOException ex)
      {
         Joculus.showError(ex.getMessage());
      }

      return i;
   }
}
