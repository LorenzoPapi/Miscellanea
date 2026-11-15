import requests as req

BASE_URL = "https://esploradati.istat.it/SDMXWS/rest"

def get_data():
    return req.get(f"{BASE_URL}/data/{DATA_FLOW}")

def get_data_structure():
    return req.get(f"{BASE_URL}/datastructure/IT1/{DS_REF}")

def get_constraints():
    return req.get(f"{BASE_URL}/availableconstraint/{DATA_FLOW}")

DATA_FLOW = "22_315_DF_DCIS_POPORESBIL1_24"
DS_REF = "DCIS_POPORESBIL1"

FILTERS = ["M", ".", "POPEND", "9"]

QUERIES = "firstNObservations=10"

with open("ref.xml", "w") as ref:
    ref.write(get_constraints().text)