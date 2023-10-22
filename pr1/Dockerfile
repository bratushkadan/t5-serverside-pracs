FROM php:8.1-rc-apache-bullseye

RUN apt-get update && \
  apt install apt-transport-https lsb-release ca-certificates wget -y && \
  wget -O /etc/apt/trusted.gpg.d/php.gpg https://packages.sury.org/php/apt.gpg  && \
  sh -c 'echo "deb https://packages.sury.org/php/ $(lsb_release -sc) main" > /etc/apt/sources.list.d/php.list' && \
  apt update && \
  apt install -y php8.1-common/bullseye php8.1-mysql/bullseye

RUN cp /usr/lib/php/20210902/mysqli.so \
  /usr/local/lib/php/extensions/no-debug-non-zts-20210902/mysqli.so
