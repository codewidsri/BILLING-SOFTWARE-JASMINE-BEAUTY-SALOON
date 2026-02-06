#!/bin/bash

set -e

APP_NAME="BillingApp"
MAIN_CLASS="com.billingapp.Main"

SRC_DIR="src/main/java"
RES_DIR="src/main/resources"
LIB_DIR="lib"
BUILD_DIR="build"
DIST_DIR="dist"

echo "🧹 Cleaning old builds..."
rm -rf $BUILD_DIR
rm -rf $DIST_DIR/*.jar
mkdir -p $BUILD_DIR/classes

echo "📦 Compiling source code..."

CLASSPATH=""
for jar in $LIB_DIR/**/*.jar $LIB_DIR/*.jar; do
  CLASSPATH="$CLASSPATH:$jar"
done

javac \
  -encoding UTF-8 \
  -cp "$CLASSPATH" \
  -d $BUILD_DIR/classes \
  $(find $SRC_DIR -name "*.java")

echo "📁 Copying resources..."
cp -r $RES_DIR/* $BUILD_DIR/classes/

echo "📦 Extracting dependency jars..."
mkdir -p $BUILD_DIR/lib
for jar in $LIB_DIR/**/*.jar $LIB_DIR/*.jar; do
  unzip -oq "$jar" -d $BUILD_DIR/lib
done

echo "📝 Creating MANIFEST..."
cat > $BUILD_DIR/MANIFEST.MF <<EOF
Manifest-Version: 1.0
Main-Class: $MAIN_CLASS
EOF

echo "📦 Creating fat JAR..."
jar cfm \
  $DIST_DIR/$APP_NAME.jar \
  $BUILD_DIR/MANIFEST.MF \
  -C $BUILD_DIR/classes . \
  -C $BUILD_DIR/lib .

echo "✅ Build successful!"
echo "➡ Run using: java -jar dist/$APP_NAME.jar"