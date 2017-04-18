java -jar APKDecompile\apktool\apktool.jar d %1 -s  -o APKDecompile\apkcode
APKDecompile\dex2jar\d2j-dex2jar APKDecompile\apkcode\classes.dex -o APKDecompile\apkcode\classes.jar
java -jar APKDecompile\jd-core\jd-core-java-1.2.jar APKDecompile\apkcode\classes.jar APKDecompile\apkcode\src
exit