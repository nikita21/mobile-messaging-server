#!/bin/bash

CURR_DIR=`dirname $0`
SCRIPT_HOME=`cd $CURR_DIR/..; pwd`
echo "Script Home Directory: $SCRIPT_HOME"

MAIN_CLASS="com.app.mobile.messaging.server.Main"
LOG4J_CONFIG_FILE="$SCRIPT_HOME/res/log4j.properties"

CURR_DATE=`date +"%Y-%m-%d"`
CURR_TIME=`date +"%H-%M-%S"`

export LOG_DIR="$SCRIPT_HOME/../logs/$CURR_DATE"
export LOG_FILE="segment-run-$CURR_TIME.log"

EXPORT_CLASSPATH_SCRIPT="$SCRIPT_HOME/scripts/exportClasspath.sh"
source $EXPORT_CLASSPATH_SCRIPT

if [ ! -d $LOG_DIR ];
then
mkdir -p $LOG_DIR ;
fi

COMMAND="java -cp $APPCLASSPATH $MAIN_CLASS $1 $2"
echo "$COMMAND"
$COMMAND  2>&1 > $LOG_DIR/$LOG_FILE