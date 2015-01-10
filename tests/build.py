from process import build_model, process_location
import glob

X = []
for file in glob.glob('./*/data'):
    l = file.replace('/data', '').replace('./', '')
    scans = process_location(file)
    for scan in scans:
        for ap in scan:
            ap['Location'] = l
        X.append(scan)

model = build_model(X)

for loc, aps in model.iteritems():
    print 'locator.addLocation()\n.setName("{}")'.format(loc)
    for ssid, signal in aps.iteritems():
        print '.addAP("{}", {})'.format(ssid, signal['score']/70*100.0)
