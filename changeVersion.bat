@echo off

mvn versions:set -DnewVersion=1.0.0.RELEASE

mvn -N versions:update-child-modules