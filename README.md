# ZynpoChess

### Download Links

Git:<br>
https://git-scm.com/download<br>
Git-2.18.0-64-bit.exe

SourceTree:<br>
https://www.sourcetreeapp.com/
SourceTreeSetup-2.6.10.exe

IntelliJ (Community edition):<br>
https://www.jetbrains.com/idea/download/<br>
ideaIC-2018.2.3.exe

Java SE Runtime Environment (JRE):<br>
http://www.oracle.com/technetwork/java/javase/downloads/jre8-downloads-2133155.html<br>
jre-8u112-windows-x64.exe<br>
Installed to `C:\Program Files\Java\jre1.8.0_112`

Java SE Development Kit (JDK) installs the JRE:<br>
http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html<br>
jdk-10.0.2_windows-x64_bin.exe<br>
Installed to `C:\Program Files\Java\jdk-10.0.2`


### Compiling and Running

ZynpoChess.java:<br>
```Java
public class ZynpoChess {

    public static void main(String[] args) {
        System.out.println("Hello, World");
        
        for(int i = 0; i < args.length; ++i) {
            System.out.println(String.format("args[%d] = %s", i, args[i]));
        }
    }

}
```

Command Prompt:<br>
```Command
> set PATH=C:\Program Files\Java\jdk1.8.0_112\bin;%PATH%
> javac ZynpoChess.java
> java ZynpoChess Hello World "Hello World!"

args[0] = Hello
args[1] = World
args[2] = Hello World!
```

