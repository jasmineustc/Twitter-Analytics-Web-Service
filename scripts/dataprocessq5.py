

import sys
import os


def main(argv):
    count = 0;
    for line in sys.stdin:
        line = line.strip()
        s = line.split("\t")
        count+=int(s[1])
        sys.stdout.write(s[0]+"\t"+str(count)+"\t"+s[1]+"\n")  


if __name__ == "__main__":
    main(sys.argv)     


