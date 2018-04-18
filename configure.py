#!/usr/bin/env python
#
#   Configure script
#

import pip
import subprocess
import glob
import os
import os.path as path

"""
    Check if a program is callable
"""
def _check_program(program_name):
    try:
        subprocess.call([program_name, "--version"])
        return True
    except subprocess.CalledProcessError:
        return False

# Verify ninja is installed
if not _check_program("ninja"):
    print("installing ninja...")
    # If it isn't then install it using pip
    pip.main(["install","ninja"])

from ninja.ninja_syntax import Writer


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

        w.variable("SRCDIR", srcdir)
        w.variable("BINDIR", bindir)
        w.newline()

        # Compile java sources rule
        w.rule("compile", "javac -d $BINDIR $in", "Compiling $in")
        w.newline()

        # Glob all java files in the source dir
        globexpr = path.join(srcdir, "src/*.java")

        src_list = glob.glob(globexpr)
        class_list = []

        for srcfile in src_list:
            print("source:", srcfile)

            # Format name of class file
            classfile = path.splitext(srcfile)[0]
            classfile = path.basename(classfile)
            classfile = classfile + ".class"

            # Get absolute source file path
            srcfile = path.abspath(srcfile)

            class_list.append(classfile)
            #w.build([classfile], "compile", [srcfile])

        w.build(class_list, "compile", src_list)

    print("build generated:", buildpath)

    return 0

if (__name__ == "__main__"):
    import sys

    srcdir = path.dirname(__file__)
    bindir = path.join(srcdir, "build")
    sys.exit(configure(srcdir, bindir))
