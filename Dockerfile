FROM tonsky/ubuntu-14.04:latest

WORKDIR /root/skija
ADD . .
RUN /bin/bash -ic ./script/install.sh