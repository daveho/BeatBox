#! /bin/bash

if [ ! -e Beads.zip ]; then
	echo "Downloading Beads library..."
	wget http://www.beadsproject.net/downloads/Beads.zip
	echo "done"
fi

jars='beads-io.jar beads.jar jarjar-1.0.jar jl1.0.1.jar jna.jar mp3spi1.9.4.jar org-jaudiolibs-audioservers-jack.jar org-jaudiolibs-audioservers-javasound.jar org-jaudiolibs-audioservers.jar org-jaudiolibs-jnajack.jar tools.jar tritonus_aos-0.3.6.jar tritonus_share.jar'

mkdir -p lib

echo -n "Extracting jar files..."
for j in $jars; do
	unzip -p Beads.zip beads/library/$j > lib/$j
	echo -n "."
done

echo "done"
