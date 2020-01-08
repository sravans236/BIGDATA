#!/bin/bash
echo "Shell started.."
echo "You entered Query: $1 :: PathWithFileName: $2"
java -Xmx2G -cp ./target/oracle-connector-jar-with-dependencies.jar com.verizon.Application "$1" "$2"