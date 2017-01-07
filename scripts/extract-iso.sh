#!/bin/bash
imgName=${2}
prjMnt=${1}mnt
sudo mount -o loop $imgName $prjMnt
mkdir extract
sudo rsync --exclude=/casper/filesystem.squashfs -a $prjMnt/ extract
sudo unsquashfs $prjMnt/casper/filesystem.squashfs
sudo mv squashfs-root edit
sudo cp /etc/resolv.conf edit/etc/

