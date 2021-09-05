#!/bin/bash
# NOTE: don't execute directly. Use ./run_test.sh

source assert.sh

function start_test() {
  local name="$1"
  echo ">> Test: $name"
}

function end_test() {
  echo "=================="
}

#####
start_test "version.properties is empty, should exit '1' and print 'version.properties is empty'"
echo "" > version.properties

out=$(./../calc_version.sh)

assert_eq 1 $? "expected 1 exit code"
assert_contain "version.properties is empty", "$out", "expected error message"
end_test

#####
start_test "main branch build but with invalid version declared, then error"
echo "version = 1.1" > version.properties
export GITHUB_REF=refs/head/main

out=$(./../calc_version.sh)

assert_eq 1 $? "expected 1 exit code"
assert_eq "invalid version from version.properties: '1.1'. must in the format: X.X.X-ZZZZZ", "$out", "expected error message"
end_test

#####
start_test "main branch build with valid version declared, then version should be SNAPSHOT"
echo "version = 1.1.0" > version.properties
export GITHUB_REF=refs/head/main

out=$(./../calc_version.sh)

assert_eq 0 $? "expected 0 exit code"
assert_eq "version to build: version = 1.1.0-SNAPSHOT", "$out", "expected error message"
end_test

#####
start_test "invalid tag format, then error"
echo "version = 1.1.0" > version.properties
export GITHUB_REF=refs/tags/1.1.1

out=$(./../calc_version.sh)

assert_eq 1 $? "expected 1 exit code"
assert_eq "tag should start with 'v' followed by X.X.X, instead got refs/tags/1.1.1", "$out", "expected error message"
end_test

#####
start_test "valid tag ref but doesn't match version.properties, then error"
echo "version = 1.1.0" > version.properties
export GITHUB_REF=refs/tags/v1.1.1

out=$(./../calc_version.sh)

assert_eq 1 $? "expected 1 exit code"
assert_eq "mismatch between version.properties and tag name", "$out", "expected error message"
end_test

#####
start_test "valid tag ref and matches match version.properties, then okay"
echo "version = 1.2.3-SNAPSHOT" > version.properties
export GITHUB_REF=refs/tags/v1.2.3

out=$(./../calc_version.sh)

assert_eq 0 $? "expected 0 exit code"
assert_eq "version to build: version = 1.2.3", "$out", "expected error message"
end_test
