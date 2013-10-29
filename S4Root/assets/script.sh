cd /mnt/extSdCard/gs4ex/
mount -o rw,remount /dev/block/system /system
cp busybox /system/xbin/
/system/xbin/busybox --install -s /system/xbin
cp Superuser.apk /system/app/Superuser.apk
chmod 644 /system/app/Superuser.apk
cp -r system/bin/.ext /system/bin
chmod 06755 /system/bin/.ext/.su
cp system/xbin/su /system/xbin
cp system/xbin/daemonsu /system/xbin
chmod 06755 /system/xbin/su
chmod 06755 /system/xbin/daemonsu
mount -o ro,remount /dev/block/system /system