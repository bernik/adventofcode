#!/bin/bash -euo pipefail
cd "`dirname $0`"

clj -A:nrepl -M -m nrepl.cmdline --interactive