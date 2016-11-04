__author__ = 'alecmacrae'

import argparse


def reverse_list(file):
    with open(file) as f:
        lines = f.readlines()
        for i in reversed(lines):
            print i.strip()

if __name__ == "__main__":
    parser = argparse.ArgumentParser(formatter_class=argparse.ArgumentDefaultsHelpFormatter)
    parser.add_argument('file', help="file with list to reverse")
    args = parser.parse_args()
    reverse_list(args.file)