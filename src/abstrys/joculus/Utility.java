/**
 * ActionPanel.java
 *
 * Part of the jOculus project: https://github.com/Abstrys/jOculus
 *
 * Copyright (C) 2012 by Eron Hennessey
 */
package abstrys.joculus;

import java.io.File;
import javax.swing.filechooser.FileFilter;

/**
 *
 * @author eron
 */
public class Utility
{
   public static String getUserHome()
   {
      String home = System.getProperty("user.home");
      if (home == null)
      {
         Joculus.showError(Strings.ERROR_NO_HOME);
         return null;
      }

      return home;
   }
   
   public static FileFilter getExecutableFileFilter()
   {
      return (new FileFilter() {
         @Override
         public boolean accept(File file)
         {
            return file.canExecute();
         }

         @Override
         public String getDescription()
         {
            return Strings.EXECUTABLE_FILES;
         }
      });
   }
}
