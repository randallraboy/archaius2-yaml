#!/bin/bash

cur=$(grep 'version' version.properties | cut -d'=' -f2 | tr -d ' \t')

if [ -z "$cur" ]; then
  echo "version.properties is empty"
  exit 1

# branch build, including main, will only build SNAPSHOT
elif printf '%s' "$GITHUB_REF" | grep -Eq '^refs/head/.+$'; then
  # check the declared version in version.properties
  relVer=$(printf '%s' "$cur" | egrep -o '[0-9]+\.[0-9]+\.[0-9]+')
  if [ -z "$relVer" ]; then
    echo "invalid version from version.properties: '$cur'. must in the format: X.X.X-ZZZZZ"
    exit 1
  fi

  echo "version = $relVer-SNAPSHOT" > version.properties

# check if building for a tag, then that's considered a release.
elif printf '%s' "$GITHUB_REF" | grep -Eq '^refs/tags/v[0-9]+\.[0-9]+\.[0-9]+$'; then
  tagVer=$(printf '%s' "$GITHUB_REF" | grep -Eo '[0-9]+\.[0-9]+\.[0-9]+$')
  relVer=$(printf '%s' "$cur" | egrep -o '[0-9]+\.[0-9]+\.[0-9]+')
  # must match what's in version.properties and tag
  if [ "$tagVer" = "$relVer" ]; then
    echo "version = $relVer" > version.properties
  else
    echo "mismatch between version.properties and tag name"
    exit 1
  fi

# invalid tag format
elif printf '%s' "$GITHUB_REF" | grep -Eq '^refs/tags/.+$'; then
  echo "tag should start with 'v' followed by X.X.X, instead got ${GITHUB_REF}"
  exit 1
fi

echo "version to build: $(cat version.properties)"
