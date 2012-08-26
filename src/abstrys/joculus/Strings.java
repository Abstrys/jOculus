/**
 * ActionPanel.java
 *
 *    Part of the jOculus project: https://github.com/Abstrys/jOculus
 *
 *    Copyright (C) 2012 by Eron Hennessey
 */
package abstrys.joculus;

final class Strings
{
   protected static final String APPNAME = "jOculus";
   protected static final String VERSION = "Version 0.2";

   protected static final String ERRORMSG_TITLE = APPNAME + " - Error";
   protected static final String ERROR_BAD_FILEPATH = "A filename was specified, but the file does not exist!";
   protected static final String ERROR_CREATEFILE_FAILED = "Could not create file";
   protected static final String ERROR_NO_FILE_SPECIFIED = "No file specified!";
   protected static final String ERROR_NO_HOME = "Could not find your home directory!";
   protected static final String ERROR_OPENSTREAM_FAILED = "Could not open file stream.";
   protected static final String ERROR_WRITE_SETTINGS_FAILED = "Could not write settings to file.";
   protected static final String ERROR_READ_SETTINGS_FAILED = "Could not read settings from file.";
   protected static final String ERROR_INVALID_HEX_COLOR = "Invalid hexadecimal color value";
   protected static final String ERROR_INVALID_PROCESSOR_PATH = "Invalid processor path. Make sure you have a valid processor path specified in your settings.";
   protected static final String ERROR_INVALID_PATH = "Invalid path";

   protected static final String PROCESSOR_SETTINGS_DIALOG_TITLE = APPNAME + " - Markdown Processor Settings";

   protected static final String UI_CANCEL_LABEL = "Cancel";
   protected static final String UI_PROC_NAME_LABEL = "Processor name";
   protected static final String UI_PROC_OPTIONS_LABEL = "Processor options";
   protected static final String UI_PROC_PATH_LABEL = "Path to processor";
   protected static final String UI_RESET_LABEL = "Reset";
   protected static final String UI_SAVE_LABEL = "Save";
   protected static final String UI_USEDEFAULTS_LABEL = "Use Defaults";
   protected static final String UI_TOOLBAR_OPEN_TIP = "Open a file";
   protected static final String UI_TOOLBAR_EDIT_TIP = "Edit the displayed file";
   protected static final String UI_TOOLBAR_REFRESH_TIP = "Refresh the display";
   protected static final String UI_TOOLBAR_SETTINGS_TIP = "Modify settings";
   protected static final String UI_TOOLBAR_ABOUT_TIP = "About " + APPNAME;
   protected static final String UI_TOOLBAR_STYLE_TIP = "Change the style sheet";
   protected static final String UI_OPEN_MDFILE_LABEL = "Choose a Markdown File to Open";

   protected static final String XHTML_DECL = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n<html xmlns=\"http://www.w3.org/1999/xhtml\">";
   protected static final String XHTML_HEAD_BEGIN = "<head>";
   protected static final String XHTML_HEAD_END = "</head>";
   protected static final String XHTML_BODY_BEGIN = "<body>";
   protected static final String XHTML_END = "</body></html>";

   protected static final String ABOUT_TAGLINE = "<i>A Cross-platform, auto-updating Markdown viewer</i>";
   protected static final String ABOUT_COPYRIGHT = "<p>Copyright © Eron Hennessey, 2012</p>";
   protected static final String ABOUT_WRITTEN_BY = "<p>Written by Eron Hennessey</p>";
   protected static final String ABOUT_INCLUDES = "<p>Includes HTML rendering technology from the <a href=\"https://github.com/flyingsaucerproject/flyingsaucer\">Flying Saucer</a> project.</p>";
   protected static final String ABOUT_MOREINFO = "<p>For more information about jOculus or for software updates, visit:</p> <p><a href=\"https://github.com/Abstrys/jOculus\">https://github.com/Abstrys/jOculus</a></p>";
}

