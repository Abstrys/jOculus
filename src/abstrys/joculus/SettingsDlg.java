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
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * A panel to set the processor settings.
 *
 * @author eronh
 */
class SettingsDlg extends JDialog
{
   JTextField processor_name_field;
   JTextField processor_path_field;
   JTextField processor_options_field;
   JTextField editor_path_field;
   JCheckBox editor_use_env_checkbox;
   Settings app_settings;
   EmptyBorder default_border;
   ImageIcon folder_icon;

   public SettingsDlg(Settings settings)
   {
      app_settings = settings;
      this.setLayout(new BorderLayout());
      default_border = new EmptyBorder(10, 20, 10, 20);
      JTabbedPane tab_pane = new JTabbedPane();

      // get the folder icon to use for the browse buttons.
      try
      {
         folder_icon = new ImageIcon(ImageIO.read(this.getClass().getClassLoader().getResource("abstrys/joculus/res/toolbar/open.png")));
      }
      catch (IOException ex)
      {
         Joculus.showError(ex.getMessage());
      }

      tab_pane.addTab(Strings.SETTINGS_DIALOG_GENERAL_TITLE, createGeneralSettingsPanel());
      tab_pane.addTab(Strings.SETTINGS_DIALOG_CSS_TITLE, createCSSSettingsPanel());
      tab_pane.addTab(Strings.SETTINGS_DIALOG_MARKDOWN_PROCESSOR_TITLE, createProcessorSettingsPanel());

      this.add(tab_pane, BorderLayout.CENTER);
      add(createButtonsPanel(), BorderLayout.SOUTH);
      this.pack();
      this.setVisible(true);
      this.setResizable(false);
      this.setTitle(Strings.SETTINGS_DIALOG_TITLE);
   }

   private JPanel createGeneralSettingsPanel()
   {
      JPanel jp = new JPanel();
      jp.setBorder(default_border);
      BoxLayout bl = new BoxLayout(jp, BoxLayout.PAGE_AXIS);
      jp.setLayout(bl);

      JPanel ip; // inner panel used for layout.

     editor_use_env_checkbox = new JCheckBox(Strings.SETTINGS_DIALOG_EDITOR_USE_ENV, !app_settings.editor_use_env);
     final JButton browse_button = new JButton(folder_icon);
     editor_use_env_checkbox.addActionListener(new ActionListener() {

         @Override
         public void actionPerformed(ActionEvent ae)
         {
            editor_path_field.setEnabled(!editor_use_env_checkbox.isSelected());
            browse_button.setEnabled(!editor_use_env_checkbox.isSelected());
          }
      });
      jp.add(editor_use_env_checkbox);

      ip = new JPanel();
      ip.add(new JLabel(Strings.SETTINGS_DIALOG_EDITOR_PATH_LABEL));
      editor_path_field = new JTextField(18);
      editor_path_field.setText(app_settings.editor_path);
      ip.add(editor_path_field);
      browse_button.setToolTipText(Strings.SETTINGS_DIALOG_EDITOR_BROWSE_TOOLTIP);
      browse_button.addActionListener(new ActionListener()
      {
         @Override
         public void actionPerformed(ActionEvent ae)
         {
            JFileChooser fc = new JFileChooser();
            fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fc.setDialogTitle(Strings.UI_OPEN_MDFILE_LABEL);
            fc.setFileFilter(Utility.getExecutableFileFilter());
            int returnVal = fc.showOpenDialog(null);

            if (returnVal == JFileChooser.APPROVE_OPTION)
            {
               editor_path_field.setText(fc.getSelectedFile().getAbsolutePath());
            }
         }
      });
      ip.add(browse_button);
      setItemAlignment(jp);
      jp.add(ip);

      editor_use_env_checkbox.doClick();
      return jp;
   }

   private JPanel createCSSSettingsPanel()
   {
      JPanel jp = new JPanel();
      BoxLayout bl = new BoxLayout(jp, BoxLayout.PAGE_AXIS);
      return jp;
   }

   private JPanel createProcessorSettingsPanel()
   {
      JPanel jp = new JPanel();
      jp.setBorder(default_border);
      BoxLayout bl = new BoxLayout(jp, BoxLayout.PAGE_AXIS);

      jp.setLayout(bl);

      JComponent cp = new JLabel(Strings.UI_PROC_NAME_LABEL);
      setItemAlignment(cp);
      jp.add(cp);

      JPanel ip = new JPanel(new FlowLayout(FlowLayout.LEFT));
      processor_name_field = new JTextField(18);
      ip.add(processor_name_field);
      setItemAlignment(ip);
      jp.add(ip);

      jp.add(Box.createVerticalStrut(10));

      cp = new JLabel(Strings.UI_PROC_PATH_LABEL);
      setItemAlignment(cp);
      jp.add(cp);

      ip = new JPanel(new FlowLayout(FlowLayout.LEFT));
      processor_path_field = new JTextField(40);
      ip.add(processor_path_field);
      JButton b = new JButton(folder_icon);
      b.setToolTipText(Strings.SETTINGS_DIALOG_MARKDOWN_PROCESSOR_BROWSE_TOOLTIP);
      b.addActionListener(new ActionListener()
      {
         @Override
         public void actionPerformed(ActionEvent ae)
         {
            JFileChooser fc = new JFileChooser();
            int returnVal = fc.showDialog(null, "Select");

            if (returnVal == JFileChooser.APPROVE_OPTION)
            {
               File file = fc.getSelectedFile();
               if(file.exists() && file.isFile())
               {
                  processor_path_field.setForeground(Color.BLACK);
               }
               else
               {
                  processor_path_field.setForeground(Color.RED);
                  processor_path_field.setText(file.getAbsolutePath() + " [" + Strings.ERROR_INVALID_PATH + "]");
               }
               processor_path_field.setText(file.getAbsolutePath());
            }
            else
            {
            }
         }
      });
      ip.add(b);
      setItemAlignment(ip);
      jp.add(ip);

      jp.add(Box.createVerticalStrut(10));

      cp = new JLabel(Strings.UI_PROC_OPTIONS_LABEL);
      setItemAlignment(cp);
      jp.add(cp);

      ip = new JPanel(new FlowLayout(FlowLayout.LEFT));
      processor_options_field = new JTextField(40);
      ip.add(processor_options_field);
      setItemAlignment(ip);
      jp.add(ip);

      reset(app_settings);

      return jp;
   }

   private void setItemAlignment(JComponent c)
   {
      Dimension d = c.getPreferredSize();
      c.setMaximumSize(d);
      c.setAlignmentX(JComponent.LEFT_ALIGNMENT);
   }

   private JPanel createButtonsPanel()
   {
      JPanel jp = new JPanel();
      jp.setBorder(new EmptyBorder(5,10,10,10));
      JButton b;

      b = new JButton(Strings.UI_SAVE_LABEL);
      b.addActionListener(new ActionListener()
      {
         @Override
         public void actionPerformed(ActionEvent ae)
         {
            saveSettings();
         }
      });
      jp.add(b);

      b = new JButton(Strings.UI_CANCEL_LABEL);
      b.addActionListener(new ActionListener()
      {
         @Override
         public void actionPerformed(ActionEvent ae)
         {
            cancel();
         }
      });
      jp.add(b);

      b = new JButton(Strings.UI_RESET_LABEL);
      b.addActionListener(new ActionListener()
      {
         @Override
         public void actionPerformed(ActionEvent ae)
         {
            reset(app_settings);
         }
      });
      jp.add(b);

      b = new JButton(Strings.UI_USEDEFAULTS_LABEL);
      b.addActionListener(new ActionListener()
      {
         @Override
         public void actionPerformed(ActionEvent ae)
         {
            setDefaults();
         }
      });
      jp.add(b);
      return jp;
   }

   private void saveSettings()
   {
      app_settings.md_processor_name = processor_name_field.getText();
      app_settings.md_processor_path = processor_path_field.getText();
      app_settings.md_processor_opt = processor_options_field.getText();
      app_settings.editor_use_env = editor_use_env_checkbox.isSelected();
      app_settings.editor_path = editor_path_field.getText();

      app_settings.save();
      this.dispose();
   }

   private void cancel()
   {
      this.dispose();
   }

   /*
    * Reset all fields to the values in the passed-in Settings.
    */
   private void reset(Settings s)
   {
      processor_name_field.setText(s.md_processor_name);
      processor_path_field.setText(s.md_processor_path);
      processor_options_field.setText(s.md_processor_opt);
   }

   private void setDefaults()
   {
      // create a new instance of the Settings class to get the default values.
      Settings s = new Settings();
      reset(s);
   }

}
