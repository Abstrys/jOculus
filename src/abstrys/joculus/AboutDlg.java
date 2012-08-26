/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package abstrys.joculus;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import org.xhtmlrenderer.simple.FSScrollPane;
import org.xhtmlrenderer.simple.XHTMLPanel;
import org.xhtmlrenderer.simple.extend.XhtmlNamespaceHandler;

/**
 *
 * @author eron
 */
public class AboutDlg extends JDialog
{
   public AboutDlg()
   {
      XHTMLPanel xp = new XHTMLPanel();

      StringBuilder html_content = new StringBuilder();

      // invoke the processor to convert the file.
      html_content.append(Strings.XHTML_DECL);
      html_content.append(Strings.XHTML_HEAD_BEGIN);
      // TODO: add the stylesheet here.
      html_content.append(Strings.XHTML_HEAD_END);
      html_content.append(Strings.XHTML_BODY_BEGIN);
      html_content.append("<h1>" + Strings.APPNAME + "</h1>");
      html_content.append(Strings.ABOUT_TAGLINE);
      html_content.append("<p>" + Strings.VERSION + "</p>");
      html_content.append(Strings.ABOUT_COPYRIGHT);
      html_content.append(Strings.ABOUT_WRITTEN_BY);
      html_content.append(Strings.ABOUT_INCLUDES);
      html_content.append(Strings.ABOUT_MOREINFO);
      html_content.append(Strings.XHTML_END);

      FSScrollPane sp = new FSScrollPane(xp);
      sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
      sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
      sp.setPreferredSize(new Dimension(350, 400));
      sp.setMinimumSize(new Dimension(40, 50));

      this.setLayout(new BorderLayout());
      this.add(sp, BorderLayout.CENTER);

      xp.setDocumentFromString(html_content.toString(), "/", new XhtmlNamespaceHandler());

      JPanel btnPanel = new JPanel();
      JButton b = new JButton("Close");
      b.addActionListener(new ActionListener() {
         @Override public void actionPerformed(ActionEvent ae) {
            closeAbout(); } } );
      btnPanel.add(b);
      this.setTitle(Strings.UI_TOOLBAR_ABOUT_TIP);
      this.add(btnPanel, BorderLayout.SOUTH);
      this.pack();
      this.setVisible(true);
   }

   void closeAbout()
   {
      this.dispose();
   }

}
