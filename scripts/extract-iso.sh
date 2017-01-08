#!/bin/bash
imgName=${2}
prjMnt=mnt
cd ${1}

mount -o loop $imgName $prjMnt
echo "Phase 1 out of 5"
rsync --exclude=/casper/filesystem.squashfs -a $prjMnt/ extract
echo "Phase 2 out of 5"
unsquashfs $prjMnt/casper/filesystem.squashfs
echo "Phase 3 out of 5"
mv squashfs-root edit
echo "Phase 4 out of 5"
cp /etc/resolv.conf edit/etc/
echo "Phase 5 out of 5"
umount $prjMnt
