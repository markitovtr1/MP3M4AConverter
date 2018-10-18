# MP3M4AConverter

This project started as a playground. I wanted to automate some of my usual work when ripping some CDs I buy. At first, I used Mp3tag and AudioCoder. After sometime, I created some scripts to convert musics on Mac with afconverter, but decided to switch back to Windows, only to find out that AudioCoder no longer works for my for some unknown reason.

## Why Guice?

As I wanted to learn how to use Guice, I decided to go with it for creating this application. No special reason.

## How to start this application?

This current version process a directory recursively, searching for MP3 files on that directory and converting then with neroAacEnc if there is no M4A already generated.

```bat
java -jar Mp3M4aConverter-1.0-SNAPSHOT.jar <Path-to-Directory-With-Mp3-Files>
```

There is a single environment property called `SHELL_COMMANDS_WAIT_SECONDS` which sets wait time for all external CLI utilities calls. Default value is 30.

## OS dependencies

This application only works on Windows. Also, you need to have `ffmpeg` and `neroAacEnc` in your PATH to be able to run this application.