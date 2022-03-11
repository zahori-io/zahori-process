#!/usr/bin/env python

import requests
import json
import time
import sys
import os
from prettytable import PrettyTable

class bcolors:
    HEADER = '\033[95m'
    OKBLUE = '\033[94m'
    OKCYAN = '\033[96m'
    OKGREEN = '\033[92m'
    WARNING = '\033[93m'
    FAIL = '\033[91m'
    ENDC = '\033[0m'
    BOLD = '\033[1m'
    UNDERLINE = '\033[4m'

# Login

user_credentials = {"username": os.environ.get('ZAHORI_USER'),"password": os.environ.get('ZAHORI_PASSWORD')}

r = requests.post(f"https://{os.environ.get('ZAHORI_SERVER')}/login", json=user_credentials)

token = r.json()['token']


# Test configuration
configuration = {} # Insert your execution configuration here

# Launch tests
r = requests.post(f"https://{os.environ.get('ZAHORI_SERVER')}/api/process/2/executions", json=configuration, headers={
    'Authorization': token
})

#print(r.json())

executionId = r.json()['executionId']
print("Execution id: {0}".format(executionId))


# Get status

status = 'Running'
max_retries = 30

for retry in range(max_retries):
    r = requests.get(f"https://{os.environ.get('ZAHORI_SERVER')}/api/execution/{0}".format(executionId), headers={
        'Authorization': token
    })
    status = r.json()['status']
    if status == 'SUCCESS':
        print(f"{bcolors.OKGREEN}{status}{bcolors.ENDC}")
        t = PrettyTable(['Test', 'Screen Resolution', 'Status', 'Browser', 'Notes'])
        print(f"See full log at https://{os.environ.get('ZAHORI_SERVER')}/#/app/process/executions")
        for case in r.json()['casesExecutions']:
            if case['status'].lower() == 'failed':
                t.add_row([case['cas']['name'],case['screenResolution'],f"{bcolors.FAIL}{case['status']}{bcolors.ENDC}",case['browser']['browserName'],case['notes'][:100]])
            elif case['status'].lower() == 'passed':
                t.add_row([case['cas']['name'],case['screenResolution'],f"{bcolors.OKGREEN}{case['status']}{bcolors.ENDC}",case['browser']['browserName'],''])
        print(t.get_string(sortby=("Browser")))
        print(f"{bcolors.OKGREEN}Total Passed: {r.json()['totalPassed']}{bcolors.ENDC}")
        print(f"{bcolors.FAIL}Total Failed: {r.json()['totalFailed']}{bcolors.ENDC}")
        break
    elif status == 'FAILURE':
        print(f"{bcolors.FAIL}{status}{bcolors.ENDC}")
        t = PrettyTable(['Test', 'Screen Resolution', 'Status', 'Browser', 'Notes'])
        print(f"See full log at https://{os.environ.get('ZAHORI_SERVER')}/#/app/process/executions")
        for case in r.json()['casesExecutions']:
            if case['status'].lower() == 'failed':
                t.add_row([case['cas']['name'],case['screenResolution'],f"{bcolors.FAIL}{case['status']}{bcolors.ENDC}",case['browser']['browserName'],case['notes'][:100]])
            elif case['status'].lower() == 'passed':
                t.add_row([case['cas']['name'],case['screenResolution'],f"{bcolors.OKGREEN}{case['status']}{bcolors.ENDC}",case['browser']['browserName'],''])
        print(t.get_string(sortby=("Browser")))
        print(f"{bcolors.OKGREEN}Total Passed: {r.json()['totalPassed']}{bcolors.ENDC}")
        print(f"{bcolors.FAIL}Total Failed: {r.json()['totalFailed']}{bcolors.ENDC}")
        sys.exit(1)
    time.sleep(20)
