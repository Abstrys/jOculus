package abstrys.antiword;

import javax.swing.JPanel;
import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import java.awt.Dimension;
import java.awt.BorderLayout;

class HTMLPanel extends JPanel
{
   private JEditorPane ep = null;
   private JScrollPane sp = null;

   /**
    * Default constructor
    */
   public HTMLPanel()
   {
      ep = new JEditorPane();
      ep.setEditable(false);
      ep.setContentType("text/html");

      sp = new JScrollPane(ep);
      sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
      sp.setPreferredSize(Settings.default_size);
      sp.setMinimumSize(new Dimension(10, 10));

      this.setLayout(new BorderLayout());
      this.add(sp, BorderLayout.CENTER);
   }

   public void setHTML(String html)
   {
      ep.setText(html);
   }
}