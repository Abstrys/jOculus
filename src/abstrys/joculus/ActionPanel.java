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
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

final class ActionPanel extends JPanel
{
   JLabel label_wc = null;
   Joculus app_instance = null;
   SettingsDlg settings_dlg = null;

   public ActionPanel(final Joculus app)
   {
      this.app_instance = app;
      setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));

      // Setting an empty border on a button removes its decoration.
      final EmptyBorder btn_border = new EmptyBorder(4, 4, 4, 4);

      JButton b;

      b = new JButton(getToolbarIcon("refresh", this));
      b.setToolTipText(UIStrings.UI_TOOLBAR_REFRESH_TIP);
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
      b.setToolTipText(UIStrings.UI_TOOLBAR_OPEN_TIP);
      b.setBorder(btn_border);
      b.addActionListener(new ActionListener()
      {
         @Override
         public void actionPerformed(ActionEvent ae)
         {
            actionOpenFile();
         }
      });
      add(b);

      b = new JButton(getToolbarIcon("edit", this));
      b.setToolTipText(UIStrings.UI_TOOLBAR_EDIT_TIP);
      b.setBorder(btn_border);
      b.addActionListener(new ActionListener()
      {
         @Override
         public void actionPerformed(ActionEvent ae)
         {
            actionEditFile();
         }
      });
      // TODO: open the configured editor -or- the editor that's specified by the EDITOR environment variable.
      add(b);

      add(Box.createHorizontalGlue());

      label_wc = new JLabel();
      label_wc.setBorder(new EmptyBorder(4, 6, 4, 6));
      add(label_wc);

      boolean display_wc = true;
      label_wc.setVisible(Joculus.settings.display_word_count);

      add(Box.createHorizontalGlue());

      b = new JButton(getToolbarIcon("style", this));
      b.setToolTipText(UIStrings.UI_TOOLBAR_STYLE_TIP);
      b.setBorder(btn_border);
      // TODO: pop up a menu with the currently configured stylesheets, and the option to manage stylesheets.
      add(b);

      b = new JButton(getToolbarIcon("settings", this));
      b.setToolTipText(UIStrings.UI_TOOLBAR_SETTINGS_TIP);
      b.setBorder(btn_border);
      b.addActionListener(new ActionListener()
      {
         @Override
         public void actionPerformed(ActionEvent ae)
         {
            if(settings_dlg == null)
            {
                settings_dlg = new SettingsDlg(app);
            }
            else
            {
                settings_dlg.setVisible(true);
            }
         }
      });
      add(b);

      b = new JButton(getToolbarIcon("about", this));
      b.setToolTipText(UIStrings.UI_TOOLBAR_ABOUT_TIP);
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

   public void actionEditFile()
   {
      // use the environment?
      if (!Joculus.settings.editor_use_env)
      {
         // use the user-specified editor (if it was specified).
         File f = new File(Joculus.settings.editor_path);
         if (!f.exists() || !f.canExecute())
         {
            Joculus.showError(UIStrings.ERROR_NO_TEXT_EDITOR);
            return;
         }
         else
         {
            if (Joculus.os_type == Joculus.OSType.OS_MacOSX)
            {
               String[] cmdarray = new String[]
               {
                  "/usr/bin/open", "-a", Joculus.settings.editor_path, app_instance.cur_file.getAbsolutePath()
               };
               try
               {
                  Runtime.getRuntime().exec(cmdarray);
               }
               catch (IOException ex)
               {
                  Joculus.showError(ex.getMessage());
               }
            }
         }
      }
      else if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.EDIT))
      {
         // try using the system-defined editor to edit the file.
         try
         {
            Desktop.getDesktop().edit(app_instance.cur_file);
         }
         catch (IOException exc)
         {
            Joculus.showError(exc.getMessage());
         }
      }
   }

   public void actionOpenFile()
   {
      JFileChooser fc = new JFileChooser();
      fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
      fc.setDialogTitle(UIStrings.UI_OPEN_MDFILE_LABEL);
      fc.addChoosableFileFilter(new FileNameExtensionFilter(
              "Markdown files (.md, .mmd, .markdown, .txt, .text)", "md", "mmd", "markdown", "txt", "text"));
      int returnVal = fc.showOpenDialog(Joculus.frame);

      if (returnVal == JFileChooser.APPROVE_OPTION)
      {
         File file = fc.getSelectedFile();
         if (file.exists() && file.isFile())
         {
            app_instance.setFile(file.getAbsolutePath());
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
