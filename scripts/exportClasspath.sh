#!/bin/bash

LIB_HOME="$SCRIPT_HOME/lib";
RES_HOME="$SCRIPT_HOME/res";
MAIN_HOME="$SCRIPT_HOME/main";
echo "LIB_HOME: $LIB_HOME"
APPCLASSPATH="$RES_HOME"
for file in `find $LIB_HOME -type f -name "*.jar"`
do
if [ -z $APPCLASSPATH ];
then
        APPCLASSPATH="$file";
else
        APPCLASSPATH="$APPCLASSPATH:$file";
fi
done
export APPCLASSPATH;