# <img src="http://www.abstrys.com/files/joculus/jOculus-logo-128.png" width="128" height="128" align="right"/> jOculus

## A cross-platform, auto-updating Markdown and HMTL viewer

-----

jOculus provides a formatted view of a [Markdown][md] or HTML document that is automatically updated every time the file is saved.

It is written in Java, which allows it to run similarly on any system with Java 6 installed.

For additional information, updates, bug reports, or if you'd like to contribute to the project, go to <https://github.com/Abstrys/jOculus>, where you can also find the latest [pre-built binaries][downloads] and [documentation][docs].


## Copyright, license and disclaimers

This software and its associated documents, unless otherwise specified, are Copyright &copy; Eron Hennessey, 2012.

jOculus is *free software*: You have license to use this software without any restrictions, and to copy or modify it in accordance with the [GNU General Public License, version 3][gpl] (GPL3). A copy of the license is provided with the jOculus source distribution, in the file titled `LICENSE.txt`.

This software is provided **as is** without warranty of any kind, expressed or implied. This includes, but is not limited to, any implied warranties of merchantability or fitness for a particular purpose.

## Additional components

jOculus uses the following Java libraries:

<table width="90%">
    <tr><th>Library</th><th>Website</th></tr>
    <tr>
        <td>flyingsaucer-R8</td>
        <td><a href="http://code.google.com/p/flying-saucer/">http://code.google.com/p/flying-saucer/</a></td>
    </tr>
    <tr>
        <td>iText-2.0.8</td>
        <td><a href="http://itextpdf.com/">http://itextpdf.com/</a></td>
    </tr>
</table>

I have much gratitude and kudos for the creators, contributors and maintainers of these components, as well as for much of the software I use daily--without them, jOculus would have been much more difficult to write. Together, we can help each other to make great things, and *that*, to me, is the real beauty of open-source software. :)

Built versions of these libraries are included with the jOculus distribution.  If you'd like to compile these from source, or if you'd like updated versions of these libraries, please refer to the website associated with each library.

## Building jOculus

### Prerequisites

To build, jOculus requires a Java JDK that supports at least Java SE 6, and Apache Ant.

On a Debian-based Linux system such as Ubuntu, you can do the following:

    $ sudo apt-get install openjdk-6-jdk
    $ sudo apt-get install ant

You will also need JAVA_HOME set to the location of your JDK installation.

Again, on Ubuntu, this is usually `/usr/lib/jre/default-java`, so adding this line in your `~/.profile` will work:

    export JAVA_HOME=/usr/lib/jre/default-java

> **Note**: It's important that your JDK and JRE are the same version, or at least, that your JRE is more recent than the JDK that built the binary. If not, you'll run into trouble when you run jOculus...

For more information about obtaining and installing the correct version of Java and Ant for your platform, see the [Java][javadl] and [Ant][antdl] sites.

### Building

**To build jOculus**

1. Open a terminal window, and cd to the location of the jOculus sources (the same directory as this README; it also includes `build.xml`, the file that Apache Ant uses to build the application).

2. At the command-prompt, type:

        $ ant

The built .jar will be in the `dist` directory, and is called `jOculus.jar`.

## Installing jOculus

I recommend that you install jOculus in a private `bin` directory within your home directory. If you do, and you are in a bash command-line environment, you can also copy the `joculus` bash script from the `shell` directory.

A bash script called `install.sh` resides in the `dist` directory after you build jOculus. It performs the following actions:

    mkdir ~/bin
    cp joculus ~/bin
    cp jOculus.jar ~/bin
    mkdir ~/bin/lib
    cp lib/* ~/bin/lib

If you'd like to use `install.sh`, simply execute the following commands after you build jOculus:

    $ cd dist
    $ ./install.sh

otherwise, you can copy the files in dist to any location you like, provided that the `lib` directory is in the same directory as `jOculus.jar`.

## Running jOculus

If you installed jOculus with the bash script, you can simply run:

    $ joculus myfile.md

Where *myfile.md* is the file to show in the viewer window (it can be any text, Markdown, or XHTML-compliant file).

Otherwise, you'll need to run it with the `java` command, like this:

    $ java -jar ~/bin/jOculus.jar myfile.md &

I hope that jOculus helps to streamline your writing... but most of all, I hope that you have fun!

## Contact Info

<table width="90%">
    <tr><th>Contributor</th><th>E-mail</th><th>Website</th></tr>
    <tr>
        <td>Eron Hennessey</td>
        <td><a href="mailto:eron@abstrys.com">eron@abstrys.com</a></td>
        <td><a href="http://www.abstrys.com">http://www.abstrys.com</a></td>
    </tr>
</table>

[javadl]: http://www.oracle.com/technetwork/java/javase/downloads/index.html
[antdl]: http://ant.apache.org/bindownload.cgi
[md]: http://daringfireball.net/projects/markdown/
[downloads]: https://github.com/Abstrys/jOculus/downloads
[docs]: https://github.com/Abstrys/jOculus/tree/master/userguide
[gpl]: http://www.gnu.org/licenses/gpl.html
