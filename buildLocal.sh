#!/bin/sh

BUILD_DIRECTORY=$1
PROFILE=$2
PROJECT_NAME=media-fever

# Help
# ****
if [ $# -eq 1 ] && [ $1 = -h ]
then
        echo "Help"
        echo "****"
        echo ""
        echo "This script will build the application."
        echo "Available parameters"
        echo ""
        echo " 1) The path to a directory where the code will be checked out and the assemblies would be generated. For example: /home/user/build. Required."
        echo ""
        echo " 2) The Profile used to build the application. For example: qa, staging, production. Required."
        echo ""
        exit 0
fi

# Parameters validation
# ************************
if [ -z "$BUILD_DIRECTORY" ]
then
	echo "[ERROR] The BUILD_DIRECTORY parameter is required"
	echo "Run the script with '-h' for help"
	exit 1;
fi

if [ ! -d "$BUILD_DIRECTORY" ]
then
	echo "[ERROR] - The BUILD_DIRECTORY directory does not exist."
	echo "Run the script with '-h' for help"
	exit 1;
fi

if [ -z "$PROFILE" ]
then
	echo "[ERROR] The PROFILE parameter is required"
	echo "Run the script with '-h' for help"
	exit 1;
fi

SOURCE_DIRECTORY=$BUILD_DIRECTORY

# Assemblies Generation
# ************************

# Generate the server war
cd $SOURCE_DIRECTORY/$PROJECT_NAME/media-fever-server
mvn dependency:resolve -P $PROFILE assembly:assembly -Dmaven.test.skip=true

# Generate the android apk
cd $SOURCE_DIRECTORY/$PROJECT_NAME/media-fever-android
mvn dependency:resolve -P $PROFILE,media-fever-free clean install -Dmaven.test.skip=true
mvn dependency:resolve -P $PROFILE,media-fever-paid install -Dmaven.test.skip=true 

#mvn -P $PROFILE android:undeploy
#mvn -P $PROFILE android:deploy
