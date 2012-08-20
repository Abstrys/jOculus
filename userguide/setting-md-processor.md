# Setting the Markdown Processor

By default, jOculus uses [pandoc][pandoc] as the markdown processor, and expects it to be in `/usr/local/bin`. This
location may or may not be where your `pandoc` installation is and, furthermore, you might want to use a different
markdown processor.

A few to try:

* [Markdown][markdown] - the original Markdown processor, written in Perl by John Gruber. It defines the Markdown standard.

* [Multimarkdown][multimarkdown] - an extended Markdown processor by Fletcher T. Penney.

* [Pandoc][pandoc], an extended Markdown processor, written in Haskell by John MacFarlane. It features quite a variety of input and output formats and many options for building output files.

Any of these, or any other processors that can be run from the command-line, can be used with jOculus by configuring them with the **Markdown Processor Settings** dialog.

**To set the Markdown Processor:**

1. Click the **Settings** button in the action menu. This will launch the **Markdown Processor Settings** dialog:

    ![](http://www.abstrys.com/files/images/jOculus-processor-settings-dlg.png)

2. In the **Processor name** field, set the name of your Markdown processor. This can be anything you want; it serves only to identify the Markdown processor.

3. In the **Path to processor** field, type the full path to the Markdown processor you're using. For example: `/usr/bin/markdown`. Alternatively, you can click the **Browse** button to select the Markdown processor with a file browser.

4. In the **Processor options** field, type any options you want to include in the command-line that will be used to process the Markdown into HTML. Some processors may not need (or even support) command-line options; it's fine to leave this field blank.

5. Click **Save** to save your settings. The Markdown processor you set here will be used the next time the main view is updated.

[pandoc]: http://johnmacfarlane.net/pandoc/
[multimarkdown]: http://fletcherpenney.net/multimarkdown/
[markdown]: http://daringfireball.net/projects/markdown/

