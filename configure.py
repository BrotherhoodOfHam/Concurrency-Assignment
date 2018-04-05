#!/usr/bin/env python
#
#   Configure script
#

import subprocess
import glob
import os
import os.path as path
from ninja.ninja_syntax import Writer

"""
    Check if a program is callable
"""
def _check_program(program_name):
    try:
        subprocess.call(["javac", "--version"])
        return True
    except subprocess.CalledProcessError:
        return False

"""
    Configure the build file
"""
def configure(srcdir, bindir):

    if not _check_program("javac"):
        print("javac command could not be found")
        return -1

    print("generating build...")

    if not os.path.exists(bindir):
        print("creating required directories.")
        os.makedirs(bindir)

    # Path of ninja build file
    buildpath = path.abspath(path.join(bindir, "build.ninja"))
    
    with open(buildpath, "w") as buildfile:
        w = Writer(buildfile)

        bindir = path.abspath(bindir)
        srcdir = path.abspath(srcdir)

        w.variable("BINDIR", bindir)

        # Clean sources rule
        w.rule("clean", "rm ./*.class")
        w.newline()

        # Compile java sources rule
        w.rule("compile", "javac -d $BINDIR $in", "Compiling $in")
        w.newline()

        # Glob all java files in the source dir
        globexpr = path.join(srcdir, "*.java")

        for srcfile in glob.glob(globexpr):
            print("source:", srcfile)

            # Format name of class file
            classfile = path.splitext(srcfile)[0]
            classfile = path.basename(classfile)
            classfile = classfile + ".class"

            # Get absolute source file path
            srcfile = path.abspath(srcfile)
            
            w.build([classfile], "compile", [srcfile])

    print("build generated:", buildpath)

    return 0

if (__name__ == "__main__"):
    import sys

    srcdir = path.dirname(__file__)
    bindir = path.join(srcdir, "build")
    sys.exit(configure(srcdir, bindir))