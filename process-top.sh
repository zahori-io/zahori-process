#!/bin/bash
# Display resources consumption of the process. Once in execution, press 'f' to change columns to display
top -p $(pgrep -d',' -f 'zahori-process') -c

