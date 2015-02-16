#!/usr/bin/env python
import glob
import json
import numpy as np
import math
import sys

from sklearn.cross_validation import KFold
from sklearn.naive_bayes import GaussianNB


def build_model(X):
    locations = {}
    for x in [item for sublist in X for item in sublist]:
        if x['Location'] in locations:
            if x['Address'] in locations[x['Location']]:
                locations[x['Location']][x['Address']]['score'] += x['Quality']
                locations[x['Location']][x['Address']]['score_total'] += 70
            else:
                locations[x['Location']][x['Address']] = {
                    'score': x['Quality'],
                    'score_total': 70,
                }
        else:
            locations[x['Location']] = {
                x['Address']: {
                    'score': x['Quality'],
                    'score_total': 70,
                }
            }

    for location, values in locations.iteritems():
        for _, val in values.iteritems():
            val['score'] /= (val['score_total']/70.0)

    return locations


def h(Li, fi):
    return 70 - abs(Li - fi)


def predict(model, scan):
    max_prob = -1000000
    max_loc = ''
    for location, vals in model.iteritems():
        prob = 1.0
        for aps in scan:
            if aps['Address'] in vals:
                prob *= h(model[location][aps['Address']]['score'],
                          aps['Quality'])
            else:
                prob *= 0.001

        if prob > max_prob:
            max_prob = prob
            max_loc = location

    return max_loc


def process_location(file):
    scans = []
    with open(file) as f:
        for line in f.read().split('\n'):
            if len(line) == 0:
                continue
            dump = json.loads(line)
            scans.append(dump)
    return scans


def prepare_Xs(X_train, Xs, keys):
    X_prepared = []
    for x in range(len(X_train)):
        out = []
        for i in range(len(keys)):
            if keys[i] in Xs[x]:
                out.append(Xs[x][keys[i]])
            else:
                out.append(0)
        X_prepared.append(out)
    return X_prepared


def sd(T):
    return np.std(T)


def acc_sd(T, C):
    M = [x[0]/float(x[1]) for x in zip(T, C)]
    return sum(M)/len(M), sd(M)


if __name__ == '__main__':

    if len(sys.argv) != 2:
        print("usage: {} <data-directory>".format(sys.argv[0]))
        sys.exit(1)

    DATA_DIR = sys.argv[1]

    ACS = []
    gACS = []

    for x in range(50):
        X = []
        for file in glob.glob(DATA_DIR + '/*/data'):
            l = file.replace('/data', '').replace(DATA_DIR + './', '')
            scans = process_location(file)
            for scan in scans:
                for ap in scan:
                    ap['Location'] = l
                X.append(scan)

        N = len(X)
        kf = KFold(N, n_folds=10, shuffle=True)
        C = []
        T = []

        gC = []
        gT = []
        for train, test in kf:
            X_train = []
            X_test = []
            y_train = []
            y_test = []

            C.append(0)
            T.append(0)
            gC.append(0)
            gT.append(0)

            for i in range(0, N):
                if i in train:
                    X_train.append(X[i])
                    y_train.append(X[i][0]['Location'])
                else:
                    X_test.append(X[i])
                    y_test.append(X[i][0]['Location'])

            model = build_model(X_train)
            for x in X_test:
                C[-1] += 1
                if predict(model, x) == x[0]['Location']:
                    T[-1] += 1
            keys = []
            Xs = []
            for x in X_train:
                out = {}
                for y in x:
                    keys.append(y['Address'])
                    out[y['Address']] = y['Quality']
                Xs.append(out)
            keys = sorted(keys)

            X_prepared = prepare_Xs(X_train, Xs, keys)

            Xst = []
            for x in X_test:
                out = {}
                for y in x:
                    out[y['Address']] = y['Quality']
                Xst.append(out)

            X_pretrained = prepare_Xs(X_test, Xst, keys)

            clf = GaussianNB()
            clf.fit(X_prepared, y_train)

            pred = clf.predict(X_pretrained)
            for loc in pred:
                gC[-1] += 1
                if loc == y_test.pop(0):
                    gT[-1] += 1

        accn, sdn = acc_sd(T, C)
        accg, sdg = acc_sd(gT, gC)
        print "Accuracy naive:    {:.10f}\tsd: {:.10f}".format(accn, sdn)
        print "Accuracy gaussian: {:.10f}\tsd: {:.10f}".format(accg, sdg)
        ACS.append(accn)
        gACS.append(accg)

    print acc_sd(ACS, [1 for x in range(10)])
    print acc_sd(gACS, [1 for x in range(10)])
