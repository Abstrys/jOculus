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
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import org.xhtmlrenderer.simple.FSScrollPane;
import org.xhtmlrenderer.simple.XHTMLPanel;
import org.xhtmlrenderer.simple.extend.XhtmlNamespaceHandler;
import org.xhtmlrenderer.swing.Java2DTextRenderer;

class HTMLPanel extends JPanel
{
   final static Dimension MIN_SIZE = new Dimension(200, 200);
   final static Dimension DEFAULT_SIZE = new Dimension(400, 400);

   private XHTMLPanel xhtml_panel = null;
   //private JEditorPane xhtml_panel = null;
   private JScrollPane sp = null;

   /**
    * Default constructor
    */
   public HTMLPanel(Dimension size)
   {
      xhtml_panel = new XHTMLPanel();
      xhtml_panel.setDoubleBuffered(true);
//      Java2DTextRenderer tr = (Java2DTextRenderer) xhtml_panel.getSharedContext().getTextRenderer();
//      xhtml_panel.setEditable(false);
//      xhtml_panel.setContentType("text/html");
      xhtml_panel.setBorder(new EmptyBorder(10,10,10,10));

      sp = new FSScrollPane(xhtml_panel);
      sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
      sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
      sp.setPreferredSize(size);
      sp.setMinimumSize(MIN_SIZE);

      this.setLayout(new BorderLayout());
      this.add(sp, BorderLayout.CENTER);
   }

   public void setHTML(String html, String base_url)
   {
      Graphics2D graphics = (Graphics2D) xhtml_panel.getGraphics();
//      graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
      graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, Joculus.settings.getFontSmoothingValue());

      try
      {
         xhtml_panel.setDocumentFromString(html, base_url, new XhtmlNamespaceHandler());
      }
      catch(org.xhtmlrenderer.util.XRRuntimeException ex)
      {
         Joculus.showError("XML Renderer runtime exception:\n" + ex.getMessage());
      }
   }
}
