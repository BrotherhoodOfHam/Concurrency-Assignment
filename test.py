#!/usr/bin/env python

import sys
import subprocess
from time import time
from os.path import dirname, join, abspath

"""
    Tests if a given file containing a list of numbers satisfies the given predicate

    Returns the length of the list of numbers
"""
def check_numberfile(filename, pred):
    count = 0
    with open(filename, "r") as f:
        for line in f.readlines():
            # Ignore whitespace lines
            if not line.isspace():
                count += 1
                # Check if n satisfies the predicate
                n = int(line.strip())
                assert(pred(n))
    # Return number count
    return count

def run_tests():

    bindir = join(dirname(__file__), "build")

    print("running application...")

    # Parameters
    n = 400000
    m = 4
    k = 2

    start = time()

    # Run application
    exitcode = subprocess.call(["java", "-cp", bindir, "Main", str(n), str(m), str(k)])

    end = time()
    print("elapsed(s):", end - start)

    if exitcode != 0:
        print("application failed to run")
        return exitcode

    #####################################################################

    ncount = 0

    evenfile = join(bindir, "even-numbers")
    print("checking even numbers:", abspath(evenfile))

    ncount += check_numberfile(evenfile, lambda n : (n % 2)  == 0)
    

    oddfile = join(bindir, "odd-numbers")
    print("checking odd numbers:", abspath(oddfile))

    ncount += check_numberfile(oddfile, lambda n : (n % 2)  != 0)

    assert(ncount == n)
    print("all numbers generated:", ncount)

    #####################################################################

    print("all tests passed.")

    return 0

if __name__ == "__main__":
    sys.exit(run_tests())
