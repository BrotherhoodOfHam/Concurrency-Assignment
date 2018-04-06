#!/usr/bin/env python

import sys
import subprocess
from os.path import dirname, join, abspath

"""
    Tests if a given file containing a list of numbers satisfies the given predicate

    Returns true if the test passes, or false if it fails and the value that caused it to fail
"""
def test_numberfile(filename, pred):
    with open(filename, "r") as f:
        for line in f.readlines():
            # Ignore whitespace lines
            if not line.isspace():
                # Check if n satisfies the predicate
                n = int(line.strip())
                if not pred(n):
                    return False, n
    return True, 0

def run_tests():

    bindir = join(dirname(__file__), "build")

    print("running application...")

    # Run application
    exitcode = subprocess.call(["java", "-cp", bindir, "Main"])

    if exitcode != 0:
        print("application failed to run")
        return exitcode

    #####################################################################

    evenfile = join(bindir, "even-numbers")

    print("testing even numbers:", abspath(evenfile))

    passed, n = test_numberfile(evenfile, lambda n : (n % 2)  == 0)
    
    if not passed:
        print(n, "is not even")
        return -1

    #####################################################################

    oddfile = join(bindir, "odd-numbers")

    print("testing odd numbers:", abspath(oddfile))

    passed, n = test_numberfile(oddfile, lambda n : (n % 2)  != 0)
    
    if not passed:
        print(n, "is not odd")
        return -1

    #####################################################################

    print("all tests passed.")

    return 0

if __name__ == "__main__":
    sys.exit(run_tests())
