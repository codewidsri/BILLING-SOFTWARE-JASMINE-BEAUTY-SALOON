#!/bin/bash

echo "=============================="
echo "  Building Java Project..."
echo "=============================="

# Create output directory
mkdir -p out

# Remove old class files
rm -f out/*.class

echo "Compiling all Java files..."

javac -cp ".:lib/sqlite-jdbc.jar:lib/flatlaf.jar:lib/itext/*:lib/slf4j/*" -d out $(find src/main/java/com/billingapp -name "*.java")

# Check for compilation error
if [ $? -ne 0 ]; then
    echo "Compilation failed!"
    exit 1
fi

echo "Copying resources..."
cp -r src/main/resources/* out/ 2>/dev/null

echo "=============================="
echo "  Running Application..."
echo "=============================="

java -cp ".:lib/sqlite-jdbc.jar:lib/flatlaf.jar:lib/itext/*:lib/slf4j/*:out" com.billingapp.Main