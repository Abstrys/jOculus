/**
 * ActionPanel.java
 *
 * Part of the jOculus project: https://github.com/Abstrys/jOculus
 *
 * Copyright (C) 2012 by Eron Hennessey
 */
package abstrys.joculus;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

final class ActionPanel extends JPanel
{
   JLabel label_wc = null;

   public ActionPanel(final Joculus app)
   {
      //setBackground(Settings.action_panel_color);
      Dimension d = app.settings.window_size_default;

      setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));

      // Setting an empty border on a button removes its decoration.
      final EmptyBorder btn_border = new EmptyBorder(4, 4, 4, 4);

      JButton b;

      b = new JButton(getToolbarIcon("refresh", this));
      b.setToolTipText(Strings.UI_TOOLBAR_REFRESH_TIP);
      b.setBorder(btn_border);
      b.addActionListener(new ActionListener()
      {
         @Override
         public void actionPerformed(ActionEvent ae)
         {
            app.refreshDisplay();
         }
      });
      add(b);

      b = new JButton(getToolbarIcon("open", this));
      b.setToolTipText(Strings.UI_TOOLBAR_OPEN_TIP);
      b.setBorder(btn_border);
      b.addActionListener(new ActionListener()
      {
         @Override
         public void actionPerformed(ActionEvent ae)
         {
            actionOpenFile(app);
         }
      });
      add(b);

      b = new JButton(getToolbarIcon("edit", this));
      b.setToolTipText(Strings.UI_TOOLBAR_EDIT_TIP);
      b.setBorder(btn_border);
      b.addActionListener(new ActionListener()
      {
         @Override
         public void actionPerformed(ActionEvent ae)
         {
            actionEditFile(app);
         }
      });
      // TODO: open the configured editor -or- the editor that's specified by the EDITOR environment variable.
      add(b);

      add(Box.createHorizontalGlue());

      label_wc = new JLabel();
      label_wc.setBorder(new EmptyBorder(4, 6, 4, 6));
      add(label_wc);
      label_wc.setVisible(app.settings.display_word_count);

      add(Box.createHorizontalGlue());

      b = new JButton(getToolbarIcon("style", this));
      b.setToolTipText(Strings.UI_TOOLBAR_STYLE_TIP);
      b.setBorder(btn_border);
      // TODO: pop up a menu with the currently configured stylesheets, and the option to manage stylesheets.
      add(b);

      b = new JButton(getToolbarIcon("settings", this));
      b.setToolTipText(Strings.UI_TOOLBAR_SETTINGS_TIP);
      b.setBorder(btn_border);
      b.addActionListener(new ActionListener()
      {
         @Override
         public void actionPerformed(ActionEvent ae)
         {
            SettingsDlg d = new SettingsDlg(app.settings);
            app.refreshDisplay();
         }
      });
      add(b);

      b = new JButton(getToolbarIcon("about", this));
      b.setToolTipText(Strings.UI_TOOLBAR_ABOUT_TIP);
      b.setBorder(btn_border);
      b.addActionListener(new ActionListener()
      {
         @Override
         public void actionPerformed(ActionEvent ae)
         {
            AboutDlg d = new AboutDlg();
         }
      });
      add(b);
   }

   public void setWordCount(int wc)
   {
      label_wc.setText(String.valueOf(wc) + " words");
   }

   public void actionEditFile(Joculus app)
   {
      // use the environment?
      if (!app.settings.editor_use_env)
      {
         // use the user-specified editor (if it was specified).
         File f = new File(app.settings.editor_path);
         if (!f.exists() || !f.canExecute())
         {
            app.showError(Strings.ERROR_NO_TEXT_EDITOR);
            return;
         }
         else
         {
            if (app.os_type == Joculus.OSType.OS_MacOSX)
            {
               String[] cmdarray = new String[]
               {
                  "/usr/bin/open", "-a", app.settings.editor_path, app.cur_file.getAbsolutePath()
               };
               try
               {
                  Runtime.getRuntime().exec(cmdarray);
               }
               catch (IOException ex)
               {
                  app.showError(ex.getMessage());
               }
            }
         }
      }
      else if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.EDIT))
      {
         // try using the system-defined editor to edit the file.
         try
         {
            Desktop.getDesktop().edit(app.cur_file);
         }
         catch (IOException exc)
         {
            app.showError(exc.getMessage());
         }
      }
   }

   public void actionOpenFile(Joculus app)
   {
      JFileChooser fc = new JFileChooser();
      fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
      fc.setDialogTitle(Strings.UI_OPEN_MDFILE_LABEL);
      fc.addChoosableFileFilter(new FileNameExtensionFilter(
              "Markdown files (.md, .mmd, .markdown, .txt, .text)", "md", "mmd", "markdown", "txt", "text"));
      int returnVal = fc.showOpenDialog(app.app_frame);

      if (returnVal == JFileChooser.APPROVE_OPTION)
      {
         File file = fc.getSelectedFile();
         if (file.exists() && file.isFile())
         {
            app.setFile(file.getAbsolutePath());
         }
      }
   }

   public ImageIcon getToolbarIcon(String name, ActionPanel actionPanel)
   {
      ImageIcon i = null;
      try
      {
         BufferedImage bi = ImageIO.read(actionPanel.getClass().getClassLoader().getResource("abstrys/joculus/res/toolbar/" + name + ".png"));
         i = new ImageIcon(bi);
      }
      catch (IOException ex)
      {
         Joculus.showError(ex.getMessage());
      }
      return i;
   }
}
