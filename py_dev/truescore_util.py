__author__ = 'alecmacrae'

import trueskill

def update_trueskills(chao_trueskills):
    """
    makes updates to the trueskills based on the order
    :param chao_trueskills: list of chao trueskills in the order in which they placed
    :return: new list of trueskills for chaos in the same order in which they placed (were received)
    """
    rating_groups = [(x,) for x in chao_trueskills]
    rated = trueskill.rate(rating_groups)
    return [rated[i][0] for i in xrange(len(rated))]