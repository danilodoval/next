#!/bin/sh
set -e

if [ "$1" = 'core' ]; then
    shift;
    # allows passing arguments to java
    # in the "docker run" command
    exec java -jar core.jar "$@"
fi

exec "$@"
