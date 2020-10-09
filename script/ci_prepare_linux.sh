#!/bin/bash
set -o errexit -o nounset -o pipefail

apt --quiet --yes install ninja-build libglu1-mesa-dev
add-apt-repository --yes ppa:ubuntu-toolchain-r/test
apt --quiet --yes update
apt --quiet --yes install gcc-9 g++-9
update-alternatives --install /usr/bin/gcc gcc /usr/bin/gcc-9 60 --slave /usr/bin/g++ g++ /usr/bin/g++-9
update-alternatives --config gcc
