#!/bin/bash

# Create .png file from some latex. This was a very quick hack. Find tex2im and
# use that instead, it's much more polished.
# 
# Iain Murray October 2003

# November 2006. This script doesn't work in the POSIX-complaint dash shell. So
# I changed the shebang line to point to bash.

if [ ! -d .latex2png.tmp ]; then
mkdir .latex2png.tmp
fi
FILE=$1
cp $FILE .latex2png.tmp
cd .latex2png.tmp

latex $FILE
dvips -R -E ${FILE%tex}dvi -o ${FILE%tex}eps
convert -quality 100 -density 150 ps:${FILE%tex}eps ${FILE%tex}png

cp ${FILE%tex}png ..
cd ..
rm -R .latex2png.tmp
