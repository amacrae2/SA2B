__author__ = 'alecmacrae'

import argparse

HEAT_SIZE = 4
VERBOSE = False

class Heat():

    def __init__(self):
        self.racers = []

    def add_racer(self, racer):
        self.racers.append(racer)


class Round():

    def __init__(self):
        self.heats = []
        self.racers = []

    def add_heat(self, heat):
        self.heats.append(heat)
        for racer in heat.racers:
            self.racers.append(racer)


def seen_racer_before(heat, seen_racers):
    for heat_racer in heat.racers:
        if heat_racer in seen_racers:
            return True
    return False


def update_seen_racers_map(heat, racer, seen_racers_map):
    for heat_racer in heat.racers:
        seen_racers_map[heat_racer].append(racer)
        seen_racers_map[racer].append(heat_racer)


def find_heats_rounds(num_racers, print_lines=True):
    if num_racers % HEAT_SIZE != 0:
        raise Exception("even heats not possible")
    seen_racers_map = {}
    round_num = 0
    while True:
        round = Round()
        for heat_number in xrange(1, num_racers/HEAT_SIZE+1):
            heat = Heat()
            for racer in xrange(1, num_racers+1):
                if not seen_racers_map.has_key(racer):
                    seen_racers = seen_racers_map[racer] = []
                else:
                    seen_racers = seen_racers_map.get(racer)
                if not seen_racer_before(heat, seen_racers) and racer not in round.racers:
                    update_seen_racers_map(heat, racer, seen_racers_map)
                    heat.add_racer(racer)
                if len(heat.racers) == HEAT_SIZE:
                    round.add_heat(heat)
                    if print_lines and VERBOSE:
                        print "found heat: {}".format(heat.racers)
                    break

        if len(round.heats) == num_racers/HEAT_SIZE:
            round_num += 1
            if print_lines:
                print "found round {}: {}".format(round_num, [heat.racers for heat in round.heats])
        else:
            break
    return round_num

if __name__ == "__main__":
    parser = argparse.ArgumentParser()
    parser.add_argument('--plot', help="plots number of round possible for 0-1024 racers", action="store_true")
    parser.add_argument('-r', '--racers', type=int, help="the number of racers per round", default=16)
    parser.add_argument('-v', '--verbose', action="store_true")
    args = parser.parse_args()

    VERBOSE = args.verbose

    if args.plot:
        num_rounds = []
        num_creatures = []
        for i in xrange(HEAT_SIZE, 1028, HEAT_SIZE):
            rounds = find_heats_rounds(i, print_lines=False)
            num_creatures.append(i)
            num_rounds.append(rounds)
            print i
            print rounds

        import matplotlib.pyplot as plt

        plt.plot(num_creatures, num_rounds)
        plt.show()
    else:
        find_heats_rounds(args.racers)