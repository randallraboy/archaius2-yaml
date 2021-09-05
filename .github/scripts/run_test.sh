#!/bin/bash

rm -rf build
mkdir build
pushd build

if [ ! -f "./assert.sh" ]; then
    wget -q 'https://raw.githubusercontent.com/torokmark/assert.sh/main/assert.sh'
    chmod +x assert.sh
fi

./../calc_version_test.sh

popd