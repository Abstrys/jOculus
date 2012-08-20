#!/bin/sh

echo "installing jOculus..."
mkdir ~/bin
cp joculus ~/bin
cp jOculus.jar ~/bin
mkdir ~/bin/lib
cp lib/* ~/bin/lib
echo "all done!"
echo
echo "To run jOculus, simply type:"
echo
echo "$ joculus <filename>"


