/**
 * ActionPanel.java
 *
 *    Part of the jOculus project: https://github.com/Abstrys/jOculus
 *
 *    Copyright (C) 2012 by Eron Hennessey
 */
package abstrys.joculus;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import org.xhtmlrenderer.simple.XHTMLPanel;
import org.xhtmlrenderer.simple.extend.XhtmlNamespaceHandler;

class HTMLPanel extends JPanel
{
   private XHTMLPanel ep = null;
   //private JEditorPane ep = null;
   private JScrollPane sp = null;

   /**
    * Default constructor
    */
   public HTMLPanel(Settings s)
   {
      ep = new XHTMLPanel();
      ep.setDoubleBuffered(true);
//      ep.setEditable(false);
//      ep.setContentType("text/html");
      ep.setBorder(new EmptyBorder(10,10,10,10));

      sp = new JScrollPane(ep);
      sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
      sp.setPreferredSize(s.window_size_default);
      sp.setMinimumSize(new Dimension(40, 40));

      this.setLayout(new BorderLayout());
      this.add(sp, BorderLayout.CENTER);
   }

   public void setHTML(String html, String base_url)
   {
      try
      {
         ep.setDocumentFromString(html, base_url, new XhtmlNamespaceHandler());
      }
      catch(org.xhtmlrenderer.util.XRRuntimeException ex)
      {
         Joculus.showError("XML Renderer runtime exception:\n" + ex.getMessage());
      }
   }
}
