#!/bin/bash
echo "Shell 传递参数实例！";
echo "执行的文件名：$0";
echo "第一个参数为：$1";
echo "第二个参数为：$2";

if [ $1 == "install" ]
  then
  echo "install apk"
  elif [ $1 == "start" ]
  then
  echo "start activity"
  else
  echo "Please make sure the positon variable is start or stop."
fi