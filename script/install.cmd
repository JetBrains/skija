rem rmdir /s /q target
call mvn --file pom.shared.xml -DskipTests install
call .\script\native.cmd
call mvn --file pom.native.xml -DskipTests install