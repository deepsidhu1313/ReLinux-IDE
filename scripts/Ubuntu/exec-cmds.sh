#!/bin/bash
sudo mount --bind /dev/ edit/dev
chroot edit mount -t proc none /proc
chroot edit mount -t sysfs none /sys
chroot edit mount -t devpts none /dev/pts
echo "                        type the following commands one by one"
echo "                            export HOME=/root"
echo "                            export LC_ALL=C"
echo  "                            exit"
chroot edit
#chroot edit export HOME=/root
#chroot edit export LC_ALL=C
chroot edit dbus-uuidgen > /var/lib/dbus/machine-id
chroot edit dpkg-divert --local --rename --add /sbin/initctl
chroot edit ln -s /bin/true /sbin/initctl

chroot edit dpkg --add-architecture i386
#echo ""
#echo
#echo
#echo "play around ,install repositories, purge any customistation"
#echo "type exit after finish"
sudo chroot edit
#here execute commands


#umount file systems
chroot edit apt-get autoremove && apt-get autoclean
chroot edit rm -rf /tmp/* ~/.bash_history
chroot edit rm /var/lib/dbus/machine-id
chroot edit rm /sbin/initctl
chroot edit dpkg-divert --rename --remove /sbin/initctl
chroot edit umount /proc || chroot edit umount -lf /proc
chroot edit umount /sys
chroot edit umount /dev/pts

sudo umount edit/dev

