/**
 * ActionPanel.java
 *
 *    Part of the jOculus project: https://github.com/Abstrys/jOculus
 *
 *    Copyright (C) 2012 by Eron Hennessey
 */
package abstrys.joculus;

import abstrys.joculus.Settings.ExternalProcessor;
import abstrys.joculus.Settings.CSSFile;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListDataListener;

/**
 * A panel to set the processor settings.
 *
 * @author Eron Hennessey <eron@abstrys.com>
 */
class SettingsDlg extends JDialog
{
   JComboBox font_smoothing_selector;
   JComboBox processor_name_selector;
   JTextField processor_path_field;
   JTextField processor_options_field;
   JComboBox css_file_selector;
   JTextField css_file_path_field;
   JTextField editor_path_field;
   JCheckBox editor_use_env_checkbox;
   EmptyBorder default_border;
   ImageIcon folder_icon;
   Settings dialog_settings;
   Joculus app_instance;

   public SettingsDlg(Joculus j)
   {
      super();
      app_instance = j;

      // read the settings from the preferences, or use defaults.
      dialog_settings = new Settings();
      dialog_settings.load();

      // set up the dialog.
      this.setLayout(new BorderLayout());
      default_border = new EmptyBorder(10, 20, 10, 20);
      JTabbedPane tab_pane = new JTabbedPane();

      // get the folder icon to use for the browse buttons.
      try
      {
         folder_icon = new ImageIcon(
            ImageIO.read(this.getClass().getClassLoader().getResource("abstrys/joculus/res/toolbar/open.png")));
      }
      catch (IOException ex)
      {
         Joculus.showError(ex.getMessage());
      }

      tab_pane.addTab(UIStrings.SETTINGS_DIALOG_GENERAL_TITLE, createGeneralSettingsPanel());
      tab_pane.addTab(UIStrings.SETTINGS_DIALOG_CSS_TITLE, createCSSSettingsPanel());
      tab_pane.addTab(UIStrings.SETTINGS_DIALOG_MARKDOWN_PROCESSOR_TITLE, createProcessorSettingsPanel());

      this.add(tab_pane, BorderLayout.CENTER);
      add(createButtonsPanel(), BorderLayout.SOUTH);
      this.pack();
      this.setVisible(true);
      this.setResizable(false);
      this.setTitle(UIStrings.SETTINGS_DIALOG_TITLE);

      reset();
   }

   private JPanel createGeneralSettingsPanel()
   {
      JPanel jp = new JPanel();
      jp.setBorder(default_border);
      BoxLayout bl = new BoxLayout(jp, BoxLayout.PAGE_AXIS);
      jp.setLayout(bl);

      JPanel ip; // inner panel used for layout.

     ip = new JPanel();
     ip.add(new JLabel("Font Smoothing"));
     font_smoothing_selector = new JComboBox();
     font_smoothing_selector.addItem(RenderingHints.VALUE_TEXT_ANTIALIAS_DEFAULT);
     font_smoothing_selector.addItem(RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
     font_smoothing_selector.addItem(RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
     font_smoothing_selector.addItem(RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
     font_smoothing_selector.addItem(RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HBGR);
     font_smoothing_selector.addItem(RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
     font_smoothing_selector.addItem(RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_VBGR);
     font_smoothing_selector.addItem(RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_VRGB);

     ip.add(font_smoothing_selector);
     font_smoothing_selector.setSelectedItem(dialog_settings.getFontSmoothingValue());
     setItemAlignment(ip);
     jp.add(ip);

     editor_use_env_checkbox = new JCheckBox(UIStrings.SETTINGS_DIALOG_EDITOR_USE_ENV, !dialog_settings.editor_use_env);
     final JButton browse_button = new JButton(folder_icon);
     editor_use_env_checkbox.addActionListener(new ActionListener()
     {
         @Override
         public void actionPerformed(ActionEvent ae)
         {
            editor_path_field.setEnabled(!editor_use_env_checkbox.isSelected());
            browse_button.setEnabled(!editor_use_env_checkbox.isSelected());
          }
      });
      jp.add(editor_use_env_checkbox);

      ip = new JPanel();
      ip.add(new JLabel(UIStrings.SETTINGS_DIALOG_EDITOR_PATH_LABEL));
      editor_path_field = new JTextField(18);
      editor_path_field.setText(dialog_settings.editor_path);
      ip.add(editor_path_field);
      browse_button.setToolTipText(UIStrings.SETTINGS_DIALOG_EDITOR_BROWSE_TOOLTIP);
      browse_button.addActionListener(new ActionListener()
      {
         @Override
         public void actionPerformed(ActionEvent ae)
         {
            JFileChooser fc = new JFileChooser();
            fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fc.setDialogTitle(UIStrings.UI_OPEN_EDITOR_LABEL);
            fc.setFileFilter(Utility.getExecutableFileFilter());
            int returnVal = fc.showOpenDialog(null);

            if (returnVal == JFileChooser.APPROVE_OPTION)
            {
               editor_path_field.setText(fc.getSelectedFile().getAbsolutePath());
            }
         }
      });
      ip.add(browse_button);
      setItemAlignment(ip);
      jp.add(ip);

      editor_use_env_checkbox.doClick();
      return jp;
   }

   private JPanel createCSSSettingsPanel()
   {
      final JPanel jp = new JPanel();
      jp.setBorder(default_border);
      BoxLayout bl = new BoxLayout(jp, BoxLayout.PAGE_AXIS);
      jp.setLayout(bl);

      JComponent cp = new JLabel(UIStrings.UI_CSS_NAME_LABEL);
      setItemAlignment(cp);
      jp.add(cp);

      JPanel ip = new JPanel();
      css_file_selector = new JComboBox();
      css_file_selector.addItemListener(new ItemListener() {
         @Override
         public void itemStateChanged(ItemEvent ie)
         {
            int type = ie.getStateChange();
            switch(type)
            {
               case ItemEvent.ITEM_STATE_CHANGED:
               {
                  CSSFile cf = (CSSFile)css_file_selector.getSelectedItem();
                  css_file_path_field.setText(cf.path);
                  break;
               }
               case ItemEvent.DESELECTED:
               {
                  CSSFile ep2 = (CSSFile)css_file_selector.getSelectedItem();
                  CSSFile ep = (CSSFile)ie.getItem();
               }
            }
         }
      });
      css_file_selector.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent ae)
         {
            String cmd = ae.getActionCommand();
            if(cmd.equals("comboBoxChanged"))
            {
               CSSFile cf = (CSSFile)css_file_selector.getSelectedItem();
               if(cf != null)
               {
                   css_file_path_field.setText(cf.path);
                   css_file_path_field.setEnabled(true);
               }
               else
               {
                   css_file_path_field.setText("no CSS file selected");
                   css_file_path_field.setEnabled(false);                   
               }
            }
         }
      });
      Dimension d = css_file_selector.getPreferredSize();
      d.width = 160;
      css_file_selector.setPreferredSize(d);
      css_file_selector.setEditable(true);
      ip.add(css_file_selector);

      JButton b = new JButton("+");
      b.setToolTipText(UIStrings.SETTINGS_DIALOG_CSS_FILE_ADD);
      b.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent ae)
         {
            CSSFile new_css_file = dialog_settings.newCSSFile(UIStrings.SETTINGS_DIALOG_DEFAULT_NEW_CSS_VALUES[0]);
            new_css_file.path = UIStrings.SETTINGS_DIALOG_DEFAULT_NEW_CSS_VALUES[1];
            css_file_selector.addItem(new_css_file);
            css_file_selector.setSelectedItem(new_css_file);
         }
      });
      d = b.getPreferredSize();
      d.width = 20;
      b.setPreferredSize(d);
      ip.add(b);

      b = new JButton("-");
      b.setToolTipText(UIStrings.SETTINGS_DIALOG_CSS_FILE_REMOVE);
      b.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent ae)
         {
            CSSFile cf = (CSSFile)css_file_selector.getSelectedItem();
            int result = JOptionPane.showConfirmDialog(jp, "Remove CSS File: " + cf.name + "?", UIStrings.APPNAME, JOptionPane.YES_NO_OPTION);
            if(result == JOptionPane.NO_OPTION)
            {
               return;
            }
            css_file_selector.removeItem(cf);
            css_file_selector.setSelectedIndex(0);
         }
      });
      b.setPreferredSize(d);
      ip.add(b);
      ip.add(Box.createHorizontalGlue());
      setItemAlignment(ip);
      jp.add(ip);

      jp.add(Box.createVerticalStrut(10));

      cp = new JLabel(UIStrings.UI_CSS_PATH_LABEL);
      setItemAlignment(cp);
      jp.add(cp);

      ip = new JPanel(new FlowLayout(FlowLayout.LEFT));
      css_file_path_field = new JTextField(40);
      css_file_path_field.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent ae)
         {
            CSSFile cf = (CSSFile)css_file_selector.getSelectedItem();
            cf.path = css_file_path_field.getText();
         }
      });
      ip.add(css_file_path_field);
      b = new JButton(folder_icon);
      b.setToolTipText(UIStrings.SETTINGS_DIALOG_CSS_BROWSE_TOOLTIP);
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
                  css_file_path_field.setForeground(Color.BLACK);
               }
               else
               {
                  css_file_path_field.setForeground(Color.RED);
                  css_file_path_field.setText(file.getAbsolutePath() + " [" + UIStrings.ERROR_INVALID_PATH + "]");
               }
               css_file_path_field.setText(file.getAbsolutePath());
            }
            else
            {
            }
         }
      });
      ip.add(b);
      setItemAlignment(ip);
      jp.add(ip);

      return jp;
   }

   private JPanel createProcessorSettingsPanel()
   {
      final JPanel jp = new JPanel();
      jp.setBorder(default_border);
      BoxLayout bl = new BoxLayout(jp, BoxLayout.PAGE_AXIS);
      jp.setLayout(bl);

      JComponent cp = new JLabel(UIStrings.UI_PROC_NAME_LABEL);
      setItemAlignment(cp);
      jp.add(cp);

      JPanel ip = new JPanel();
      bl = new BoxLayout(ip, BoxLayout.LINE_AXIS);
      processor_name_selector = new JComboBox();
      processor_name_selector.setModel(new DefaultComboBoxModel());
      processor_name_selector.setEditable(true);
      Dimension d = processor_name_selector.getPreferredSize();
      d.width = 160;
      processor_name_selector.setPreferredSize(d);
      processor_name_selector.addItemListener(new ItemListener() {
         @Override
         public void itemStateChanged(ItemEvent ie)
         {
            int type = ie.getStateChange();
            switch(type)
            {
               case ItemEvent.ITEM_STATE_CHANGED:
               {
                  ExternalProcessor ep = (ExternalProcessor)processor_name_selector.getSelectedItem();
                  processor_path_field.setText(ep.path);
                  processor_options_field.setText(ep.options);
                  break;
               }
               case ItemEvent.DESELECTED:
               {
                  ExternalProcessor ep2 = (ExternalProcessor)processor_name_selector.getSelectedItem();
                  ExternalProcessor ep = (ExternalProcessor)ie.getItem();
               }
            }
         }
      });
      processor_name_selector.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent ae)
         {
            String cmd = ae.getActionCommand();
            if(cmd.equals("comboBoxChanged"))
            {
               ExternalProcessor ep = (ExternalProcessor)processor_name_selector.getSelectedItem();
               processor_path_field.setText(ep.path);
               processor_options_field.setText(ep.options);
            }
         }
      });
      ip.add(processor_name_selector);

      JButton b = new JButton("+");
      b.setToolTipText(UIStrings.SETTINGS_DIALOG_MARKDOWN_PROCESSOR_ADD);
      b.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent ae)
         {
            ExternalProcessor new_ep = dialog_settings.newProcessor(UIStrings.SETTINGS_DIALOG_DEFAULT_NEW_PROC_VALUES[0]);
            new_ep.path = UIStrings.SETTINGS_DIALOG_DEFAULT_NEW_PROC_VALUES[1];
            new_ep.options = UIStrings.SETTINGS_DIALOG_DEFAULT_NEW_PROC_VALUES[2];
            processor_name_selector.addItem(new_ep);
            processor_name_selector.setSelectedItem(new_ep);
            processor_name_selector.setEnabled(true);
         }
      });
//      d = b.getPreferredSize();
//      d.width = 20;
//      b.setPreferredSize(d);
      ip.add(b);

      b = new JButton("-");
      b.setToolTipText(UIStrings.SETTINGS_DIALOG_MARKDOWN_PROCESSOR_REMOVE);
      b.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent ae)
         {
            ExternalProcessor ep = (ExternalProcessor)processor_name_selector.getSelectedItem();
            int result = JOptionPane.showConfirmDialog(jp, "Remove Processor: " + ep.name + "?", UIStrings.APPNAME, JOptionPane.YES_NO_OPTION);
            if(result == JOptionPane.NO_OPTION)
            {
               return;
            }
            processor_name_selector.removeItem(ep);
            processor_name_selector.setSelectedIndex(0);
            if(processor_name_selector.getItemCount() == 0)
            {
               processor_name_selector.setEnabled(false);
            }
         }
      });
      ip.add(b);
      ip.add(Box.createHorizontalGlue());
      setItemAlignment(ip);
      jp.add(ip);

      jp.add(Box.createVerticalStrut(10));

      cp = new JLabel(UIStrings.UI_PROC_PATH_LABEL);
      setItemAlignment(cp);
      jp.add(cp);

      ip = new JPanel(new FlowLayout(FlowLayout.LEFT));
      processor_path_field = new JTextField(40);
      processor_path_field.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent ae)
         {
            ExternalProcessor ep = (ExternalProcessor)processor_name_selector.getSelectedItem();
            ep.path = processor_path_field.getText();
         }
      });
      ip.add(processor_path_field);
      b = new JButton(folder_icon);
      b.setToolTipText(UIStrings.SETTINGS_DIALOG_MARKDOWN_PROCESSOR_BROWSE_TOOLTIP);
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
                  processor_path_field.setText(file.getAbsolutePath() + " [" + UIStrings.ERROR_INVALID_PATH + "]");
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

      cp = new JLabel(UIStrings.UI_PROC_OPTIONS_LABEL);
      setItemAlignment(cp);
      jp.add(cp);

      ip = new JPanel(new FlowLayout(FlowLayout.LEFT));
      processor_options_field = new JTextField(40);
      processor_options_field.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent ae)
         {
            ExternalProcessor ep = (ExternalProcessor)processor_name_selector.getSelectedItem();
            ep.options = processor_options_field.getText();
         }
      });
      ip.add(processor_options_field);
      setItemAlignment(ip);
      jp.add(ip);

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

      b = new JButton(UIStrings.UI_SAVE_LABEL);
      b.addActionListener(new ActionListener()
      {
         @Override
         public void actionPerformed(ActionEvent ae)
         {
            saveSettings();
         }
      });
      jp.add(b);

      b = new JButton(UIStrings.UI_CANCEL_LABEL);
      b.addActionListener(new ActionListener()
      {
         @Override
         public void actionPerformed(ActionEvent ae)
         {
            cancel();
         }
      });
      jp.add(b);

      b = new JButton(UIStrings.UI_RESET_LABEL);
      b.addActionListener(new ActionListener()
      {
         @Override
         public void actionPerformed(ActionEvent ae)
         {
            reset();
         }
      });
      jp.add(b);

      b = new JButton(UIStrings.UI_USEDEFAULTS_LABEL);
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
      // Font Smoothing
      dialog_settings.setFontSmoothingValue(font_smoothing_selector.getSelectedItem());

      // Editor Settings
      dialog_settings.editor_use_env = editor_use_env_checkbox.isSelected();
      dialog_settings.editor_path = editor_path_field.getText();

      // Style Sheets
      if(css_file_selector.getItemCount() > 0)
      {
         CSSFile cf = (CSSFile)(css_file_selector.getSelectedItem());
         dialog_settings.css_files.clear();
         int x = css_file_selector.getItemCount();
         for(int i = 0; i < x; i++)
         {
            cf = (CSSFile)(css_file_selector.getItemAt(i));
            dialog_settings.css_files.add(cf);
         }
         dialog_settings.cur_css_file = (CSSFile)(css_file_selector.getSelectedItem());
      }

      // Markdown Processor
      ExternalProcessor ep = (ExternalProcessor)(processor_name_selector.getSelectedItem());
      dialog_settings.md_processors.clear();
      int x = processor_name_selector.getItemCount();
      for(int i = 0; i < x; i++)
      {
         ep = (ExternalProcessor)(processor_name_selector.getItemAt(i));
         dialog_settings.md_processors.add(ep);
      }
      dialog_settings.cur_processor = (ExternalProcessor)(processor_name_selector.getSelectedItem());

      Joculus.settings = dialog_settings;
      Joculus.settings.save();
      app_instance.refreshDisplay();
      this.dispose();
   }

   private void cancel()
   {
      this.dispose();
   }

   /*
    * Reset the dialog fields to the currently-stored values.
    */
   private void reset()
   {
      font_smoothing_selector.setSelectedItem(dialog_settings.getFontSmoothingValue());

      editor_path_field.setText(dialog_settings.editor_path);
      if(editor_use_env_checkbox.isSelected() != dialog_settings.editor_use_env)
      {
         editor_use_env_checkbox.doClick();
      }

      processor_name_selector.removeAllItems();
      for(Settings.ExternalProcessor ep : dialog_settings.md_processors)
      {
         processor_name_selector.addItem(ep);
      }
      processor_name_selector.setSelectedItem(dialog_settings.cur_processor);

      processor_path_field.setText(dialog_settings.cur_processor.path);
      processor_options_field.setText(dialog_settings.cur_processor.options);

      css_file_selector.removeAllItems();
      for(Settings.CSSFile cf : dialog_settings.css_files)
      {
         css_file_selector.addItem(cf);
      }
      if(dialog_settings.cur_css_file != null)
      {
         css_file_selector.setSelectedItem(dialog_settings.cur_css_file);
         css_file_path_field.setText(dialog_settings.cur_css_file.path);
      }
   }

   private void setDefaults()
   {
      dialog_settings = new Settings();
      reset();
   }

}
