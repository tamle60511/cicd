#!/bin/sh

filename=test.txt
vi $filename << EndOfCommands 
i 
this file was created automatically from 
a shell script
^[
ZZ
EndOfCommands