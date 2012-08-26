/**
 * ActionPanel.java
 *
 * Part of the jOculus project: https://github.com/Abstrys/jOculus
 *
 * Copyright (C) 2012 by Eron Hennessey
 */
package abstrys.joculus;

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
}
