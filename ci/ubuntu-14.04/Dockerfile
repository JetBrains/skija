FROM ubuntu:14.04

WORKDIR /root

RUN apt-get install -y software-properties-common
RUN add-apt-repository -y ppa:ubuntu-toolchain-r/test
RUN add-apt-repository -y ppa:openjdk-r/ppa
RUN add-apt-repository -y ppa:git-core/ppa
RUN apt-get update -y
RUN apt-get install -y binutils-2.26 wget curl zip python git build-essential libssl-dev gcc-9 g++-9 ninja-build fontconfig libfontconfig1-dev libglu1-mesa-dev openjdk-11-jdk-headless
RUN update-alternatives --install /usr/bin/gcc gcc /usr/bin/gcc-9 60 --slave /usr/bin/g++ g++ /usr/bin/g++-9
# RUN update-alternatives --config gcc
RUN wget --no-verbose http://www.cmake.org/files/v3.18/cmake-3.18.4.tar.gz && tar -xzf cmake-3.18.4.tar.gz && cd cmake-3.18.4/ && ./configure && make && make install
RUN wget --no-verbose https://downloads.apache.org/maven/maven-3/3.6.3/binaries/apache-maven-3.6.3-bin.tar.gz && tar -xzf apache-maven-3.6.3-bin.tar.gz
RUN echo 'export PATH=/root/apache-maven-3.6.3/bin:/usr/lib/binutils-2.26/bin:$PATH' >> $HOME/.bashrc
RUN echo 'export JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64' >> $HOME/.bashrc
RUN /usr/bin/printf '\xfe\xed\xfe\xed\x00\x00\x00\x02\x00\x00\x00\x00\xe2\x68\x6e\x45\xfb\x43\xdf\xa4\xd9\x92\xdd\x41\xce\xb6\xb2\x1c\x63\x30\xd7\x92' > /etc/ssl/certs/java/cacerts
RUN /var/lib/dpkg/info/ca-certificates-java.postinst configure
