cd /mnt/extSdCard/gs4ex/
mount -o rw,remount /dev/block/system /system
cat busybox /system/xbin/busybox
/system/xbin/busybox --install -s /system/xbin
cat Superuser.apk /system/app/Superuser.apk
chmod 644 /system/app/Superuser.apk
mkdir /system/bin/.ext
cat system/bin/.ext/.su /system/bin/.ext/.su
chmod 06755 /system/bin/.ext/.su
cat system/xbin/su /system/xbin/su
cat system/xbin/daemonsu /system/xbin/daemonsu
chmod 06755 /system/xbin/su
chmod 06755 /system/xbin/daemonsu
mount -o ro,remount /dev/block/system /system