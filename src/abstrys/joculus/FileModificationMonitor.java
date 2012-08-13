/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package abstrys.joculus;

import java.io.File;
import java.util.TimerTask;
import javax.swing.JOptionPane;

/**
 * Monitors a file for changes.
 * @author Eron Hennessey <eron@abstrys.com>
 */
class FileModificationMonitor extends TimerTask
{
   interface ReloadsFile
   {
       public void reloadFile();
   }
   
   private long last_timestamp;
   private File file;
   private ReloadsFile reloader;
   
   public FileModificationMonitor(File file, ReloadsFile reloader)
   {
      this.file = file;
      this.last_timestamp = file.lastModified();
      this.reloader = reloader;
   }

    @Override
   public final void run()
   {
      long cur_timestamp = file.lastModified();

      if(last_timestamp != cur_timestamp)
      {
         notifyChange(file);
         last_timestamp = cur_timestamp;
      }
   }

   protected void notifyChange(File file)
   {
      // reload the document.
      reloader.reloadFile();
   }
}