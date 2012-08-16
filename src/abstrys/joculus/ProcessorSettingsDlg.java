/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package abstrys.joculus;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * A panel to set the processor settings.
 *
 * @author eronh
 */
class ProcessorSettingsDlg extends JDialog
{
   JTextField name_field;
   JTextField path_field;
   JTextField options_field;
   Settings app_settings;

   public ProcessorSettingsDlg(Settings settings)
   {
      app_settings = settings;
      this.setLayout(new BorderLayout());
      add(createSettingsPanel(), BorderLayout.CENTER);
      add(createButtonsPanel(), BorderLayout.SOUTH);
      this.pack();
      this.setVisible(true);
      this.setResizable(false);
      this.setTitle(Strings.PROCESSOR_SETTINGS_DIALOG_TITLE);
   }

   private JPanel createSettingsPanel()
   {
      JPanel jp = new JPanel();
      jp.setBorder(new EmptyBorder(10, 20, 10, 20));
      BoxLayout bl = new BoxLayout(jp, BoxLayout.PAGE_AXIS);

      jp.setLayout(bl);

      JComponent cp = new JLabel(Strings.UI_PROC_NAME_LABEL);
      setItemAlignment(cp);
      jp.add(cp);

      JPanel ip = new JPanel(new FlowLayout(FlowLayout.LEFT));
      name_field = new JTextField(18);
      ip.add(name_field);
      setItemAlignment(ip);
      jp.add(ip);

      jp.add(Box.createVerticalStrut(10));

      cp = new JLabel(Strings.UI_PROC_PATH_LABEL);
      setItemAlignment(cp);
      jp.add(cp);

      ip = new JPanel(new FlowLayout(FlowLayout.LEFT));
      path_field = new JTextField(40);
      ip.add(path_field);
      JButton b = new JButton("Browse");
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
                  path_field.setForeground(Color.BLACK);
               }
               else
               {
                  path_field.setForeground(Color.RED);
                  path_field.setText(file.getAbsolutePath() + " [" + Strings.ERROR_INVALID_PATH + "]");
               }
               path_field.setText(file.getAbsolutePath());
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
      options_field = new JTextField(40);
      ip.add(options_field);
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
      app_settings.md_processor_name = name_field.getText();
      app_settings.md_processor_path = path_field.getText();
      app_settings.md_processor_opt = options_field.getText();
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
      name_field.setText(s.md_processor_name);
      path_field.setText(s.md_processor_path);
      options_field.setText(s.md_processor_opt);
   }

   private void setDefaults()
   {
      // create a new instance of the Settings class to get the default values.
      Settings s = new Settings();
      reset(s);
   }
}
