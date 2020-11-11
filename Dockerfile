FROM tonsky/ubuntu-14.04:latest

WORKDIR /root/skija

ADD . ./
RUN curl --fail --location --silent --show-error https://github.com/JetBrains/skia-build/releases/download/m87-4893488/Skia-m87-4893488-linux-Release-x64.zip > Skia-m87-4893488-linux-Release-x64.zip.zip
RUN unzip -qq Skia-m87-4893488-linux-Release-x64.zip.zip -d third_party/skia