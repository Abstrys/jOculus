# jOculus - A cross-platform, auto-updating Markdown and HMTL viewer

jOculus provides a formatted view of a Markdown or HTML document that is
automatically updated every time the file is saved.

## License

This software and all associated documents is Copyright (C) Eron Hennessey,
2012.

You have license to use, copy, and modify this software in accordance with the
GNU General Public License, version 3 (GPL3). The GPL3 can be viewed at
<http://www.gnu.org/licenses/gpl.html>. A copy of the license is also provided
in the file titled LICENSE.txt.

## Additional components.

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

Built versions of these libraries are included with the jOculus distribution.
If you'd like to compile these from source, or if you'd like updated versions
of these libraries, please refer to the website associated with each library.

## Building jOculus

### Prerequisites

To build, jOculus requires a Java JDK that supports at least Java SE 6, and
Apache Ant.

On a Debian-based Linux system such as Ubuntu, you can do the following:

    $ sudo apt-get install openjdk-6-jdk
    $ sudo apt-get install ant

You will also need JAVA_HOME set to the location of your JDK installation.

Again, on Ubuntu, this is usually /usr/lib/jre/default-java, so adding this
line in your .profile will work:

    export JAVA_HOME=/usr/lib/jre/default-java

> **Note**: It's important that your JDK and JRE are the same version, or at
> least, that your JRE is more recent than the JDK that built the binary. If
> not, you'll run into trouble when you run jOculus...

For more information about obtaining and installing the correct version of Java
and Ant for your platform, see the [Java][javadl] and [Ant][antdl] sites.

### Building

**To build jOculus**

1. Open a terminal window, and cd to the location of the jOculus sources (the
same directory as this README; it also includes `build.xml`, the file that
Apache Ant uses to build the application).

2. At the command-prompt, type:

        $ ant

The built .jar will be in the `dist` directory, and is called `jOculus.jar`.

## Installing jOculus

I recommend that you install jOculus in a private `bin` directory within your
home directory. If you do, and you are in a bash command-line environment,
you can also copy the `joculus` bash script from the `shell` directory.

A bash script called `install.sh` resides in the `dist` directory after you
build jOculus. It performs the following actions:

    mkdir ~/bin
    cp joculus ~/bin
    cp jOculus.jar ~/bin
    mkdir ~/bin/lib
    cp lib/* ~/bin/lib

If you'd like to use `install.sh`, simply execute the following commands after
you build jOculus:

    $ cd dist
    $ ./install.sh

otherwise, you can copy the files in dist to any location you like, provided
that the `lib` directory is in the same directory as `jOculus.jar`.

## Running jOculus

If you installed jOculus with the bash script, you can simply run:

    $ joculus myfile.md

Where myfile.md is the file to show in the viewer window (it can be any text,
Markdown, or XHTML-compliant file).

Otherwise, you'll need to run it with the java interpreter, like this:

    $ java -jar ~/bin/jOculus.jar myfile.md &

I hope that jOculus helps to streamline your writing... but most of all, I hope
that you have fun!

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
