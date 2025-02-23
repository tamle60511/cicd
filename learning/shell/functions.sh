#!/bin/sh

# Define your function here
Hello () {
   echo "Hello World $1 $2"
	 Hi
   return 10
}
Hi () {
	echo "Hi world"
}
# Invoke your function
Hello Zara Ali

# Capture value returnd by last command
ret=$?

echo "Return value is $ret"
