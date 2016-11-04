import argparse

NUM_CHAOS = 160
GROUP_SIZE = 16
HEAT_SIZE = 4


def group_into_heats(chaos):
    # assert len(chaos) == NUM_CHAOS
    chao_idx = 0
    for i in xrange(NUM_CHAOS / GROUP_SIZE):
        print "wave {}".format(i + 1)
        print "round 1"
        for j in xrange(GROUP_SIZE / HEAT_SIZE):
            for i in xrange(HEAT_SIZE):
                print chaos[chao_idx].strip()
                chao_idx += 1
            print ""


if __name__ == "__main__":
    parser = argparse.ArgumentParser(formatter_class=argparse.ArgumentDefaultsHelpFormatter)
    parser.add_argument('file', help="file with list to reverse")
    args = parser.parse_args()
    with open(args.file) as f:
        lines = f.readlines()
    group_into_heats(lines)
